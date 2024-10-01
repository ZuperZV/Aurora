package net.zuperz.aurora.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.util.ModTags;
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


        tag(ModTags.Blocks.GOBLIN_MINEABLE_STONE)
                .add(Blocks.STONE)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.GRANITE)
                .add(Blocks.DIORITE)
                .add(Blocks.ANDESITE)
                .add(Blocks.COBBLESTONE)
                .add(Blocks.GOLD_ORE)
                .add(Blocks.DEEPSLATE_GOLD_ORE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE)
                .add(Blocks.COAL_ORE)
                .add(Blocks.DEEPSLATE_COAL_ORE)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.DEEPSLATE_DIAMOND_ORE)
                .add(Blocks.NETHERRACK)
                .add(Blocks.ANDESITE);
    }
}