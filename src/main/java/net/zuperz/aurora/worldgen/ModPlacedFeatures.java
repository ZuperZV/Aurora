package net.zuperz.aurora.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.zuperz.aurora.aurora;

import java.util.ArrayList;
import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> VOID_GRASS_PLACED_KEY = registerKey("void_grass_placed");
    public static final ResourceKey<PlacedFeature> STONE_SKULL_PLACED_KEY = registerKey("stone_skull_placed");
    public static final ResourceKey<PlacedFeature> LUMINOUS_ORE_PLACED_KEY = registerKey("luminous_ore_placed");
    public static final ResourceKey<PlacedFeature> COBBLE_VOID_STONE_PLACED_KEY = registerKey("cobble_void_stone_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, VOID_GRASS_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.VOID_GRASS_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, STONE_SKULL_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.STONE_SKULL_KEY),
                ModOrePlacement.commonOrePlacement(4,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(70))));

        register(context, LUMINOUS_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.LUMINOUS_ORE_KEY),
                ModOrePlacement.commonOrePlacement(8,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(196))));

        register(context, COBBLE_VOID_STONE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.COBBLE_VOID_STONE_KEY),
                ModOrePlacement.commonOrePlacement(38,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(96))));

    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}