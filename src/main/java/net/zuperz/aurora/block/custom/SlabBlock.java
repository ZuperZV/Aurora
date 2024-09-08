package net.zuperz.aurora.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.custom.AuroraPedestalBlockEntity;
import net.zuperz.aurora.block.entity.custom.PedestalSlabBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class SlabBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 2, 14);
    public static final MapCodec<SlabBlock> CODEC = simpleCodec(SlabBlock::new);
    public static final List<BlockPos> WIRE_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2)
            .filter(p_341357_ -> Math.abs(p_341357_.getX()) == 2 || Math.abs(p_341357_.getZ()) == 2)
            .map(BlockPos::immutable)
            .toList();
    public static final BooleanProperty LIT = BlockStateProperties.LIT;


    public SlabBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

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
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PedestalSlabBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                            BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if(pLevel.getBlockEntity(pPos) instanceof PedestalSlabBlockEntity PedestalSlabBlockEntity) {
                Containers.dropContents(pLevel, pPos, PedestalSlabBlockEntity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof PedestalSlabBlockEntity pedestalSlabBlockEntity) {
            if (pedestalSlabBlockEntity.isEmpty()) {
                if (!pStack.isEmpty()) {
                    ItemStack itemToStore = pStack.copy();
                    itemToStore.setCount(1);
                    pedestalSlabBlockEntity.setItem(0, itemToStore);
                    pStack.shrink(1);
                    pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                }
            } else if(pStack.isEmpty()) {
                ItemStack stackOnPedestal = pedestalSlabBlockEntity.getItem(0);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                pedestalSlabBlockEntity.clearContent();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null; // No ticking needed on the client side
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.SLAB_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> {
                    if (pBlockEntity instanceof PedestalSlabBlockEntity) {
                        PedestalSlabBlockEntity.tick(pLevel1, pPos, (PedestalSlabBlockEntity) pBlockEntity);
                    }
                });
    }

    private static BlockPos[] getPedestalPositions(BlockPos Pos) {
        return new BlockPos[]{
                Pos.offset(2, 0, 0),
                Pos.offset(-2, 0, 0),
                Pos.offset(0, 0, 2),
                Pos.offset(0, 0, -2),

                Pos.offset(2, 0, 1),
                Pos.offset(-2, 0, 1),
                Pos.offset(1, 0, 2),
                Pos.offset(1, 0, -2),

                Pos.offset(2, 0, -1),
                Pos.offset(-2, 0, -1),
                Pos.offset(-1, 0, 2),
                Pos.offset(-1, 0, -2),


                Pos.offset(1, 0, 1),
                Pos.offset(-1, 0, 1),
                Pos.offset(1, 0, 1),
                Pos.offset(1, 0, -1),

                Pos.offset(1, 0, -1),
                Pos.offset(-1, 0, -1),
                Pos.offset(-1, 0, 1),
                Pos.offset(-1, 0, -1),
        };
    }

    public static boolean arePedestalPositionsAuroraWire(Level pLevel, BlockPos Pos) {
        BlockPos[] pedestalPositions = getPedestalPositions(Pos);

        for (BlockPos pos : pedestalPositions) {
            if (!pLevel.getBlockState(pos).is(ModBlocks.AURORA_WIRE)) {
                return false;
            }
        }
        return true;
    }

    public static boolean arePedestalPositionsAuroraWireOrPiller(Level pLevel, BlockPos pos) {
        BlockPos[] pedestalPositions = getPedestalPositions(pos);

        for (BlockPos pedestalPos : pedestalPositions) {
            BlockState blockState = pLevel.getBlockState(pedestalPos);
            if (blockState.is(ModBlocks.AURORA_PILLER)) {
                return true;
            }
        }
        return false;
    }
}