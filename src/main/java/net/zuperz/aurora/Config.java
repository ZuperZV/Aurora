package net.zuperz.aurora;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.zuperz.aurora.aurora;

@EventBusSubscriber(modid = aurora.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    private static final ModConfigSpec.IntValue SHADOW_TELEPORTATION_MAX_BLOCKS = BUILDER
            .comment("The maximum number of blocks for Shadow Teleportation (must be at least 0)")
            .defineInRange("shadowTeleportationMaxBlocks", 32, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SHADOWMAGIC_OVERLAY_POS_X = BUILDER
            .comment("The X position of the ShadowmagicOverlay")
            .defineInRange("shadowmagicOverlayPosX", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SHADOWMAGIC_OVERLAY_POS_Y = BUILDER
            .comment("The Y position of the ShadowmagicOverlay")
            .defineInRange("shadowmagicOverlayPosY", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static int shadowTeleportationMaxBlocks; // New field for max blocks
    public static int shadowmagicOverlayPosX; // New field for overlay X position
    public static int shadowmagicOverlayPosY; // New field for overlay Y position
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();
        shadowTeleportationMaxBlocks = SHADOW_TELEPORTATION_MAX_BLOCKS.get(); // Load the new config value
        shadowmagicOverlayPosX = SHADOWMAGIC_OVERLAY_POS_X.get(); // Load X position
        shadowmagicOverlayPosY = SHADOWMAGIC_OVERLAY_POS_Y.get(); // Load Y position

        // Convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemName)))
                .collect(Collectors.toSet());
    }
}
