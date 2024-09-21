package net.zuperz.aurora.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;
import net.zuperz.aurora.block.entity.custom.ArcanePowerTableBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Alter extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);
    public Alter(Properties properties) {
        super(Properties.of());
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
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @javax.annotation.Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlterBlockEntity(pos, state);
    }

    @Override
    public @javax.annotation.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof AlterBlockEntity tile) {
                tile.tick();
            }
        };
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof AlterBlockEntity alterBE) {
            if (Alter.arePedestalPositionsClayWire(pLevel, pPos) || Alter.arePedestalPositionsAuroraWire(pLevel, pPos)) {

                ItemStack singleStack = pStack.copy();
                singleStack.setCount(1);

                if (!pStack.isEmpty()) {
                    for (int i = 0; i < 5; i++) {
                        if (alterBE.isInputEmpty(i)) {
                            alterBE.setItem(i, singleStack);
                            pStack.shrink(1);
                            pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                } else {
                    if (!alterBE.isOutputEmpty(0)) {
                        ItemStack stackOnPedestal = alterBE.getItem(5);
                        pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                        alterBE.clearOutput(0);
                        pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                        return ItemInteractionResult.SUCCESS;
                    }

                    for (int i = 0; i < 5; i++) {
                        if (!alterBE.isInputEmpty(i)) {
                            ItemStack stackOnPedestal = alterBE.getItem(i);
                            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                            alterBE.clearInput(i);
                            pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AlterBlockEntity furnace) {
                furnace.dropItems();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    private static BlockPos[] getWirePositions(BlockPos pos) {
        return new BlockPos[]{
                pos.offset(1, 0, -2),
                pos.offset(0, 0, -2),
                pos.offset(-1, 0, -2),
                pos.offset(2, 0, -1),
                pos.offset(-2, 0, -1),
                pos.offset(2, 0, 0),
                pos.offset(-2, 0, 0),
                pos.offset(2, 0, 1),
                pos.offset(-2, 0, 1),
                pos.offset(1, 0, 2),
                pos.offset(0, 0, 2),
                pos.offset(-1, 0, 2),
        };
    }

    private static BlockPos[] getBeamPositions(BlockPos pos) {
        return new BlockPos[]{
                pos.offset(2, 0, -2),
                pos.offset(-2, 0, -2),
                pos.offset(2, 0, 2),
                pos.offset(-2, 0, 2),
        };
    }

    public static boolean arePedestalPositionsAuroraWire(Level pLevel, BlockPos pos) {
        BlockPos[] wirePositions = getWirePositions(pos);
        BlockPos[] beamPositions = getBeamPositions(pos);

        for (BlockPos wirePos : wirePositions) {
            if (!pLevel.getBlockState(wirePos).is(ModBlocks.AURORA_WIRE)) {
                return false;
            }
        }
        for (BlockPos beamPos : beamPositions) {
            if (!pLevel.getBlockState(beamPos).is(ModBlocks.BEAM)) {
                return false;
            }
        }
        return true;
    }

    public static boolean arePedestalPositionsClayWire(Level pLevel, BlockPos pos) {
        BlockPos[] wirePositions = getWirePositions(pos);
        BlockPos[] beamPositions = getBeamPositions(pos);

        for (BlockPos wirePos : wirePositions) {
            if (!pLevel.getBlockState(wirePos).is(ModBlocks.CLAY_WIRE)) {
                return false;
            }
        }
        for (BlockPos beamPos : beamPositions) {
            if (!pLevel.getBlockState(beamPos).is(ModBlocks.BEAM)) {
                return false;
            }
        }
        return true;
    }
}