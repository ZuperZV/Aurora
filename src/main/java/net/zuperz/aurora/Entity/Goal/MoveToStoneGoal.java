package net.zuperz.aurora.Entity.Goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperz.aurora.Entity.custom.GoblinMinerEntity;
import net.zuperz.aurora.util.ModTags;

import java.util.EnumSet;

public class MoveToStoneGoal extends Goal {
    private final GoblinMinerEntity goblin;
    private BlockPos targetStonePos;

    public MoveToStoneGoal(GoblinMinerEntity goblin) {
        this.goblin = goblin;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return goblin.hasPickaxe && (findNearbyStone() != null);
    }

    @Override
    public void tick() {
        if (goblin.moveToStoneProgress >= goblin.moveToStoneTime) {

            if (targetStonePos != null) {
                goblin.getNavigation().moveTo(targetStonePos.getX(), targetStonePos.getY(), targetStonePos.getZ(), 1.0D);
            }

            goblin.moveToStoneProgress = 0;
        } else {
            goblin.moveToStoneProgress++;
        }
    }

    private BlockPos findNearbyStone() {
        BlockPos currentPos = goblin.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(currentPos.offset(-10, -3, -10), currentPos.offset(10, 3, 10))) {
            if (goblin.level().getBlockState(pos).is(ModTags.Blocks.GOBLIN_MINEABLE_STONE)) {
                targetStonePos = pos;
                return pos;
            }
        }
        return null;
    }
}