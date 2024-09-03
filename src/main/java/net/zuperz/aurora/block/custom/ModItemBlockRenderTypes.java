package net.zuperz.aurora.block.custom;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;

@EventBusSubscriber(modid = aurora.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModItemBlockRenderTypes {
    @SubscribeEvent
    public static void registerItemModelProperties(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AURORA_WIRE.get(), RenderType.cutout());
        });
    }
}