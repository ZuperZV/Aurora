package net.zuperz.aurora.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.zuperz.aurora.Entity.custom.GoblinMinerEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CenterSetterItem extends Item {

    private UUID selectedGoblinMiner;  // Stores the UUID of the selected Goblin Miner

    public CenterSetterItem(Properties properties) {
        super(properties);
        this.selectedGoblinMiner = null;  // No Goblin Miner selected initially
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof GoblinMinerEntity goblinMiner) {
            if (!player.level().isClientSide()) {
                this.selectedGoblinMiner = entity.getUUID();
                player.displayClientMessage(Component.nullToEmpty("Goblin Miner selected!"), true);
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clickedBlockPos = context.getClickedPos();

        if (!level.isClientSide() && this.selectedGoblinMiner != null) {
            Optional<GoblinMinerEntity> goblinMinerOpt = findGoblinMinerByUUID(level, this.selectedGoblinMiner, player.blockPosition(), 100.0);

            if (goblinMinerOpt.isPresent()) {
                GoblinMinerEntity goblinMiner = goblinMinerOpt.get();
                goblinMiner.setCenterBlock(clickedBlockPos);
                player.displayClientMessage(Component.nullToEmpty("Center block set!"), true);

                this.selectedGoblinMiner = null;
                return InteractionResult.SUCCESS;
            } else {
                player.displayClientMessage(Component.nullToEmpty("Goblin Miner not found!"), true);
            }
        }

        return InteractionResult.FAIL;
    }

    private Optional<GoblinMinerEntity> findGoblinMinerByUUID(Level level, UUID uuid, BlockPos playerPos, double searchRadius) {
        List<GoblinMinerEntity> goblinMiners = level.getEntitiesOfClass(
                GoblinMinerEntity.class,
                new net.minecraft.world.phys.AABB(
                        playerPos.getX() - searchRadius, playerPos.getY() - searchRadius, playerPos.getZ() - searchRadius,
                        playerPos.getX() + searchRadius, playerPos.getY() + searchRadius, playerPos.getZ() + searchRadius
                )
        );

        // Search for the Goblin Miner with the matching UUID
        for (GoblinMinerEntity goblinMiner : goblinMiners) {
            if (goblinMiner.getUUID().equals(uuid)) {
                return Optional.of(goblinMiner);
            }
        }

        return Optional.empty();  // Return empty if no matching UUID found
    }
}
