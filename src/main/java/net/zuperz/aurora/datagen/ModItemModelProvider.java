package net.zuperz.aurora.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.item.custom.SoulShardItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, aurora.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SKRAP_AURORA.get());
        basicItem(ModItems.STAR_DUST.get());
        basicItem(ModItems.AURORA_INGOT.get());
        basicItem(ModItems.FATE_BLADE.get());
        basicItem(ModItems.AURORA_SKULL.get());
        basicItem(ModItems.STONE_SKULL.get());

        basicItem(ModItems.AURORA_DUST.get());
        basicItem(ModItems.CLAY_DUST.get());
        basicItem(ModItems.HARD_CLAY_BALL.get());
        basicItem(ModItems.TWIG.get());
        basicItem(ModItems.SKULL_TWIG.get());
        basicItem(ModItems.MATERIALE_TWIG.get());
        basicItem(ModItems.NETHERITE_MATERIALE_TWIG.get());

        basicItem(ModItems.CLAY_JAR.get());
        basicItem(ModItems.SOFT_CLAY_JAR.get());

        basicItem(ModItems.SAPLING_CLAY_JAR.get());
        basicItem(ModItems.FIRE_CLAY_JAR.get());
        basicItem(ModItems.TWIG_CLAY_JAR.get());
        basicItem(ModItems.MAGIC_CLAY_JAR.get());

        basicItem(ModItems.AIR.get());
        basicItem(ModItems.VOID_STONE_SHARD.get());
        basicItem(ModItems.LUMINOUS.get());
        basicItem(ModItems.EXTRACTER_CLAY_JAR.get());
        basicItem(ModItems.BLUESTONE_DUST.get());

        basicItem(ModItems.GLOW_STONE_SHARD.get());
        basicItem(ModItems.BLUESTONE_SHARD.get());
        basicItem(ModItems.WITHER_SKULL_CASTING.get());

        summoningScrollItem(ModItems.SUMMONING_SCROLL_ZOMBIE);
        summoningScrollItem(ModItems.SUMMONING_SCROLL_ENDERMAN);
        summoningScrollItem(ModItems.SUMMONING_SCROLL_GHAST);

        basicItem(ModItems.SOUL_SHARD.get());
        soulShardItem(ModItems.SOUL_SHARD_ZOMBIE);
        soulShardItem(ModItems.SOUL_SHARD_SKELETON);
        soulShardItem(ModItems.SOUL_SHARD_ENDERMAN);
        soulShardItem(ModItems.SOUL_SHARD_ALLAY);
        soulShardItem(ModItems.SOUL_SHARD_ARMADILLO);
        soulShardItem(ModItems.SOUL_SHARD_AXOLOTL);
        soulShardItem(ModItems.SOUL_SHARD_BAT);
        soulShardItem(ModItems.SOUL_SHARD_BEE);
        soulShardItem(ModItems.SOUL_SHARD_BOGGED);
        soulShardItem(ModItems.SOUL_SHARD_BLAZE);
        soulShardItem(ModItems.SOUL_SHARD_BREEZE);
        soulShardItem(ModItems.SOUL_SHARD_CAMEL);
        soulShardItem(ModItems.SOUL_SHARD_CAT);
        soulShardItem(ModItems.SOUL_SHARD_CAVE_SPIDER);
        soulShardItem(ModItems.SOUL_SHARD_CHICKEN);
        soulShardItem(ModItems.SOUL_SHARD_CREEPER);
        soulShardItem(ModItems.SOUL_SHARD_PIG);
        soulShardItem(ModItems.SOUL_SHARD_COW);
        soulShardItem(ModItems.SOUL_SHARD_SHEEP);
        soulShardItem(ModItems.SOUL_SHARD_RABBIT);
        soulShardItem(ModItems.SOUL_SHARD_WOLF);
        soulShardItem(ModItems.SOUL_SHARD_GHAST);
        soulShardItem(ModItems.SOUL_SHARD_PILLAGER);
        soulShardItem(ModItems.SOUL_SHARD_VINDICATOR);
        soulShardItem(ModItems.SOUL_SHARD_WITCH);
        soulShardItem(ModItems.SOUL_SHARD_ENDERMITE);
        soulShardItem(ModItems.SOUL_SHARD_PHANTOM);
        soulShardItem(ModItems.SOUL_SHARD_DROWNED);
        soulShardItem(ModItems.SOUL_SHARD_WARDEN);
    }

    private ItemModelBuilder handheldItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder summoningScrollItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + "summoning_scroll"));
    }

    private ItemModelBuilder soulShardItem(DeferredItem<SoulShardItem> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + "soul_shard"));
    }

    private ItemModelBuilder smallAmberItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/small_amber"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder bigAmberItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/big_amber"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + item.getId().getPath()));
    }


    private ItemModelBuilder saplingItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,"block/" + item.getId().getPath()));
    }
    
    public void buttonItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

}