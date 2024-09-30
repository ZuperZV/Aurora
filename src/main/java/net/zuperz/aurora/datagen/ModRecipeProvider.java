package net.zuperz.aurora.datagen;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pWriter) {

        SimpleCookingRecipeBuilder.blasting (Ingredient.of(ModItems.SOFT_CLAY_JAR.get()), RecipeCategory.MISC , ModItems.CLAY_JAR.get(), 0.15f , 200)
                .unlockedBy("has_soft_clay_jar",inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SOFT_CLAY_JAR.get()).build()))
                .save(pWriter, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "clay_jar_from_blasting"));

        SimpleCookingRecipeBuilder.smelting (Ingredient.of(ModItems.SOFT_CLAY_JAR.get()), RecipeCategory.MISC , ModItems.CLAY_JAR.get(), 0.15f , 200)
                .unlockedBy("has_soft_clay_jar",inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.SOFT_CLAY_JAR.get()).build()))
                .save(pWriter);

        SimpleCookingRecipeBuilder.smelting ((Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "saplings")))), RecipeCategory.MISC , Items.CHARCOAL, 0.15f , 200)
                .unlockedBy("has_sapling",inventoryTrigger(ItemPredicate.Builder.item().of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft", "saplings"))).build()))
                .save(pWriter, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "charcoal_from_saplings"));

        SimpleCookingRecipeBuilder.smelting ((Ingredient.of(ModItems.TWIG)), RecipeCategory.MISC , Items.CHARCOAL, 0.20f , 200)
                .unlockedBy("has_twig",inventoryTrigger(ItemPredicate.Builder.item().of(ModItems.TWIG).build()))
                .save(pWriter, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "charcoal_from_twig"));

        SimpleCookingRecipeBuilder.smelting ((Ingredient.of(Items.LAPIS_LAZULI)), RecipeCategory.MISC , Items.CYAN_DYE, 0.15f , 100)
                .unlockedBy("has_lapis",inventoryTrigger(ItemPredicate.Builder.item().of(Items.LAPIS_LAZULI).build()))
                .save(pWriter);

        SimpleCookingRecipeBuilder.smelting ((Ingredient.of(ModBlocks.COBBLE_VOID_STONE)), RecipeCategory.MISC , ModBlocks.VOID_STONE, 0.20f , 200)
                .unlockedBy("has_cobble_void_stone",inventoryTrigger(ItemPredicate.Builder.item().of(ModBlocks.COBBLE_VOID_STONE).build()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLUESTONE.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', ModItems.BLUESTONE_DUST)
                .unlockedBy("has_bluestone", has(ModItems.BLUESTONE_DUST)).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOFT_CLAY_JAR.get())
                .pattern(" A ")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.CLAY_BALL)
                .unlockedBy("has_clay_ball", has(Items.CLAY_BALL)).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ALCHE_FLAME.get())
                .pattern("EBE")
                .pattern("CAC")
                .pattern("DDD")
                .define('A', Blocks.BLAST_FURNACE)
                .define('B', Blocks.DEEPSLATE)
                .define('C', ModItems.CLAY_JAR)
                .define('D', Items.IRON_INGOT)
                .define('E', Blocks.STONE)
                .unlockedBy("has_blast_furnace", has(Blocks.BLAST_FURNACE)).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PEDESTAL_SLAB.get())
                .pattern("CDC")
                .pattern("ABA")
                .define('A', ModItems.FIRE_CLAY_JAR)
                .define('B', Blocks.DEEPSLATE)
                .define('C', Blocks.DEEPSLATE_BRICKS)
                .define('D', Blocks.POLISHED_DEEPSLATE)
                .unlockedBy("has_blast_furnace", has(Blocks.BLAST_FURNACE)).save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CLAY_DUST.get())
                .requires(ModItems.HARD_CLAY_BALL.get())
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_hard_clay_ball", inventoryTrigger(ItemPredicate.Builder.item().
                        of(ModItems.HARD_CLAY_BALL.get()).build()))
                .save(pWriter);
    }


    protected static void oreSmelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput pRecipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pRecipeOutput, aurora.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}