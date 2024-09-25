package net.zuperz.aurora.block.custom;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.BlockUtil;
import net.zuperz.aurora.block.ModBlocks;

import java.util.Comparator;
import java.util.Optional;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class VTeleporter {
    public static Holder<PoiType> poi = null;

    @SubscribeEvent
    public static void registerPointOfInterest(RegisterEvent event) {
        event.register(Registries.POINT_OF_INTEREST_TYPE, registerHelper -> {
            PoiType poiType = new PoiType(ImmutableSet.copyOf(ModBlocks.RIFT_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1);
            registerHelper.register(ResourceLocation.parse("aurora:rift_portal"), poiType);
            poi = BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(poiType);
        });
    }

    private final ServerLevel level;

    public VTeleporter(ServerLevel level) {
        this.level = level;
    }

    public Optional<BlockPos> findClosestPortalPosition(BlockPos p_352378_, boolean p_352309_, WorldBorder p_352374_) {
        PoiManager poimanager = this.level.getPoiManager();
        int i = p_352309_ ? 16 : 128;
        poimanager.ensureLoadedAndValid(this.level, p_352378_, i);
        return poimanager.getInSquare(p_230634_ -> p_230634_.is(poi.unwrapKey().get()), p_352378_, i, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).filter(p_352374_::isWithinBounds)
                .filter(p_352047_ -> this.level.getBlockState(p_352047_).hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).min(Comparator.<BlockPos>comparingDouble(p_352046_ -> p_352046_.distSqr(p_352378_)).thenComparingInt(Vec3i::getY));
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double closestDistance = -1.0;
        BlockPos bestPos = null;
        double backupDistance = -1.0;
        BlockPos backupPos = null;
        WorldBorder worldBorder = this.level.getWorldBorder();
        int maxY = Math.min(this.level.getMaxBuildHeight(), this.level.getMinBuildHeight() + this.level.getLogicalHeight()) - 1;
        BlockPos.MutableBlockPos mutablePos = pos.mutable();

        // Search for suitable portal position
        for (BlockPos.MutableBlockPos currentPos : BlockPos.spiralAround(pos, 16, Direction.EAST, Direction.SOUTH)) {
            int height = Math.min(maxY, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, currentPos.getX(), currentPos.getZ()));
            if (worldBorder.isWithinBounds(currentPos) && worldBorder.isWithinBounds(currentPos.move(direction, 1))) {
                currentPos.move(direction.getOpposite(), 1);
                for (int y = height; y >= this.level.getMinBuildHeight(); y--) {
                    currentPos.setY(y);
                    if (this.canPortalReplaceBlock(currentPos)) {
                        int potentialHeight = y;
                        while (y > this.level.getMinBuildHeight() && this.canPortalReplaceBlock(currentPos.move(Direction.DOWN))) {
                            y--;
                        }
                        if (y + 1 <= maxY) {  // Adjusted for 1x1 portal
                            int verticalSize = potentialHeight - y;
                            if (verticalSize >= 1) {
                                currentPos.setY(y);
                                if (this.canHostFrame(currentPos, mutablePos, direction, 0)) {
                                    double distance = pos.distSqr(currentPos);
                                    if (this.canHostFrame(currentPos, mutablePos, direction, -1) && this.canHostFrame(currentPos, mutablePos, direction, 1) && (closestDistance == -1.0 || closestDistance > distance)) {
                                        closestDistance = distance;
                                        bestPos = currentPos.immutable();
                                    }
                                    if (closestDistance == -1.0 && (backupDistance == -1.0 || backupDistance > distance)) {
                                        backupDistance = distance;
                                        backupPos = currentPos.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Set the best position or fallback position
        if (closestDistance == -1.0 && backupDistance != -1.0) {
            bestPos = backupPos;
            closestDistance = backupDistance;
        }

        // If no portal location found, place manually
        if (closestDistance == -1.0) {
            int minHeight = Math.max(this.level.getMinBuildHeight(), 70);
            int availableHeight = maxY - 9;
            bestPos = new BlockPos(pos.getX() - direction.getStepX() * 1, Mth.clamp(pos.getY(), minHeight, availableHeight), pos.getZ() - direction.getStepZ() * 1).immutable();
            bestPos = worldBorder.clampToBounds(bestPos);
        }

        // Create the portal frame and the portal itself (1x1)
        BlockState portalBlockState = ModBlocks.RIFT_BLOCK.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        mutablePos.setWithOffset(bestPos, 0, 1, 0);  // Only 1 block
        this.level.setBlock(mutablePos, portalBlockState, 18);
        this.level.getPoiManager().add(mutablePos, poi);

        return Optional.of(new BlockUtil.FoundRectangle(bestPos.immutable(), 1, 1));  // Return 1x1 rectangle
    }

    private boolean canHostFrame(BlockPos pos, BlockPos.MutableBlockPos mutablePos, Direction direction, int offset) {
        Direction perpendicular = direction.getClockWise();
        for (int x = -1; x < 3; x++) {
            for (int y = -1; y < 4; y++) {
                mutablePos.setWithOffset(pos, direction.getStepX() * x + perpendicular.getStepX() * offset, y, direction.getStepZ() * x + perpendicular.getStepZ() * offset);
                if (y < 0 && !this.level.getBlockState(mutablePos).isSolid()) {
                    return false;
                }
                if (y >= 0 && !this.canPortalReplaceBlock(mutablePos)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
        BlockState blockState = this.level.getBlockState(pos);
        return blockState.canBeReplaced() && blockState.getFluidState().isEmpty();
    }
}


/*
public Optional<BlockPos> findClosestPortalPosition(BlockPos p_352378_, boolean p_352309_, WorldBorder p_352374_) {
        PoiManager poimanager = this.level.getPoiManager();
        int i = p_352309_ ? 16 : 128;
        poimanager.ensureLoadedAndValid(this.level, p_352378_, i);
        return poimanager.getInSquare(p_230634_ -> p_230634_.is(poi.unwrapKey().get()), p_352378_, i, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).filter(p_352374_::isWithinBounds)
                .filter(p_352047_ -> this.level.getBlockState(p_352047_).hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).min(Comparator.<BlockPos>comparingDouble(p_352046_ -> p_352046_.distSqr(p_352378_)).thenComparingInt(Vec3i::getY));
    }
 */