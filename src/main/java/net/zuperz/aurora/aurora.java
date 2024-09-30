package net.zuperz.aurora;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zuperz.aurora.Entity.ModEntities;
import net.zuperz.aurora.Entity.client.ChainRenderer;
import net.zuperz.aurora.Entity.client.VoidWalkerRenderer;
import net.zuperz.aurora.Recipes.ModRecipes;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.renderer.*;
import net.zuperz.aurora.component.ModDataComponentTypes;
import net.zuperz.aurora.events.ModEvents;
import net.zuperz.aurora.item.ModCreativeModeTabs;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.item.custom.SoulShardItem;
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

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static net.zuperz.aurora.item.ModItems.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(aurora.MOD_ID)
public class aurora {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "aurora";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Map<EntityType<?>, SoulShardItem> SOUL_SHARD_MAP = new HashMap<>();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public aurora(IEventBus modEventBus, ModContainer modContainer) {
        ModEvents.registerEvents();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModDataComponentTypes.register(modEventBus);

        ModEntities.register(modEventBus);

        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(this::onModSetup);
    }

    private void onModSetup(final FMLCommonSetupEvent event) {
        registerSoulShardItems();
    }

    public static void registerSoulShardItems() {
        SOUL_SHARD_MAP.put(EntityType.ZOMBIE, SOUL_SHARD_ZOMBIE.get());
        SOUL_SHARD_MAP.put(EntityType.SKELETON, SOUL_SHARD_SKELETON.get());
        SOUL_SHARD_MAP.put(EntityType.ENDERMAN, SOUL_SHARD_ENDERMAN.get());
        SOUL_SHARD_MAP.put(EntityType.ALLAY, SOUL_SHARD_ALLAY.get());
        SOUL_SHARD_MAP.put(EntityType.ARMADILLO, SOUL_SHARD_ARMADILLO.get());
        SOUL_SHARD_MAP.put(EntityType.AXOLOTL, SOUL_SHARD_AXOLOTL.get());
        SOUL_SHARD_MAP.put(EntityType.BAT, SOUL_SHARD_BAT.get());
        SOUL_SHARD_MAP.put(EntityType.BEE, SOUL_SHARD_BEE.get());
        SOUL_SHARD_MAP.put(EntityType.BOGGED, SOUL_SHARD_BOGGED.get());
        SOUL_SHARD_MAP.put(EntityType.BLAZE, SOUL_SHARD_BLAZE.get());
        SOUL_SHARD_MAP.put(EntityType.BREEZE, SOUL_SHARD_BREEZE.get());
        SOUL_SHARD_MAP.put(EntityType.CAMEL, SOUL_SHARD_CAMEL.get());
        SOUL_SHARD_MAP.put(EntityType.CAT, SOUL_SHARD_CAT.get());
        SOUL_SHARD_MAP.put(EntityType.CAVE_SPIDER, SOUL_SHARD_CAVE_SPIDER.get());
        SOUL_SHARD_MAP.put(EntityType.CHICKEN, SOUL_SHARD_CHICKEN.get());
        SOUL_SHARD_MAP.put(EntityType.CREEPER, SOUL_SHARD_CREEPER.get());
        SOUL_SHARD_MAP.put(EntityType.PIG, SOUL_SHARD_PIG.get());
        SOUL_SHARD_MAP.put(EntityType.COW, SOUL_SHARD_COW.get());
        SOUL_SHARD_MAP.put(EntityType.SHEEP, SOUL_SHARD_SHEEP.get());
        SOUL_SHARD_MAP.put(EntityType.RABBIT, SOUL_SHARD_RABBIT.get());
        SOUL_SHARD_MAP.put(EntityType.WOLF, SOUL_SHARD_WOLF.get());
        SOUL_SHARD_MAP.put(EntityType.GHAST, SOUL_SHARD_GHAST.get());
        SOUL_SHARD_MAP.put(EntityType.PILLAGER, SOUL_SHARD_PILLAGER.get());
        SOUL_SHARD_MAP.put(EntityType.VINDICATOR, SOUL_SHARD_VINDICATOR.get());
        SOUL_SHARD_MAP.put(EntityType.WITCH, SOUL_SHARD_WITCH.get());
        SOUL_SHARD_MAP.put(EntityType.ENDERMITE, SOUL_SHARD_ENDERMITE.get());
        SOUL_SHARD_MAP.put(EntityType.PHANTOM, SOUL_SHARD_PHANTOM.get());
        SOUL_SHARD_MAP.put(EntityType.DROWNED, SOUL_SHARD_DROWNED.get());
        SOUL_SHARD_MAP.put(EntityType.WARDEN, SOUL_SHARD_WARDEN.get());
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        LOGGER.info("Blocks >> {}", Config.shadowTeleportationMaxBlocks);
    }

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
            event.registerBlockEntityRenderer(ModBlockEntities.ARCANE_PEDESTAL_BLOCK_ENTITY.get(), ArcanePedestalBlockEntityRenderer::new);
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

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.CHAIN.get(), ChainRenderer::new);
            EntityRenderers.register(ModEntities.VOID_WALKER.get(), VoidWalkerRenderer::new);
        }
    }
}