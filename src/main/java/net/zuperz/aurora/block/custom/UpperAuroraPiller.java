package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;

import javax.annotation.Nullable;

public class UpperAuroraPiller extends BarrierBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final double SIZE = 5.0;
    VoxelShape SHAPE = Shapes.or(
            // First shape from [0, -16, 0] to [16, -13, 16]
            box(0.0, -16.0, 0.0, 16.0, -13.0, 16.0),

            // Second shape from [0, -11, 0] to [16, -7, 16]
            box(0.0, -11.0, 0.0, 16.0, -7.0, 16.0),

            // Third shape from [0.5, -11.75, 6.5] to [15.5, -7.75, 9.5]
            box(0.5, -11.75, 6.5, 15.5, -6.75, 9.5),

            // Fourth shape from [6.5, -11.75, 0.5] to [9.5, -7.75, 15.5]
            box(6.5, -11.75, 0.5, 9.5, -7.75, 15.5),

            // Fifth shape from [1.0, -13.0, 1.0] to [15.0, -10.0, 15.0]
            box(1.0, -13.0, 1.0, 15.0, -10.0, 15.0),

            // Sixth shape from [3.0, 1.0, 3.0] to [13.0, 9.0, 13.0]
            box(3.0, 1.0, 3.0, 13.0, 9.0, 13.0),

            // Seventh shape from [2.0, -7.0, 2.0] to [14.0, 1.0, 14.0]
            box(2.0, -7.0, 2.0, 14.0, 1.0, 14.0),

            // Eighth shape from [4.0, 9.0, 4.0] to [12.0, 16.0, 12.0]
            box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0)
    );


    public UpperAuroraPiller(Properties p_49092_) {
        super(p_49092_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(WATERLOGGED);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            pLevel.removeBlock(pPos.below(), false);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.AURORA_PILLER.get());
    }
}
