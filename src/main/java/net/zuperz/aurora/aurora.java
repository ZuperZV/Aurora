package net.zuperz.aurora;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.zuperz.aurora.Recipes.ModRecipes;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.renderer.*;
import net.zuperz.aurora.component.ModDataComponentTypes;
import net.zuperz.aurora.events.ModEvents;
import net.zuperz.aurora.item.ModCreativeModeTabs;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.item.custom.decorator.NumberBarDecorator;
import net.zuperz.aurora.screen.AlcheFlameScreen;
import net.zuperz.aurora.screen.AlterScreen;
import net.zuperz.aurora.screen.ModMenuTypes;
import net.zuperz.aurora.screen.MyBlockScreen;
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
        ModEvents.registerEvents();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModDataComponentTypes.register(modEventBus);

        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        //modEventBus.addListener(this::addCreative);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    /*private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModMenuTypes.MY_MENU.get(), (o, dir) -> {
            if (dir == null) return o.getItemHandler().get();
            if (dir == Direction.DOWN) return o.getOutputItemHandler().get();
            return o.getInputItemHandler().get();
        });
    }
     */
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.AURORA_PEDESTAL_BE.get(), AuroraPedestalBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.GOLDEN_CAULDRON_BE.get(), GoldenCauldronBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.SLAB_BE.get(), PedestalSlabBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.ALTER_BE.get(), AlterBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_POWER_TABLE_BLOCK_ENTITY.get(), ArcanePowerTableBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.MY_MENU.get(), MyBlockScreen::new);
            event.register(ModMenuTypes.ALCHE_FLAME_MENU.get(), AlcheFlameScreen::new);
            event.register(ModMenuTypes.ALTER_MENU.get(), AlterScreen::new);
        }

        @SubscribeEvent
        public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
            event.register(ModItems.AURORA_SKULL.get(), new NumberBarDecorator());
            event.register(ModItems.STONE_SKULL.get(), new NumberBarDecorator());
        }
    }
}