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

        basicItem(ModItems.AURORA_DUST.get());
    }

    private ItemModelBuilder handheldItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder amberItem(DeferredItem<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/amber"))
                .texture("layer1", ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "item/" + item.getId().getPath()));
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