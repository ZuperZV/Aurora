package net.zuperz.aurora.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.custom.AuroraSkullItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(aurora.MOD_ID);

    public static final DeferredItem<Item> SKRAP_AURORA = ITEMS.register("skrap_aurora",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_INGOT = ITEMS.register("aurora_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_SKULL = ITEMS.register("aurora_skull",
            () -> new AuroraSkullItem(0, new Item.Properties()));

    public static final DeferredItem<Item> FATE_BLADE = ITEMS.register("fate_blade",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STAR_DUST = ITEMS.register("star_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AURORA_DUST = ITEMS.register("aurora_dust",
            () -> new ItemNameBlockItem(ModBlocks.AURORA_WIRE.get(), new Item.Properties()));

    public static final DeferredItem<Item> BLUESTONE_DUST = ITEMS.register("bluestone_dust",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SOFT_CLAY_JAR = ITEMS.register("soft_clay_jar",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CLAY_JAR = ITEMS.register("clay_jar",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}