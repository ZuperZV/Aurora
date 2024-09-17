package net.zuperz.aurora.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, aurora.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        horizontalBlock(ModBlocks.AURORA_PEDESTAL.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/aurora_pedestal")));

        horizontalBlock(ModBlocks.GOLDEN_CAULDRON.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/golden_cauldron")));

        horizontalBlock(ModBlocks.PEDESTAL_SLAB.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/pedestal_slab")));

        horizontalBlock(ModBlocks.AURORA_PILLER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/aurora_piller")));

        horizontalBlock(ModBlocks.UPPER_AURORA_PILLER.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/upper_aurora_piller")));

        horizontalBlock(ModBlocks.BEAM.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/beam")));

        horizontalBlock(ModBlocks.UPPER_BEAM.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/upper_beam")));

        horizontalBlock(ModBlocks.ARCANE_POWER_TABLE.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/arcane_power_table")));

        blockWithItem(ModBlocks.BLUESTONE);
        blockWithItem(ModBlocks.STONE_SKULL_STONE);
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("aurora:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<Block> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("aurora:block/" + deferredBlock.getId().getPath() + appendix));
    }

    private void leavesBlock(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(deferredBlock.get())).renderType("cutout"));
    }

    private void saplingBlock(DeferredBlock<Block> deferredBlock) {
        simpleBlock(deferredBlock.get(), models().cross(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), blockTexture(deferredBlock.get())).renderType("cutout"));
    }
}