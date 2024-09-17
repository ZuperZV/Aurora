package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.ArcanePowerTableBlockEntity;
import org.jetbrains.annotations.Nullable;

public class ArcanePowerTable extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    static VoxelShape SHAPE = Shapes.or(
            box(15, 13, 3, 20, 16, 14),

            box(20, 13, 1, 31, 16, 15),

            box(21, 12, 2, 30, 14, 14),

            box(22, 1, 3, 29, 12, 13),

            box(21, 0, 2, 30, 2, 14),

            box(-4, 13, 3, 1, 16, 14),

            box(-15, 13, 1, -4, 16, 15),

            box(-14, 12, 2, -5, 14, 14),

            box(-13, 1, 3, -6, 12, 13),

            box(-14, 0, 2, -5, 2, 14),

            box(1, 13, 1, 15, 16, 15),

            box(3, 2, 3, 13, 13, 13),

            box(2, 12, 2, 14, 14, 14),

            box(2, 1, 2, 14, 3, 14),

            box(1, 0, 1, 15, 1, 15)
    );
    private static final VoxelShape SHAPE_EAST = rotateShape(Direction.NORTH, Direction.EAST, SHAPE);
    private static final VoxelShape SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE);
    private static final VoxelShape SHAPE_WEST = rotateShape(Direction.NORTH, Direction.WEST, SHAPE);


    public ArcanePowerTable(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        Level level = pContext.getLevel();

        Direction facing = pContext.getHorizontalDirection().getOpposite();
        if (hasSpaceForPlacement(level, blockpos, facing)) {
            return this.defaultBlockState().setValue(FACING, facing);
        } else {
            return null;
        }
    }

    private boolean hasSpaceForPlacement(Level level, BlockPos pos, Direction facing) {
        for (int i = 1; i <= 3; i++) {
            BlockPos sidePos = pos.relative(facing.getCounterClockWise(), i);
            if (!level.getBlockState(sidePos).isAir()) {
                return false;
            }
        }

        for (int i = 1; i <= 3; i++) {
            BlockPos sidePos = pos.relative(facing.getClockWise(), i);
            if (!level.getBlockState(sidePos).isAir()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pLevel == null || pState == null) {
            return;
        }

        BlockState sideBlockState = ModBlocks.SIDE_ARCANE_POWER_TABLE.get().defaultBlockState();

        if (sideBlockState.hasProperty(FACING)) {
            Direction facing = pState.getValue(FACING);

            BlockPos leftPos = pPos.relative(facing.getCounterClockWise());
            BlockState leftBlockState = sideBlockState.setValue(FACING, facing.getOpposite());
            pLevel.setBlock(leftPos, leftBlockState, 3);

            BlockPos rightPos = pPos.relative(facing.getClockWise());
            BlockState rightBlockState = sideBlockState.setValue(FACING, facing);
            pLevel.setBlock(rightPos, rightBlockState, 3);

        } else {
            System.err.println("FACING property is not available on SIDE_ARCANE_POWER_TABLE");
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {

            pLevel.removeBlock(pPos.above(), false);

            if (pState.getBlock() == ModBlocks.ARCANE_POWER_TABLE.get()) {
                Direction facing = pState.getValue(FACING);

                BlockPos leftPos = pPos.relative(facing.getCounterClockWise());
                BlockState leftState = pLevel.getBlockState(leftPos);
                if (leftState.getBlock() == ModBlocks.SIDE_ARCANE_POWER_TABLE.get()) {
                    pLevel.removeBlock(leftPos, false);
                }

                BlockPos rightPos = pPos.relative(facing.getClockWise());
                BlockState rightState = pLevel.getBlockState(rightPos);
                if (rightState.getBlock() == ModBlocks.SIDE_ARCANE_POWER_TABLE.get()) {
                    pLevel.removeBlock(rightPos, false);
                }
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState p_54561_, BlockGetter p_54562_, BlockPos p_54563_, CollisionContext p_54564_) {
        switch ((Direction)p_54561_.getValue(FACING)) {
            case NORTH:
                return SHAPE;
            case SOUTH:
                return SHAPE_SOUTH;
            case EAST:
                return SHAPE_EAST;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
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

    @Override
    public @javax.annotation.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcanePowerTableBlockEntity(pos, state);
    }

    @Override
    public @javax.annotation.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ArcanePowerTableBlockEntity tile) {
                tile.tick(pos);
            }
        };
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            ArcanePowerTableBlockEntity blockEntity = (ArcanePowerTableBlockEntity) level.getBlockEntity(pos);
            if (blockEntity != null) {
                player.displayClientMessage(
                        Component.literal("Energi: " + blockEntity.getEnergyStored() + " / " + blockEntity.getMaxEnergyStored()),
                        true
                );
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}
