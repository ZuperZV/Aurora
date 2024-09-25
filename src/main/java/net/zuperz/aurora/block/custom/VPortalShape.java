package net.zuperz.aurora.block.custom;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.BlockUtil;
import net.zuperz.aurora.block.ModBlocks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

public class VPortalShape {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final float SAFE_TRAVEL_MAX_ENTITY_XY = 4.0F;
    private static final double SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0;
    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private final int width;

    public static Optional<VPortalShape> findEmptyPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        return findPortalShape(level, pos, shape -> shape.isValid() && shape.numPortalBlocks == 0, axis);
    }

    public static Optional<VPortalShape> findPortalShape(LevelAccessor level, BlockPos pos, Predicate<VPortalShape> predicate, Direction.Axis axis) {
        Optional<VPortalShape> optional = Optional.of(new VPortalShape(level, pos, axis)).filter(predicate);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis altAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(new VPortalShape(level, pos, altAxis)).filter(predicate);
        }
    }

    public VPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        this.level = level;
        this.axis = axis;
        this.rightDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.bottomLeft = calculateBottomLeft(pos);
        if (this.bottomLeft == null) {
            this.bottomLeft = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = calculateWidth();
            if (this.width > 0) {
                this.height = calculateHeight();
            }
        }
    }

    @Nullable
    private BlockPos calculateBottomLeft(BlockPos pos) {
        int minY = Math.max(this.level.getMinBuildHeight(), pos.getY() - 21);
        while (pos.getY() > minY && isEmpty(this.level.getBlockState(pos.below()))) {
            pos = pos.below();
        }
        Direction direction = this.rightDir.getOpposite();
        int distance = getDistanceUntilEdgeAboveFrame(pos, direction) - 1;
        return distance < 0 ? null : pos.relative(direction, distance);
    }

    private int calculateWidth() {
        int width = getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        return width >= 2 && width <= 21 ? width : 0;
    }

    private int getDistanceUntilEdgeAboveFrame(BlockPos pos, Direction direction) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int i = 0; i <= 21; i++) {
            mutablePos.set(pos).move(direction, i);
            BlockState blockState = this.level.getBlockState(mutablePos);
            if (!isEmpty(blockState)) {
                break;
            }
        }
        return 0;
    }

    private int calculateHeight() {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int height = getDistanceUntilTop(mutablePos);
        return height >= 3 && height <= 21 ? height : 0;
    }

    private int getDistanceUntilTop(BlockPos.MutableBlockPos pos) {
        for (int i = 0; i < 21; i++) {
            pos.set(this.bottomLeft).move(Direction.UP, i);
            if (!isEmpty(this.level.getBlockState(pos))) {
                return i;
            }
            for (int j = 0; j < this.width; j++) {
                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, j);
                BlockState blockState = this.level.getBlockState(pos);
                if (!isEmpty(blockState)) {
                    return i;
                }
                // Only keep track of RIFT_BLOCK here, removing other usages
                if (blockState.getBlock() == ModBlocks.RIFT_BLOCK.get()) {
                    this.numPortalBlocks++;
                }
            }
        }
        return 21;
    }

    private static boolean isEmpty(BlockState state) {
        return state.isAir();  // Only check for air here, no need for RIFT_BLOCK
    }

    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    // Keep the single occurrence of RIFT_BLOCK here
    public void createPortalBlock() {
        BlockState blockState = ModBlocks.RIFT_BLOCK.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, this.axis);
        BlockPos portalBlockPos = this.bottomLeft.above(1);
        this.level.setBlock(portalBlockPos, blockState, 18);
    }

    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }

    public static Vec3 getRelativePosition(BlockUtil.FoundRectangle rectangle, Direction.Axis axis, Vec3 position, EntityDimensions dimensions) {
        double deltaX = (double) rectangle.axis1Size - (double) dimensions.width();
        double deltaY = (double) rectangle.axis2Size - (double) dimensions.height();
        BlockPos cornerPos = rectangle.minCorner;
        double relativeX = deltaX > 0.0 ? Mth.clamp(Mth.inverseLerp(position.get(axis) - (double) cornerPos.get(axis), 0.0, deltaX), 0.0, 1.0) : 0.5;
        double relativeY = deltaY > 0.0 ? Mth.clamp(Mth.inverseLerp(position.get(Direction.Axis.Y) - (double) cornerPos.get(Direction.Axis.Y), 0.0, deltaY), 0.0, 1.0) : 0.0;
        Direction.Axis perpendicularAxis = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double relativeZ = position.get(perpendicularAxis) - ((double) cornerPos.get(perpendicularAxis) + 0.5);
        return new Vec3(relativeX, relativeY, relativeZ);
    }

    public static Vec3 findCollisionFreePosition(Vec3 position, ServerLevel level, Entity entity, EntityDimensions dimensions) {
        if (dimensions.width() <= 4.0F && dimensions.height() <= 4.0F) {
            double halfHeight = (double) dimensions.height() / 2.0;
            Vec3 adjustedPos = position.add(0.0, halfHeight, 0.0);
            AABB aabb = AABB.ofSize(adjustedPos, (double) dimensions.width(), 0.0, (double) dimensions.width()).expandTowards(0.0, 1.0, 0.0).inflate(1.0E-6);
            Optional<Vec3> freePosition = level.findFreePosition(entity, Shapes.create(aabb), adjustedPos, (double) dimensions.width(), (double) dimensions.height(), (double) dimensions.width());
            return freePosition.map(p -> p.subtract(0.0, halfHeight, 0.0)).orElse(position);
        } else {
            return position;
        }
    }
}