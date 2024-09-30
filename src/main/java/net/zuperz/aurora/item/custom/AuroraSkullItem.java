package net.zuperz.aurora.item.custom;

import net.zuperz.aurora.component.ModDataComponentTypes;
import net.zuperz.aurora.component.StarDustData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AuroraSkullItem extends Item {
    public AuroraSkullItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        StarDustData starDust = stack.get(ModDataComponentTypes.STAR_DUST.get());

        if (starDust == null) {
            stack.set(ModDataComponentTypes.STAR_DUST.get(), new StarDustData(0));
        } else {
            int currentValue = starDust.getValue1();

            if (currentValue < 100) {
                stack.set(ModDataComponentTypes.STAR_DUST.get(), new StarDustData(currentValue + 1));
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        StarDustData starDust = stack.get(ModDataComponentTypes.STAR_DUST.get());

        if (starDust != null) {
            tooltip.add(Component.literal("Star Dust: " + starDust.getValue1()));
        }

        super.appendHoverText(stack, (TooltipContext) tooltipContext, tooltip, flag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        StarDustData starDust = stack.get(ModDataComponentTypes.STAR_DUST.get());
        return starDust != null && starDust.getValue1() >= 100;
    }

    public void decrease(ItemStack stack, int amount) {
        StarDustData starDust = stack.get(ModDataComponentTypes.STAR_DUST.get());

        starDust.decrease(amount);
    }

    public @Nullable StarDustData GetStarDust(ItemStack stack) {
        return stack.get(ModDataComponentTypes.STAR_DUST);
    }
}
