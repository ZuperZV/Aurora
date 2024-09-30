package net.zuperz.aurora.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.ArcanePedestalBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArcanePedestalBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    static VoxelShape SHAPE = Shapes.or(
            box(1, 13, 1, 15, 16, 15),

            box(3, 2, 3, 13, 13, 13),

            box(2, 12, 2, 14, 14, 14),

            box(2, 1, 2, 14, 3, 14),

            box(1, 0, 1, 15, 1, 15)
    );

    public static final MapCodec<ArcanePedestalBlock> CODEC = simpleCodec(ArcanePedestalBlock::new);
    public static final List<BlockPos> WIRE_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2)
            .filter(p_341357_ -> Math.abs(p_341357_.getX()) == 2 || Math.abs(p_341357_.getZ()) == 2)
            .map(BlockPos::immutable)
            .toList();
    public static final BooleanProperty LIT = BlockStateProperties.LIT;


    public ArcanePedestalBlock(Properties pProperties) {
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
        return new ArcanePedestalBlockEntity(pPos, pState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof ArcanePedestalBlockEntity furnace) {
                furnace.dropItems();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.getBlockEntity(pPos) instanceof ArcanePedestalBlockEntity alterBE) {

            ItemStack singleStack = pStack.copy();
            singleStack.setCount(1);

            if (!pStack.isEmpty()) {
                for (int i = 0; i < 1; i++) {
                    if (alterBE.isInputEmpty(0)) {
                        alterBE.setItem(0, singleStack);
                        pStack.shrink(1);
                        pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
            if (!alterBE.isInputEmpty(0)) {
                ItemStack stackOnPedestal = alterBE.getItem(0);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                alterBE.clearInput(0);
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @javax.annotation.Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;

        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ArcanePedestalBlockEntity tile) {
                tile.tick();
            }
        };
    }

    private static BlockPos[] getBeamPositions(BlockPos pos) {
        return new BlockPos[]{
                pos.offset(2, 0, 0),
                pos.offset(-2, 0, 0),
        };
    }

    private static BlockPos[] getBeamPositions2(BlockPos pos) {
        return new BlockPos[]{
                pos.offset(0, 0, -2),
                pos.offset(0, 0, -2),
        };
    }

    public static boolean areBeamPositionsValid(Level pLevel, BlockPos pos) {
        BlockPos[] dirtPositions = getBeamPositions(pos);

        for (BlockPos dirtPos : dirtPositions) {
            BlockState blockState = pLevel.getBlockState(dirtPos);

            if (!blockState.is(ModBlocks.BEAM)) {
                return false;
            }
        }

        return true;
    }

    public static boolean areBeamPositionsValid2(Level pLevel, BlockPos pos) {
        BlockPos[] dirtPositions2 = getBeamPositions2(pos);

        for (BlockPos dirtPos : dirtPositions2) {
            BlockState blockState = pLevel.getBlockState(dirtPos);

            if (!blockState.is(ModBlocks.BEAM)) {
                return false;
            }
        }

        return true;
    }

    public static void spawnParticles(Level level, BlockPos pos) {
        if (level != null && !level.isClientSide) {
            for (int i = 0; i < 5; i++) { // Change 5 to the number of particles you want
                double x = pos.getX() + 0.5; // Center X of the block
                double y = pos.getY() + 1.5; // 2 blocks up, change as needed
                double z = pos.getZ() + 0.5; // Center Z of the block

                // Randomly adjust the X and Z coordinates for more natural spread
                x += (level.random.nextDouble() - 0.5) * 0.5; // Random offset
                z += (level.random.nextDouble() - 0.5) * 0.5; // Random offset

                level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0.05, 0); // You can replace ENCHANT with your particle type
            }
        }
    }
}