package net.zuperz.aurora.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.custom.AuroraPedestalBlock;
import net.zuperz.aurora.block.custom.AuroraWireBlock;
import net.zuperz.aurora.block.custom.GoldenCauldronBlock;
import net.zuperz.aurora.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(aurora.MOD_ID);

    public static final DeferredBlock<Block> AURORA_PEDESTAL = registerBlock("aurora_pedestal",
            () -> new AuroraPedestalBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> GOLDEN_CAULDRON = registerBlock("golden_cauldron",
            () -> new GoldenCauldronBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion()));

    public static final DeferredBlock<Block> AURORA_WIRE = registerBlock("aurora_wire",
            () ->  new AuroraWireBlock(BlockBehaviour.Properties.of().noCollission().noOcclusion().instabreak().pushReaction(PushReaction.DESTROY)));

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