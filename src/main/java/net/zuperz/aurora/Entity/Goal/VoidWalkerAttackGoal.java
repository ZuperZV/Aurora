package net.zuperz.aurora.Entity.Goal;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.zuperz.aurora.Entity.custom.VoidWalkerEntity;

public class VoidWalkerAttackGoal extends MeleeAttackGoal {
    private final VoidWalkerEntity zombie;
    private int raiseArmTicks;

    public VoidWalkerAttackGoal(VoidWalkerEntity p_26019_, double p_26020_, boolean p_26021_) {
        super(p_26019_, p_26020_, p_26021_);
        this.zombie = p_26019_;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            this.zombie.setAggressive(true);
        } else {
            this.zombie.setAggressive(false);
        }
    }
}
