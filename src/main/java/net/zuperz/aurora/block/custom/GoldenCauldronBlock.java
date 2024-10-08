package net.zuperz.aurora.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.block.entity.custom.AuroraPedestalBlockEntity;
import net.zuperz.aurora.block.entity.custom.GoldenCauldronBlockEntity;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class GoldenCauldronBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    public static final MapCodec<GoldenCauldronBlock> CODEC = simpleCodec(GoldenCauldronBlock::new);
    protected static final VoxelShape SHAPE;


    public GoldenCauldronBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    static {
        SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), new VoxelShape[]{box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE}), BooleanOp.ONLY_FIRST);
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
        return new GoldenCauldronBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                            BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if(pLevel.getBlockEntity(pPos) instanceof GoldenCauldronBlockEntity AoldenCauldronBlockEntity) {
                Containers.dropContents(pLevel, pPos, AoldenCauldronBlockEntity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof GoldenCauldronBlockEntity goldenCauldronBlockEntity) {
            if (goldenCauldronBlockEntity.isEmpty()) {
                if (!pStack.isEmpty()) {
                    ItemStack itemToStore = pStack.copy();
                    itemToStore.setCount(1);
                    goldenCauldronBlockEntity.setItem(0, itemToStore);
                    pStack.shrink(1);
                    pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                }
            } else if(pStack.isEmpty()) {
                ItemStack stackOnPedestal = goldenCauldronBlockEntity.getItem(0);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                goldenCauldronBlockEntity.clearContent();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.GOLDEN_CAULDRON_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> {
            if (pBlockEntity instanceof GoldenCauldronBlockEntity) {
                GoldenCauldronBlockEntity.tick(pLevel1, pPos, pState1, pBlockEntity);
            }
        });
    }

    public static void clearPedestalItems(Level level, BlockPos pos) {
        for (BlockPos pedestalPos : getPedestalPositions(pos)) {
            BlockEntity entity = level.getBlockEntity(pedestalPos);
            if (entity instanceof AuroraPedestalBlockEntity pedestal) {
                pedestal.clearContent();
                level.sendBlockUpdated(pedestalPos, pedestal.getBlockState(), pedestal.getBlockState(), 3);
            }
        }
    }

    private static BlockPos[] getPedestalPositions(BlockPos cauldronPos) {
        return new BlockPos[]{
                cauldronPos.offset(2, 0, 0),
                cauldronPos.offset(-2, 0, 0),
                cauldronPos.offset(0, 0, 2),
                cauldronPos.offset(0, 0, -2)
        };
    }
}