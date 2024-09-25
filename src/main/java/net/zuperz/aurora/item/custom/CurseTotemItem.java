package net.zuperz.aurora.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class CurseTotemItem extends Item {

    public CurseTotemItem(Properties properties) {
        super(properties);
    }

    private boolean hasTotemInInventory(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof CurseTotemItem) {
                return true;  // Totem found
            }
        }
        return false;
    }

    public void activateTotem(LivingEntity entity, ItemStack totemStack) {
        entity.setHealth(1.0F);

        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
        totemStack.shrink(1);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CurseTotemItem totem = null;
            ItemStack totemStack = null;

            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.getItem() instanceof CurseTotemItem) {
                    totem = (CurseTotemItem) itemStack.getItem();
                    totemStack = itemStack;
                    break;
                }
            }

            if (totem != null && player.getHealth() <= 0.0F) {
                event.setCanceled(true);
                totem.activateTotem(player, totemStack);
            }
        }
    }
}