package net.zuperz.aurora.Entity.Goal;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperz.aurora.Entity.custom.GoblinMinerEntity;
import net.zuperz.aurora.util.ModTags;

import java.util.EnumSet;

public class MineStoneGoal extends Goal {
    public final GoblinMinerEntity goblin;
    private BlockPos targetBlockPos;

    public MineStoneGoal(GoblinMinerEntity goblin) {
        this.goblin = goblin;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        targetBlockPos = findStoneBlock();
        ItemStack storedPickaxe = goblin.inventory.getStackInSlot(0);
        return !storedPickaxe.isEmpty() && targetBlockPos != null;
    }

    @Override
    public void tick() {
        if (goblin.miningProgress >= goblin.miningTime) {
            BlockState state = goblin.level().getBlockState(targetBlockPos);
            goblin.level().destroyBlock(targetBlockPos, false); // Mine the block
            goblin.inventory.insertItem(0, new ItemStack(state.getBlock()), false); // Add block to inventory
            goblin.miningProgress = 0; // Reset progress after mining
        } else {
            goblin.miningProgress++; // Increment mining progress
        }
    }

    private BlockPos findStoneBlock() {
        BlockPos currentPos = goblin.blockPosition();

        BlockPos[] adjacentPositions = {
                currentPos.above(),
                currentPos.north(),
                currentPos.south(),
                currentPos.east(),
                currentPos.west(),
                currentPos.above().above(),
                currentPos.north().above(),
                currentPos.south().above(),
                currentPos.east().above(),
                currentPos.west().above(),

                currentPos.west().north(),
                currentPos.north().east(),
                currentPos.east().south(),
                currentPos.south().west(),
                currentPos.west().north().above(),
                currentPos.north().east().above(),
                currentPos.east().south().above(),
                currentPos.south().west().above(),
        };

        for (BlockPos pos : adjacentPositions) {
            if (goblin.level().getBlockState(pos).is(ModTags.Blocks.GOBLIN_MINEABLE_STONE)) {
                return pos;
            }
        }
        return null;
    }
}