package net.zuperz.aurora.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.zuperz.aurora.Entity.ModEntities;
import net.zuperz.aurora.Entity.custom.ChainEntity;

public class CurseTotemItem extends Item {

    public CurseTotemItem(Properties properties) {
        super(properties);
    }

    private boolean hasTotemInInventory(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() instanceof CurseTotemItem) {
                return true;
            }
        }
        return false;
    }

    public void activateTotem(LivingEntity entity, ItemStack totemStack, ServerLevel world) {
        entity.setHealth(1.0F);
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1));
        totemStack.shrink(1);

        liftEntity(entity);

        disableMovement(entity);

        spawnExplosion(entity, world);

        spawnChainStructure(entity, world);
    }

    private void disableMovement(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 255, false, false, true));
    }

    private void liftEntity(LivingEntity entity) {
        // Set upward motion
        Vec3 currentMotion = entity.getDeltaMovement();
        entity.setDeltaMovement(currentMotion.x, 2.0D, currentMotion.z);
    }

    private void spawnExplosion(LivingEntity entity, ServerLevel world) {
        world.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
    }

    private void spawnChainStructure(LivingEntity entity, ServerLevel world) {
        // Get the player's position
        BlockPos playerPos = entity.blockPosition();

        // Create the chain entity and set its position
        ChainEntity chain = new ChainEntity(ModEntities.CHAIN.get(), world);
        chain.setPos(playerPos.getX(), playerPos.getY(), playerPos.getZ());
        chain.setOwner(entity);
        world.addFreshEntity(chain);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && player.level() instanceof ServerLevel world) {

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
                totem.activateTotem(player, totemStack, world);
            }
        }
    }
}