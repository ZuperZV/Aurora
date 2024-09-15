package net.zuperz.aurora.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.*;

import java.util.function.Supplier;


public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, aurora.MOD_ID);

    public static final Supplier<BlockEntityType<AuroraPedestalBlockEntity>> AURORA_PEDESTAL_BE =
            BLOCK_ENTITIES.register("aurora_pedestal_be", () -> BlockEntityType.Builder.of(
                    AuroraPedestalBlockEntity::new, ModBlocks.AURORA_PEDESTAL.get()).build(null));

    public static final Supplier<BlockEntityType<GoldenCauldronBlockEntity>> GOLDEN_CAULDRON_BE =
            BLOCK_ENTITIES.register("golden_cauldron_be", () -> BlockEntityType.Builder.of(
                    GoldenCauldronBlockEntity::new, ModBlocks.GOLDEN_CAULDRON.get()).build(null));

    public static final Supplier<BlockEntityType<PedestalSlabBlockEntity>> SLAB_BE =
            BLOCK_ENTITIES.register("slab_be", () -> BlockEntityType.Builder.of(
                    PedestalSlabBlockEntity::new, ModBlocks.PEDESTAL_SLAB.get()).build(null));

    public static final Supplier<BlockEntityType<AlterBlockEntity>> ALTER_BE =
            BLOCK_ENTITIES.register("alter_be", () -> BlockEntityType.Builder.of(
                    AlterBlockEntity::new, ModBlocks.ALTER_SLAB.get()).build(null));

    public static final Supplier<BlockEntityType<MyBlockTile>> MY_TILE =
            BLOCK_ENTITIES.register("my_tile", () -> BlockEntityType.Builder.of(
                    MyBlockTile::new, ModBlocks.MY_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<AlcheFlameBlockEntity>> ALCHE_FLAME_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("alche_flame_block_entity", () -> BlockEntityType.Builder.of(
                    AlcheFlameBlockEntity::new, ModBlocks.ALCHE_FLAME.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}