package net.zuperz.aurora.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.SpawnEggItem;
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

    public static final DeferredItem<Item> CURSED_TOTEM = ITEMS.register("cursed_totem",
            () -> new CurseTotemItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SUMMONING_SCROLL_ZOMBIE = ITEMS.register("summoning_scroll_zombie",
            () -> new SummoningScrollItem(EntityType.ZOMBIE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SUMMONING_SCROLL_ENDERMAN = ITEMS.register("summoning_scroll_enderman",
            () -> new SummoningScrollItem(EntityType.ENDERMAN, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SUMMONING_SCROLL_GHAST = ITEMS.register("summoning_scroll_ghast",
            () -> new SummoningScrollItem(EntityType.GHAST, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> RIFT = ITEMS.register("rift",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> AIR = ITEMS.register("air",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> VOID_STONE_SHARD = ITEMS.register("void_stone_shard",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LUMINOUS = ITEMS.registerItem("luminous",
            properties -> new FuelItem(properties, 2100), new Item.Properties());

    public static final DeferredItem<Item> EXTRACTER_CLAY_JAR = ITEMS.registerItem("extracter_clay_jar",
            properties -> new ExtracterClayJarItem(new Item.Properties()));

    public static final DeferredItem<Item> GLOW_STONE_SHARD = ITEMS.registerItem("glow_stone_shard",
            properties -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BLUESTONE_SHARD = ITEMS.registerItem("bluestone_shard",
            properties -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WITHER_SKULL_CASTING = ITEMS.registerItem("wither_skull_casting",
            properties -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> WAND_OF_PLACE = ITEMS.registerItem("wand_of_place",
            properties -> new CenterSetterItem(new Item.Properties()));

//Soul Shard
    public static final DeferredItem<Item> SOUL_SHARD = ITEMS.register("soul_shard",
            () -> new SoulShardItem(EntityType.ITEM, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_ZOMBIE = ITEMS.register("soul_shard_zombie",
            () -> new SoulShardItem(EntityType.ZOMBIE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_SKELETON = ITEMS.register("soul_shard_skeleton",
            () -> new SoulShardItem(EntityType.SKELETON, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_ENDERMAN = ITEMS.register("soul_shard_enderman",
            () -> new SoulShardItem(EntityType.ENDERMAN, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_ALLAY = ITEMS.register("soul_shard_allay",
            () -> new SoulShardItem(EntityType.ALLAY, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_ARMADILLO = ITEMS.register("soul_shard_armadillo",
            () -> new SoulShardItem(EntityType.ARMADILLO, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_AXOLOTL = ITEMS.register("soul_shard_axolotl",
            () -> new SoulShardItem(EntityType.AXOLOTL, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_BAT = ITEMS.register("soul_shard_bat",
            () -> new SoulShardItem(EntityType.BAT, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_BEE = ITEMS.register("soul_shard_bee",
            () -> new SoulShardItem(EntityType.BEE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_BOGGED = ITEMS.register("soul_shard_bogged",
            () -> new SoulShardItem(EntityType.BOGGED, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_BLAZE = ITEMS.register("soul_shard_blaze",
            () -> new SoulShardItem(EntityType.BLAZE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_BREEZE = ITEMS.register("soul_shard_breeze",
            () -> new SoulShardItem(EntityType.BREEZE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_CAMEL = ITEMS.register("soul_shard_camel",
            () -> new SoulShardItem(EntityType.CAMEL, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_CAT = ITEMS.register("soul_shard_cat",
            () -> new SoulShardItem(EntityType.CAT, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_CAVE_SPIDER = ITEMS.register("soul_shard_cave_spider",
            () -> new SoulShardItem(EntityType.CAVE_SPIDER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_CHICKEN = ITEMS.register("soul_shard_chicken",
            () -> new SoulShardItem(EntityType.CHICKEN, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_CREEPER = ITEMS.register("soul_shard_creeper",
            () -> new SoulShardItem(EntityType.CREEPER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_PIG = ITEMS.register("soul_shard_pig",
            () -> new SoulShardItem(EntityType.PIG, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_COW = ITEMS.register("soul_shard_cow",
            () -> new SoulShardItem(EntityType.COW, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_SHEEP = ITEMS.register("soul_shard_sheep",
            () -> new SoulShardItem(EntityType.SHEEP, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_RABBIT = ITEMS.register("soul_shard_rabbit",
            () -> new SoulShardItem(EntityType.RABBIT, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_WOLF = ITEMS.register("soul_shard_wolf",
            () -> new SoulShardItem(EntityType.WOLF, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_GHAST = ITEMS.register("soul_shard_ghast",
            () -> new SoulShardItem(EntityType.GHAST, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_PILLAGER = ITEMS.register("soul_shard_pillager",
            () -> new SoulShardItem(EntityType.PILLAGER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_VINDICATOR = ITEMS.register("soul_shard_vindicator",
            () -> new SoulShardItem(EntityType.VINDICATOR, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_WITCH = ITEMS.register("soul_shard_witch",
            () -> new SoulShardItem(EntityType.WITCH, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_ENDERMITE = ITEMS.register("soul_shard_endermite",
            () -> new SoulShardItem(EntityType.ENDERMITE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_PHANTOM = ITEMS.register("soul_shard_phantom",
            () -> new SoulShardItem(EntityType.PHANTOM, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_DROWNED = ITEMS.register("soul_shard_drowned",
            () -> new SoulShardItem(EntityType.DROWNED, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<SoulShardItem> SOUL_SHARD_WARDEN = ITEMS.register("soul_shard_warden",
            () -> new SoulShardItem(EntityType.WARDEN, new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}