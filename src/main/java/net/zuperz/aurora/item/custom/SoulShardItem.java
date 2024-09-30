package net.zuperz.aurora.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zuperz.aurora.component.ModDataComponentTypes;
import net.zuperz.aurora.component.StarDustData;

import java.util.List;

import static net.zuperz.aurora.aurora.SOUL_SHARD_MAP;

public class SoulShardItem extends Item {
    private final EntityType<?> targetEntityType;

    public SoulShardItem(EntityType<?> entityType, Properties properties) {
        super(properties);
        this.targetEntityType = entityType;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        Level level = player.level();
        //System.out.println("Interacting with: " + livingEntity.getType().getDescriptionId());

        if (!level.isClientSide()) {
            if ((targetEntityType == EntityType.ITEM)) {

                if (itemStack.getItem() instanceof SoulShardItem soulShard && soulShard.targetEntityType == livingEntity.getType()) {
                    //player.sendSystemMessage(Component.translatable("Already captured this entity's soul!"));
                    return InteractionResult.PASS;
                }

                SoulShardItem newSoulShard = SOUL_SHARD_MAP.get(livingEntity.getType());
                if (newSoulShard != null) {
                    player.setItemInHand(interactionHand, new ItemStack(newSoulShard));
                    player.playSound(SoundEvents.WARDEN_DEATH, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    player.sendSystemMessage(Component.translatable("tooltip.aurora.This entity cannot be captured!"));
                    return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> pTooltipComponents, TooltipFlag flag) {
        String entityTypeKey = targetEntityType.getDescriptionId().replace("entity.minecraft.", "");

        String tooltipKey = "tooltip.aurora." + entityTypeKey;

        pTooltipComponents.add(Component.translatable(tooltipKey, Component.translatable(entityTypeKey)));
        pTooltipComponents.add(Component.translatable("tooltip.aurora.soul_shard"));
        pTooltipComponents.add(Component.translatable(""));

        super.appendHoverText(stack, tooltipContext, pTooltipComponents, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if ((targetEntityType == EntityType.ITEM)){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.aurora.soul_shard";
    }
}
