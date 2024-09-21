package net.zuperz.aurora.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.custom.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(aurora.MOD_ID);

    public static final DeferredItem<Item> SKRAP_AURORA = ITEMS.register("skrap_aurora",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_INGOT = ITEMS.register("aurora_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_SKULL = ITEMS.register("aurora_skull",
            () -> new AuroraSkullItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> STONE_SKULL = ITEMS.register("stone_skull",
            () -> new AuroraSkullItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FATE_BLADE = ITEMS.register("fate_blade",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STAR_DUST = ITEMS.register("star_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_DUST = ITEMS.register("aurora_dust",
            () -> new ItemNameBlockItem(ModBlocks.AURORA_WIRE.get(), new Item.Properties()));

    public static final DeferredItem<Item> CLAY_DUST = ITEMS.register("clay_dust",
            () -> new ItemNameBlockItem(ModBlocks.CLAY_WIRE.get(), new Item.Properties()));

    public static final DeferredItem<Item> HARD_CLAY_BALL = ITEMS.register("hard_clay_ball",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SKULL_TWIG = ITEMS.register("skull_twig",
            () -> new StoneSkullTwig(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> TWIG = ITEMS.register("twig",
            () -> new TwigItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MATERIALE_TWIG = ITEMS.register("materiale_twig",
            () -> new MaterialeTwigItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> NETHERITE_MATERIALE_TWIG = ITEMS.register("netherite_materiale_twig",
            () -> new NetheriteTwigItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLUESTONE_DUST = ITEMS.register("bluestone_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOFT_CLAY_JAR = ITEMS.register("soft_clay_jar",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CLAY_JAR = ITEMS.register("clay_jar",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SAPLING_CLAY_JAR = ITEMS.register("sapling_clay_jar",
            () -> new SaplingClayJarItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> FIRE_CLAY_JAR = ITEMS.register("fire_clay_jar",
            () -> new SaplingClayJarItem(new Item.Properties().fireResistant()));

    public static final DeferredItem<Item> TWIG_CLAY_JAR = ITEMS.register("twig_clay_jar",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MAGIC_CLAY_JAR = ITEMS.register("magic_clay_jar",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}