package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.commands.arguments.blocks.BlockStateArgument.getBlock;

public class SideArcanePowerTable extends BarrierBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    // Base VoxelShape for the default direction (e.g. NORTH)
    private static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(-1, 13, 3, 4, 16, 14),
            box(4, 13, 1, 15, 16, 15),
            box(5, 12, 2, 14, 14, 14),
            box(6, 1, 3, 13, 12, 13),
            box(5, 0, 2, 14, 2, 14),
            box(-20, 13, 3, -15, 16, 14),
            box(-31, 13, 1, -20, 16, 15),
            box(-30, 12, 2, -21, 14, 14),
            box(-29, 1, 3, -22, 12, 13),
            box(-30, 0, 2, -21, 2, 14),
            box(-15, 13, 1, -1, 16, 15),
            box(-13, 2, 3, -3, 13, 13),
            box(-14, 12, 2, -2, 14, 14),
            box(-14, 1, 2, -2, 3, 14),
            box(-15, 0, 1, -1, 1, 15)
    );
    private static final VoxelShape SHAPE_EAST = rotateShape(Direction.NORTH, Direction.EAST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = rotateShape(Direction.NORTH, Direction.WEST, SHAPE_NORTH);

    public SideArcanePowerTable(Properties p_49092_) {
        super(p_49092_);
    }

    @Override
    protected VoxelShape getShape(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        switch ((Direction)p_54561_.getValue(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_NORTH;
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.ARCANE_POWER_TABLE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);

        int radius = 1;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = pPos.offset(x, y, z);
                    BlockState targetState = pLevel.getBlockState(targetPos);

                    if (targetState.is(ModBlocks.ARCANE_POWER_TABLE) || targetState.is(ModBlocks.SIDE_ARCANE_POWER_TABLE)) {
                        pLevel.removeBlock(targetPos, false);
                    }
                }
            }
        }
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }
}
