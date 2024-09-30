package net.zuperz.aurora.Entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.Entity.custom.ChainEntity;
import net.zuperz.aurora.Entity.custom.VoidWalkerEntity;
import net.zuperz.aurora.aurora;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, aurora.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ChainEntity>> CHAIN =
            ENTITY_TYPES.register("chain", () -> EntityType.Builder.of(ChainEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F).build("chain"));

    public static final DeferredHolder<EntityType<?>, EntityType<VoidWalkerEntity>> VOID_WALKER =
            ENTITY_TYPES.register("void_walker", () -> EntityType.Builder.of(VoidWalkerEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.3F).build("void_walker"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
