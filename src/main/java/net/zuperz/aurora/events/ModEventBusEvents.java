package net.zuperz.aurora.events;

import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.zuperz.aurora.Entity.ModEntities;
import net.zuperz.aurora.Entity.client.ChainModel;
import net.zuperz.aurora.Entity.client.GoblinMinerModel;
import net.zuperz.aurora.Entity.client.ModModelLayers;
import net.zuperz.aurora.Entity.client.VoidWalkerModel;
import net.zuperz.aurora.Entity.custom.ChainEntity;
import net.zuperz.aurora.Entity.custom.GoblinMinerEntity;
import net.zuperz.aurora.Entity.custom.VoidWalkerEntity;
import net.zuperz.aurora.aurora;

@EventBusSubscriber(modid = aurora.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CHAIN, ChainModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.VOID_WALKER, VoidWalkerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.GOBLIN_MINER, GoblinMinerModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CHAIN.get(), ChainEntity.createAttributes().build());
        event.put(ModEntities.VOID_WALKER.get(), VoidWalkerEntity.createAttributes().build());
        event.put(ModEntities.GOBLIN_MINER.get(), GoblinMinerEntity.createAttributes().build());
    }
}