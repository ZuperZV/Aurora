package net.zuperz.aurora.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, aurora.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.AURORA_PEDESTAL.get())
                .add(ModBlocks.BLUESTONE.get())
                .add(ModBlocks.UPPER_AURORA_PILLER.get())
                .add(ModBlocks.AURORA_PILLER.get())
                .add(ModBlocks.PEDESTAL_SLAB.get())
                .add(ModBlocks.GOLDEN_CAULDRON.get())
                .add(ModBlocks.ALCHE_FLAME.get())
                .add(ModBlocks.ALTER.get())
                .add(ModBlocks.BEAM.get())
                .add(ModBlocks.UPPER_BEAM.get())
                .add(ModBlocks.SIDE_ARCANE_POWER_TABLE.get())
                .add(ModBlocks.ARCANE_POWER_TABLE.get())
                .add(ModBlocks.ARCANE_PEDESTAL.get())
                .add(ModBlocks.COBBLE_VOID_STONE.get())
                .add(ModBlocks.VOID_STONE.get())
                .add(ModBlocks.LUMINOUS_VOID_STONE.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.AURORA_PEDESTAL.get())
                .add(ModBlocks.BLUESTONE.get())
                .add(ModBlocks.UPPER_AURORA_PILLER.get())
                .add(ModBlocks.AURORA_PILLER.get())
                .add(ModBlocks.PEDESTAL_SLAB.get())
                .add(ModBlocks.GOLDEN_CAULDRON.get())
                .add(ModBlocks.ALCHE_FLAME.get())
                .add(ModBlocks.ALTER.get())
                .add(ModBlocks.BEAM.get())
                .add(ModBlocks.UPPER_BEAM.get())
                .add(ModBlocks.SIDE_ARCANE_POWER_TABLE.get())
                .add(ModBlocks.ARCANE_POWER_TABLE.get())
                .add(ModBlocks.ARCANE_PEDESTAL.get())
                .add(ModBlocks.COBBLE_VOID_STONE.get())
                .add(ModBlocks.VOID_STONE.get())
                .add(ModBlocks.LUMINOUS_VOID_STONE.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
        ;
    }
}