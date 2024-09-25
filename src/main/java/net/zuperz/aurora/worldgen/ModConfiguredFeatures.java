package net.zuperz.aurora.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.SpruceFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> VOID_GRASS_KEY = registerKey("void_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STONE_SKULL_KEY = registerKey("stone_skull_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LUMINOUS_ORE_KEY = registerKey("luminous_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> COBBLE_VOID_STONE_KEY = registerKey("cobble_void_stone");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new BlockMatchTest(Blocks.STONE);
        RuleTest voidstoneReplaceabele = new BlockMatchTest(ModBlocks.VOID_STONE.get());

        register(context, STONE_SKULL_KEY, Feature.ORE, new OreConfiguration(stoneReplaceables,
                ModBlocks.STONE_SKULL_STONE.get().defaultBlockState(), 1));

        register(context, VOID_GRASS_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(32, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.VOID_GRASS.get())))));

        List<OreConfiguration.TargetBlockState> LuminousOre = List.of(OreConfiguration.target(voidstoneReplaceabele,
                        ModBlocks.LUMINOUS_VOID_STONE.get().defaultBlockState()));

        register(context, LUMINOUS_ORE_KEY, Feature.ORE, new OreConfiguration(LuminousOre, 8));

        List<OreConfiguration.TargetBlockState> cobbleVoidStone = List.of(OreConfiguration.target(voidstoneReplaceabele,
                ModBlocks.COBBLE_VOID_STONE.get().defaultBlockState()));

        register(context, COBBLE_VOID_STONE_KEY, Feature.ORE, new OreConfiguration(cobbleVoidStone, 33));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}