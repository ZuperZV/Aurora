package net.zuperz.aurora.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.custom.*;
import net.zuperz.aurora.block.custom.SlabBlock;
import net.zuperz.aurora.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(aurora.MOD_ID);

    public static final DeferredBlock<Block> AURORA_PEDESTAL = registerBlock("aurora_pedestal",
            () -> new AuroraPedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> MY_BLOCK = registerBlock("my_block",
            () -> new MyBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F)));

    public static final DeferredBlock<Block> ALCHE_FLAME = registerBlock("alche_flame",
            () -> new AlcheFlame(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F)));

    public static final DeferredBlock<Block> GOLDEN_CAULDRON = registerBlock("golden_cauldron",
            () -> new GoldenCauldronBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> PEDESTAL_SLAB = registerBlock("pedestal_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> ALTER = registerBlock("alter",
            () -> new Alter(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> AURORA_WIRE = registerBlock("aurora_wire",
            () ->  new AuroraWireBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> CLAY_WIRE = registerBlock("clay_wire",
            () ->  new ClayWireBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> BLUESTONE = registerBlock("bluestone",
            () ->  new BlueStoneBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> AURORA_PILLER = registerBlock("aurora_piller",
            () ->  new AuroraPillerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> UPPER_AURORA_PILLER = registerBlock("upper_aurora_piller",
            () ->  new UpperAuroraPiller(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(10.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> BEAM = registerBlock("beam",
            () ->  new BeamBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(20.0F, 1200.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> UPPER_BEAM = registerBlock("upper_beam",
            () ->  new UpperBeamBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(20.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> ARCANE_POWER_TABLE = registerBlock("arcane_power_table",
            () ->  new ArcanePowerTable(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(25.0F, 1200.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> SIDE_ARCANE_POWER_TABLE = registerBlock("side_arcane_power_table",
            () ->  new SideArcanePowerTable(BlockBehaviour.Properties.of().strength(25.0F, 1200.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> STONE_SKULL_STONE = registerBlock("stone_skull_stone",
            () ->  new TwigableBlock(Blocks.STONE, SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED, BlockBehaviour.Properties.of().noLootTable()));

    public static final DeferredBlock<Block> ARCANE_PEDESTAL = registerBlock("arcane_pedestal",
            () ->  new ArcanePedestalBlock(BlockBehaviour.Properties.of().strength(15.0F, 1200.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 4)));

    public static final DeferredBlock<Block> VOID_STONE = registerBlock("void_stone",
            () ->  new VoidStoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.5F, 7.5F)));

    public static final DeferredBlock<Block> LUMINOUS_VOID_STONE = registerBlock("luminous_void_stone",
            () ->  new DropExperienceBlock(UniformInt.of(1, 5), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 7.5F)));

    public static final DeferredBlock<Block> COBBLE_VOID_STONE = registerBlock("cobble_void_stone",
            () ->  new VoidStoneBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 7.0F)));

    public static final DeferredBlock<Block> RIFT_BLOCK = BLOCKS.register("rift_block", RiftPortalBlock::new);

    public static final DeferredBlock<Block> VOID_GRASS = registerBlock("void_grass",
            () -> new VoidFlowerBlock(MobEffects.WEAKNESS, (int) 9.0F, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));


    //arcane_pedestal

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}