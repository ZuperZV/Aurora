package net.zuperz.aurora.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperz.aurora.block.ModBlocks;

public class BlueStoneBlock extends Block {

    public BlueStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.getBlock() == Blocks.GOLD_BLOCK) {
            level.setBlock(belowPos, ModBlocks.AURORA_PILLER.get().defaultBlockState(), 3);
            level.setBlock(pos, ModBlocks.UPPER_AURORA_PILLER.get().defaultBlockState(), 3);
        }
    }
}
