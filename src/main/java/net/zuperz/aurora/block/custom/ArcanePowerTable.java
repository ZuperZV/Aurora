package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack itemStack) {
        if (level == null || pState == null) {
            return;
        }

        BlockState sideBlockState = ModBlocks.SIDE_ARCANE_POWER_TABLE.get().defaultBlockState();

        if (sideBlockState.hasProperty(FACING)) {
            Direction facing = pState.getValue(FACING);

            BlockPos leftPos = pos.relative(facing.getCounterClockWise());
            BlockState leftBlockState = sideBlockState.setValue(FACING, facing.getOpposite());
            level.setBlock(leftPos, leftBlockState, 3);

            BlockPos rightPos = pos.relative(facing.getClockWise());
            BlockState rightBlockState = sideBlockState.setValue(FACING, facing);
            level.setBlock(rightPos, rightBlockState, 3);

        } else {
            System.err.println("FACING property is not available on SIDE_ARCANE_POWER_TABLE");
        }
    }

    @Override
    public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {

            if (pState.getValue(ArcanePowerTable.FACING) == Direction.SOUTH) {
                BlockPos westPos = pos.offset(-1, 0, 0);
                BlockPos eastPos = pos.offset(1, 0, 0);
                level.removeBlock(westPos, false);
                level.removeBlock(eastPos, false);
            }

            if (pState.getValue(ArcanePowerTable.FACING) == Direction.NORTH) {
                BlockPos westPos = pos.offset(-1, 0, 0);
                BlockPos eastPos = pos.offset(1, 0, 0);
                level.removeBlock(westPos, false);
                level.removeBlock(eastPos, false);
            }

            if (pState.getValue(ArcanePowerTable.FACING) == Direction.EAST) {
                BlockPos westPos = pos.offset(0, 0, -1);
                BlockPos eastPos = pos.offset(0, 0, 1);
                level.removeBlock(westPos, false);
                level.removeBlock(eastPos, false);
            }

            if (pState.getValue(ArcanePowerTable.FACING) == Direction.WEST) {
                BlockPos westPos = pos.offset(0, 0, -1);
                BlockPos eastPos = pos.offset(0, 0, 1);
                level.removeBlock(westPos, false);
                level.removeBlock(eastPos, false);
            }

            level.removeBlock(pos.above(), false);
        }

        if (pState.getBlock() != pNewState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ArcanePowerTableBlockEntity furnace) {
                furnace.dropItems();
            }
        }

        super.onRemove(pState, level, pos, pNewState, pIsMoving);
    }

    private boolean isPartOfMultiBlock(BlockState targetState, BlockState originalState, Level level, BlockPos targetPos) {
        if (targetState.getBlock() == ModBlocks.ARCANE_POWER_TABLE.get() ||
                targetState.getBlock() == ModBlocks.SIDE_ARCANE_POWER_TABLE.get()) {

            Direction targetFacing = targetState.getValue(ArcanePowerTable.FACING);
            Direction originalFacing = originalState.getValue(ArcanePowerTable.FACING);

            return targetFacing == originalFacing;
        }

        return false;
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

        if (level.getBlockEntity(pos) instanceof ArcanePowerTableBlockEntity alterBE) {

            ItemStack singleStack = itemStack.copy();
            singleStack.setCount(1);

            if (!itemStack.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    if (alterBE.isInputEmpty(i)) {
                        alterBE.setItem(i, singleStack);
                        itemStack.shrink(1);
                        level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            } else {
                if (!alterBE.isOutputEmpty(0)) {
                    ItemStack stackOnPedestal = alterBE.getItem(3);
                    player.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                    alterBE.clearOutput(0);
                    level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    return ItemInteractionResult.SUCCESS;
                }

                for (int i = 0; i < 3; i++) {
                    if (!alterBE.isInputEmpty(i)) {
                        ItemStack stackOnPedestal = alterBE.getItem(i);
                        player.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                        alterBE.clearInput(i);
                        level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }

            if (!level.isClientSide) {
                ArcanePowerTableBlockEntity blockEntity = (ArcanePowerTableBlockEntity) level.getBlockEntity(pos);
                if (blockEntity != null) {
                    player.displayClientMessage(
                            Component.literal("Arcane Power: " + blockEntity.getEnergyStored() + " / " + blockEntity.getMaxEnergyStored()),
                            true
                    );
                }
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
}
