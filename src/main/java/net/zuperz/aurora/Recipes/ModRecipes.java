package net.zuperz.aurora.Recipes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, aurora.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, aurora.MOD_ID);

    public static void register(IEventBus eventBus){
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }

    public static final Supplier<RecipeType<MyBlockRecipe>> MY_BLOCK_RECIPE_TYPE =
            RECIPE_TYPES.register("my_recipe", () -> MyBlockRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<AlcheFlameRecipe>> ALCHE_FLAME_RECIPE_TYPE =
            RECIPE_TYPES.register("alche_flame", () -> AlcheFlameRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<PedestalSlabRecipe>> PEDESTAL_SLAB_RECIPE_TYPE =
            RECIPE_TYPES.register("pedestal_slab", () -> PedestalSlabRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<PedestalSlabClayRecipe>> PEDESTAL_SLAB_CLAY_RECIPE_TYPE =
            RECIPE_TYPES.register("pedestal_slab_clay", () -> PedestalSlabClayRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<AuroraPillerPedestalSlabRecipe>> AURORA_PILLER_RECIPE_TYPE =
            RECIPE_TYPES.register("aurora_piller_pedestal_slab", () -> AuroraPillerPedestalSlabRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<PedestalSlabRingRecipe>> PEDESTAL_SLAB_RING_RECIPE_TYPE =
            RECIPE_TYPES.register("pedestal_slab_ring", () -> PedestalSlabRingRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<PedestalSlabClayRingRecipe>> PEDESTAL_SLAB_CLAY_RING_RECIPE_TYPE =
            RECIPE_TYPES.register("pedestal_slab_clay_ring", () -> PedestalSlabClayRingRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<AuroraPillerPedestalSlabRingRecipe>> AURORA_PILLER_PEDESTAL_SLAB_RING_RECIPE_TYPE =
            RECIPE_TYPES.register("aurora_piller_pedestal_slab_ring", () -> AuroraPillerPedestalSlabRingRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<BlockInWoldRecipe>> IND_WOLD_BLOCK_RECIPE_TYPE =
            RECIPE_TYPES.register("ind_wold_block", () -> BlockInWoldRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<AlterRecipe>> ALTER_RECIPE_TYPE =
            RECIPE_TYPES.register("alter", () -> AlterRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<AuroraAlterRecipe>> AURORA_ALTER_RECIPE_TYPE =
            RECIPE_TYPES.register("aurora_alter", () -> AuroraAlterRecipe.Type.INSTANCE);



    public static final Supplier<RecipeSerializer<MyBlockRecipe>> MY_BLOCK_SERIALIZER =
            SERIALIZERS.register("my_block", () -> MyBlockRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<AlcheFlameRecipe>> ALCHE_FLAME_SERIALIZER =
            SERIALIZERS.register("alche_flame", () -> AlcheFlameRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<PedestalSlabRecipe>> PEDESTAL_SLAB_SERIALIZER =
            SERIALIZERS.register("pedestal_slab", () -> PedestalSlabRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<PedestalSlabClayRecipe>> PEDESTAL_SLAB_CLAY_SERIALIZER =
            SERIALIZERS.register("pedestal_slab_clay", () -> PedestalSlabClayRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<AuroraPillerPedestalSlabRecipe>> AURORA_PILLER_SLAB_SERIALIZER =
            SERIALIZERS.register("aurora_piller_pedestal_slab", () -> AuroraPillerPedestalSlabRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<PedestalSlabClayRingRecipe>> PEDESTAL_SLAB_CLAY_RING_SERIALIZER =
            SERIALIZERS.register("pedestal_slab_clay_ring", () -> PedestalSlabClayRingRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<PedestalSlabRingRecipe>> PEDESTAL_SLAB_RING_SERIALIZER =
            SERIALIZERS.register("pedestal_slab_ring", () -> PedestalSlabRingRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<AuroraPillerPedestalSlabRingRecipe>> AURORA_PILLER_PEDESTAL_SLAB_RING_SERIALIZER =
            SERIALIZERS.register("aurora_piller_pedestal_slab_ring", () -> AuroraPillerPedestalSlabRingRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<BlockInWoldRecipe>> IND_WOLD_BLOCK_SERIALIZER =
            SERIALIZERS.register("in_world_block", () -> BlockInWoldRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<AlterRecipe>> ALTER_SERIALIZER =
            SERIALIZERS.register("alter", () -> AlterRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeSerializer<AuroraAlterRecipe>> AURORA_ALTER_SERIALIZER =
            SERIALIZERS.register("aurora_alter", () -> AuroraAlterRecipe.Serializer.INSTANCE);
}