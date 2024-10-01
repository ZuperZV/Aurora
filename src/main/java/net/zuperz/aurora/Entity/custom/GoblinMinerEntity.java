package net.zuperz.aurora.Entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zuperz.aurora.Entity.Goal.MineStoneGoal;
import net.zuperz.aurora.Entity.Goal.MoveToStoneGoal;
import net.zuperz.aurora.Entity.Goal.VoidWalkerAttackGoal;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoblinMinerEntity extends Animal {
    public boolean hasPickaxe = false;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final ItemStackHandler inventory = new ItemStackHandler(1);
    public int miningProgress = 0;
    public final int miningTime = 20;
    public int moveToStoneProgress = 0;
    public final int moveToStoneTime = 40;
    private BlockPos centerBlock;
    private static final int MAX_DISTANCE = 10;

    public GoblinMinerEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MineStoneGoal(this));
        this.goalSelector.addGoal(3, new MoveToStoneGoal(this));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));

        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25, stack -> stack.is(Items.IRON_PICKAXE), false));

        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public void setCenterBlock(BlockPos center) {
        this.centerBlock = center;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.centerBlock != null) {
            double horizontalDistance = Math.sqrt(
                    Math.pow(this.blockPosition().getX() - this.centerBlock.getX(), 2) +
                            Math.pow(this.blockPosition().getZ() - this.centerBlock.getZ(), 2)
            );

            if (horizontalDistance > MAX_DISTANCE) {
                this.getNavigation().moveTo(centerBlock.getX(), this.getY(), centerBlock.getZ(), 1.0D);
                return;
            }
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }

        if (!this.level().isClientSide()) {
            List<ItemEntity> items = this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(3.0D));

            for (ItemEntity itemEntity : items) {
                ItemStack itemStack = itemEntity.getItem();

                if (itemStack.is(ItemTags.PICKAXES) || itemStack.is(ModTags.Items.GOBLIN_MINEABLE_STONE)) {
                    this.hasPickaxe = true;
                    itemEntity.discard();
                    this.inventory.insertItem(0, itemStack.copy(), false);
                    break;
                }
            }
        }
    }

    @Override
    public boolean canBeLeashed() {
        return super.canBeLeashed();
    }

    /* SOUNDS */

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ALLAY_AMBIENT_WITH_ITEM;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ARMADILLO_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean isFood(ItemStack p_27600_) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100D)
                .add(Attributes.MOVEMENT_SPEED, 0.15F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(Attributes.FOLLOW_RANGE, 44.0);
    }

    private void setupAnimationStates() {
        if(this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 39;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }
}
