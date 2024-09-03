package net.zuperz.aurora;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.renderer.AuroraPedestalBlockEntityRenderer;
import net.zuperz.aurora.block.entity.renderer.GoldenCauldronBlockEntityRenderer;
import net.zuperz.aurora.item.ModCreativeModeTabs;
import net.zuperz.aurora.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(aurora.MOD_ID)
public class aurora {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "aurora";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public aurora(IEventBus modEventBus) {

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        //ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        //ModEntities.register(modEventBus);
        //ModSounds.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);

        // Register the commonSetup method for modloading
        //modEventBus.addListener(this::commonSetup);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.AURORA_PEDESTAL_BE.get(), AuroraPedestalBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.GOLDEN_CAULDRON_BE.get(), GoldenCauldronBlockEntityRenderer::new);
        }
    }
}