package net.zuperz.aurora.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.zuperz.aurora.block.custom.TwigableBlock;
import net.zuperz.aurora.item.ModItems;

public class MaterialeTwigItem extends Item {
    public static final int ANIMATION_DURATION = 10;
    private static final int USE_DURATION = 200;

    public MaterialeTwigItem(Properties p_272907_) {
        super(p_272907_);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_272607_) {
        Player player = p_272607_.getPlayer();
        Level level = p_272607_.getLevel();
        BlockPos pos = p_272607_.getClickedPos();
        BlockState blockState = level.getBlockState(pos);

        if (blockState.getBlock() instanceof TwigableBlock) {
            if (player != null) {
                player.startUsingItem(p_272607_.getHand());
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_273490_) {
        return UseAnim.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack p_272765_, LivingEntity p_344739_) {
        return USE_DURATION;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingUseTicks) {
        if (entity instanceof Player player) {
            HitResult hitResult = this.calculateHitResult(player);
            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);

                if (blockState.getBlock() instanceof TwigableBlock twigableBlock) {
                    int usedTicks = this.getUseDuration(itemStack, entity) - remainingUseTicks;

                    level.playSound(player, blockPos, twigableBlock.getBrushSound(), SoundSource.BLOCKS);

                    if (usedTicks >= 30) {
                        if (!level.isClientSide) {
                            ItemStack air = new ItemStack(Items.AIR);
                            player.setItemInHand(player.getUsedItemHand(), air);
                            ItemStack skull_twig = new ItemStack(ModItems.STONE_SKULL.get());
                            ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), skull_twig);
                            level.addFreshEntity(itemEntity);
                            level.setBlock(blockPos, Blocks.STONE.defaultBlockState(), 3);
                        }
                        player.releaseUsingItem();
                    }

                }
            }
        }
    }

    private HitResult calculateHitResult(Player player) {
        return player.pick(player.blockInteractionRange(), 0.0F, false);
    }
}
