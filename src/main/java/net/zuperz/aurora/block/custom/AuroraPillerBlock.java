package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class AuroraPillerBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    VoxelShape SHAPE = Shapes.or(
            // First shape from [0, 0, 0] to [16, 3, 16]
            box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),

            // Second shape from [0, 6, 0] to [16, 9, 16]
            box(0.0, 6.0, 0.0, 16.0, 9.0, 16.0),

            // Third shape from [0.5, 4.25, 6.5] to [15.5, 8.25, 9.5]
            box(0.5, 4.25, 6.5, 15.5, 8.25, 9.5),

            // Fourth shape from [6.5, 4.25, 0.5] to [9.5, 8.25, 15.5]
            box(6.5, 4.25, 0.5, 9.5, 8.25, 15.5),

            // Fifth shape from [1, 3, 1] to [15, 6, 15]
            box(1.0, 3.0, 1.0, 15.0, 6.0, 15.0),

            // Sixth shape from [3, 17, 3] to [13, 25, 13]
            box(3.0, 17.0, 3.0, 13.0, 25.0, 13.0),

            // Seventh shape from [2, 9, 2] to [14, 17, 14]
            box(2.0, 9.0, 2.0, 14.0, 17.0, 14.0),

            // Eighth shape from [4, 25, 4] to [12, 32, 12]
            box(4.0, 25.0, 4.0, 12.0, 32.0, 12.0)
    );

    public AuroraPillerBlock(Properties pProperties) {
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

        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(pContext)) {
            return this.defaultBlockState()
                    .setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pLevel == null || pState == null) {
            return; // Early exit if level or state is null
        }

        BlockState upperAuroraPillerState = ModBlocks.UPPER_AURORA_PILLER.get().defaultBlockState();

        if (upperAuroraPillerState.hasProperty(FACING)) {
            upperAuroraPillerState = upperAuroraPillerState.setValue(FACING, pState.getValue(FACING));
            pLevel.setBlock(pPos.above(), upperAuroraPillerState, 3);
        } else {
            // Log or handle the case where FACING property is not available
            System.err.println("FACING property is not available on UPPER_AURORA_PILLER");
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
                // Remove the upper part if the lower part is removed
                pLevel.removeBlock(pPos.above(), false);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
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
}
