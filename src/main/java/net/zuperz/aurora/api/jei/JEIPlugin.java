package net.zuperz.aurora.api.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.zuperz.aurora.Recipes.*;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.screen.AlcheFlameScreen;
import net.zuperz.aurora.screen.MyBlockScreen;
import org.jetbrains.annotations.NotNull;
import net.zuperz.aurora.aurora;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    public static mezz.jei.api.recipe.RecipeType<MyBlockRecipe> MY_BLOCK_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(MyBlockRecipeCategory.UID, MyBlockRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<PedestalSlabRecipe> PEDESTAL_SLAB_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(PedestalSlabRecipeCategory.UID, PedestalSlabRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<PedestalSlabClayRecipe> PEDESTAL_SLAB_CLAY_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(PedestalSlabClayRecipeCategory.UID, PedestalSlabClayRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<AuroraPillerPedestalSlabRecipe> AURORA_PILLER_PEDESTAL_SLAB_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(AuroraPillerPedestalSlabRecipeCategory.UID, AuroraPillerPedestalSlabRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<PedestalSlabRingRecipe> PEDESTAL_SLAB_RING_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(PedestalSlabRingRecipeCategory.UID, PedestalSlabRingRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<PedestalSlabClayRingRecipe> PEDESTAL_SLAB_CLAY_RING_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(PedestalSlabClayRingRecipeCategory.UID, PedestalSlabClayRingRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<AuroraPillerPedestalSlabRingRecipe> AURORA_PILLER_PEDESTAL_SLAB_RING_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(AuroraPillerPedestalSlabRingRecipeCategory.UID, AuroraPillerPedestalSlabRingRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<BlockInWoldRecipe> IND_WOLD_BLOCK_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(InWoldBlockRecipeCategory.UID, BlockInWoldRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<AlcheFlameRecipe> ALCHE_OVEN_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(AlcheOvenRecipeCategory.UID, AlcheFlameRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<AlterRecipe> ALTER_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(AlterRecipeCategory.UID, AlterRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<AuroraAlterRecipe> AURORA_ALTER_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(AuroraAlterRecipeCategory.UID, AuroraAlterRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<ArcanePowerTableRecipe> ARCANE_POWER_TABLE_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(ArcanePowerTableRecipeCategory.UID, ArcanePowerTableRecipe.class);

    public static mezz.jei.api.recipe.RecipeType<ArcanePedestalRecipe> ARCANE_PEDESTAL_TYPE =
            new mezz.jei.api.recipe.RecipeType<>(ArcanePedestalRecipeCategory.UID, ArcanePedestalRecipe.class);



    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();

        registration.addRecipeCategories(new MyBlockRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new PedestalSlabRecipeCategory(jeiHelpers.getGuiHelper()));
        registration.addRecipeCategories(new PedestalSlabRingRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new PedestalSlabClayRecipeCategory(jeiHelpers.getGuiHelper()));
        registration.addRecipeCategories(new PedestalSlabClayRingRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new AuroraPillerPedestalSlabRecipeCategory(jeiHelpers.getGuiHelper()));
        registration.addRecipeCategories(new AuroraPillerPedestalSlabRingRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new AlcheOvenRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new InWoldBlockRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new AlterRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new AuroraAlterRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new ArcanePowerTableRecipeCategory(jeiHelpers.getGuiHelper()));

        registration.addRecipeCategories(new ArcanePedestalRecipeCategory(jeiHelpers.getGuiHelper()));
    }


    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var world = Minecraft.getInstance().level;
        if (world != null) {

            var my_block = world.getRecipeManager();
            registration.addRecipes(MyBlockRecipeCategory.RECIPE_TYPE,
                    getRecipe(my_block, ModRecipes.MY_BLOCK_RECIPE_TYPE.get()));

            var pedestal_slab = world.getRecipeManager();
            registration.addRecipes(PedestalSlabRecipeCategory.RECIPE_TYPE,
                    getRecipe(pedestal_slab, ModRecipes.PEDESTAL_SLAB_RECIPE_TYPE.get()));

            var pedestal_slab_ring = world.getRecipeManager();
            registration.addRecipes(PedestalSlabRingRecipeCategory.RECIPE_TYPE,
                    getRecipe(pedestal_slab_ring, ModRecipes.PEDESTAL_SLAB_RING_RECIPE_TYPE.get()));

            var pedestal_slab_clay = world.getRecipeManager();
            registration.addRecipes(PedestalSlabClayRecipeCategory.RECIPE_TYPE,
                    getRecipe(pedestal_slab_clay, ModRecipes.PEDESTAL_SLAB_CLAY_RECIPE_TYPE.get()));

            var pedestal_slab_clay_ring = world.getRecipeManager();
            registration.addRecipes(PedestalSlabClayRingRecipeCategory.RECIPE_TYPE,
                    getRecipe(pedestal_slab_clay_ring, ModRecipes.PEDESTAL_SLAB_CLAY_RING_RECIPE_TYPE.get()));

            var aurora_piller_pedestal_slab = world.getRecipeManager();
            registration.addRecipes(AuroraPillerPedestalSlabRecipeCategory.RECIPE_TYPE,
                    getRecipe(aurora_piller_pedestal_slab, ModRecipes.AURORA_PILLER_RECIPE_TYPE.get()));

            var aurora_piller_pedestal_slab_ring = world.getRecipeManager();
            registration.addRecipes(AuroraPillerPedestalSlabRingRecipeCategory.RECIPE_TYPE,
                    getRecipe(aurora_piller_pedestal_slab_ring, ModRecipes.AURORA_PILLER_PEDESTAL_SLAB_RING_RECIPE_TYPE.get()));

            var in_world_block = world.getRecipeManager();
            registration.addRecipes(InWoldBlockRecipeCategory.RECIPE_TYPE,
                    getRecipe(in_world_block, ModRecipes.IND_WOLD_BLOCK_RECIPE_TYPE.get()));

            var alche = world.getRecipeManager();
            registration.addRecipes(AlcheOvenRecipeCategory.RECIPE_TYPE,
                    getRecipe(alche, ModRecipes.ALCHE_FLAME_RECIPE_TYPE.get()));

            var alter = world.getRecipeManager();
            registration.addRecipes(AlterRecipeCategory.RECIPE_TYPE,
                    getRecipe(alter, ModRecipes.ALTER_RECIPE_TYPE.get()));

            var aurora_alter = world.getRecipeManager();
            registration.addRecipes(AuroraAlterRecipeCategory.RECIPE_TYPE,
                    getRecipe(aurora_alter, ModRecipes.AURORA_ALTER_RECIPE_TYPE.get()));

            var arcane_power_table = world.getRecipeManager();
            registration.addRecipes(ArcanePowerTableRecipeCategory.RECIPE_TYPE,
                    getRecipe(arcane_power_table, ModRecipes.AURORA_POWER_TABLE_RECIPE_TYPE.get()));

            var arcane_pedestal = world.getRecipeManager();
            registration.addRecipes(ArcanePedestalRecipeCategory.RECIPE_TYPE,
                    getRecipe(arcane_pedestal, ModRecipes.ARCANE_PEDESTAL_RECIPE_TYPE.get()));
        }

    }

    public <C extends RecipeInput, T extends Recipe<C>> List<T> getRecipe(RecipeManager manager, RecipeType<T> recipeType){
        List<T> list = new ArrayList<>();
        manager.getAllRecipesFor(recipeType).forEach(tRecipeHolder -> {
            list.add(tRecipeHolder.value());
        });
        return list;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration){

        var my_block = new ItemStack(ModBlocks.MY_BLOCK.get());
        registration.addRecipeCatalyst(my_block, MyBlockRecipeCategory.RECIPE_TYPE);

        var pedestal_slab = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var pedestal_slab_wire = new ItemStack(ModItems.AURORA_DUST.get());
        registration.addRecipeCatalyst(pedestal_slab, PedestalSlabRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(pedestal_slab_wire, PedestalSlabRecipeCategory.RECIPE_TYPE);

        var pedestal_slab_ring = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var pedestal_slab_ring_wire = new ItemStack(ModItems.AURORA_DUST.get());
        registration.addRecipeCatalyst(pedestal_slab_ring, PedestalSlabRingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(pedestal_slab_ring_wire, PedestalSlabRingRecipeCategory.RECIPE_TYPE);

        var pedestal_slab_clay = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var pedestal_slab_wire_clay = new ItemStack(ModItems.CLAY_DUST.get());
        registration.addRecipeCatalyst(pedestal_slab_clay, PedestalSlabClayRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(pedestal_slab_wire_clay, PedestalSlabClayRecipeCategory.RECIPE_TYPE);

        var arcane_pedestal_clay = new ItemStack(ModBlocks.ARCANE_PEDESTAL.get());
        var arcane_pedestal_wire_clay = new ItemStack(ModBlocks.BEAM.get());
        registration.addRecipeCatalyst(arcane_pedestal_clay, ArcanePedestalRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(arcane_pedestal_wire_clay, ArcanePedestalRecipeCategory.RECIPE_TYPE);

        var pedestal_slab_ring_clay = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var pedestal_slab_ring_wire_clay = new ItemStack(ModItems.CLAY_DUST.get());
        registration.addRecipeCatalyst(pedestal_slab_ring_clay, PedestalSlabClayRingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(pedestal_slab_ring_wire_clay, PedestalSlabClayRingRecipeCategory.RECIPE_TYPE);

        var aurora_piller_pedestal_slab = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var aurora_piller = new ItemStack(ModBlocks.AURORA_PILLER.get());
        var aurora_piller_wire = new ItemStack(ModItems.AURORA_DUST.get());
        registration.addRecipeCatalyst(aurora_piller_pedestal_slab, AuroraPillerPedestalSlabRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_piller, AuroraPillerPedestalSlabRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_piller_wire, AuroraPillerPedestalSlabRecipeCategory.RECIPE_TYPE);

        var aurora_piller_pedestal_slab_ring = new ItemStack(ModBlocks.PEDESTAL_SLAB.get());
        var aurora_piller_ring = new ItemStack(ModBlocks.AURORA_PILLER.get());
        var aurora_piller_wire_ring = new ItemStack(ModItems.AURORA_DUST.get());
        registration.addRecipeCatalyst(aurora_piller_pedestal_slab_ring, AuroraPillerPedestalSlabRingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_piller_ring, AuroraPillerPedestalSlabRingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_piller_wire_ring, AuroraPillerPedestalSlabRingRecipeCategory.RECIPE_TYPE);

        var block_1 = new ItemStack(Blocks.GRASS_BLOCK);
        var block_2 = new ItemStack(Blocks.DIRT);
        var block_3 = new ItemStack(Blocks.STONE);
        registration.addRecipeCatalyst(block_1, InWoldBlockRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(block_2, InWoldBlockRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(block_3, InWoldBlockRecipeCategory.RECIPE_TYPE);

        var alche = new ItemStack(ModBlocks.ALCHE_FLAME.get());
        registration.addRecipeCatalyst(alche, AlcheOvenRecipeCategory.RECIPE_TYPE, RecipeTypes.FUELING);

        var alter = new ItemStack(ModBlocks.ALTER.get());
        var alter2 = new ItemStack(ModBlocks.BEAM.get());
        var alter3 = new ItemStack(ModItems.CLAY_DUST.get());
        registration.addRecipeCatalyst(alter, AlterRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(alter2, AlterRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(alter3, AlterRecipeCategory.RECIPE_TYPE);

        var aurora_alter = new ItemStack(ModBlocks.ALTER.get());
        var aurora_alter2 = new ItemStack(ModBlocks.BEAM.get());
        var aurora_alter3 = new ItemStack(ModItems.AURORA_DUST.get());
        registration.addRecipeCatalyst(aurora_alter, AuroraAlterRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_alter2, AuroraAlterRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(aurora_alter3, AuroraAlterRecipeCategory.RECIPE_TYPE);

        var arcane_power_table = new ItemStack(ModBlocks.ARCANE_POWER_TABLE.get());
        registration.addRecipeCatalyst(arcane_power_table, ArcanePowerTableRecipeCategory.RECIPE_TYPE);

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(MyBlockScreen.class, 72, 34, 24, 17, JEIPlugin.MY_BLOCK_TYPE);

        registration.addRecipeClickArea(AlcheFlameScreen.class, 72, 20, 24, 17, JEIPlugin.ALCHE_OVEN_TYPE);
    }
}