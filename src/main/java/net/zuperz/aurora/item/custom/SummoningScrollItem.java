package net.zuperz.aurora.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SummoningScrollItem extends Item {
    private final EntityType<?> defaultType;

    public SummoningScrollItem(EntityType<?> entityType, Item.Properties properties) {
        super(properties);
        this.defaultType = entityType;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level instanceof ServerLevel) {
            ItemStack itemStack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            Direction direction = context.getClickedFace();

            BlockPos spawnPos = blockPos.relative(direction);

            EntityType<?> entityType = this.defaultType;
            Entity entity = entityType.spawn((ServerLevel)level, itemStack, context.getPlayer(), spawnPos, MobSpawnType.SPAWN_EGG, true, true);

            if (entity != null) {
                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, entity.position());
                itemStack.shrink(1);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        }

        BlockPos blockPos = blockHitResult.getBlockPos();
        if (level instanceof ServerLevel serverLevel && level.getBlockState(blockPos).getBlock() instanceof LiquidBlock) {
            Entity entity = this.defaultType.spawn(serverLevel, itemStack, player, blockPos, MobSpawnType.SPAWN_EGG, false, false);

            if (entity != null) {
                level.gameEvent(player, GameEvent.ENTITY_PLACE, entity.position());
                player.awardStat(Stats.ITEM_USED.get(this));
                itemStack.shrink(1);
                return InteractionResultHolder.consume(itemStack);
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> pTooltipComponents, TooltipFlag flag) {
        String entityTypeKey = defaultType.getDescriptionId().replace("entity.minecraft.", "");

        String tooltipKey = "tooltip.aurora." + entityTypeKey;

        pTooltipComponents.add(Component.translatable(tooltipKey, Component.translatable(entityTypeKey)));

        super.appendHoverText(stack, tooltipContext, pTooltipComponents, flag);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.aurora.summoning_scroll";
    }
}