package net.zuperz.aurora.block;

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
            () -> new AuroraPedestalBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> MY_BLOCK = registerBlock("my_block",
            () -> new MyBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> ALCHE_FLAME = registerBlock("alche_flame",
            () -> new AlcheFlame(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> GOLDEN_CAULDRON = registerBlock("golden_cauldron",
            () -> new GoldenCauldronBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> PEDESTAL_SLAB = registerBlock("pedestal_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> AURORA_WIRE = registerBlock("aurora_wire",
            () ->  new AuroraWireBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> BLUESTONE = registerBlock("bluestone",
            () ->  new BlueStoneBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> AURORA_PILLER = registerBlock("aurora_piller",
            () ->  new AuroraPillerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 5)));

    public static final DeferredBlock<Block> UPPER_AURORA_PILLER = registerBlock("upper_aurora_piller",
            () ->  new UpperAuroraPiller(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0F, 6.0F).isValidSpawn(Blocks::never).noOcclusion().lightLevel(pos -> 6)));

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