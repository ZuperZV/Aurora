package net.zuperz.aurora.events;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.item.custom.SaplingClayJarItem;
import net.zuperz.aurora.item.custom.StoneSkullTwig;

public class ModEvents {

    // Register the event bus
    public static void registerEvents() {
        NeoForge.EVENT_BUS.register(ModEvents.class);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getEntity().getCommandSenderWorld();
        BlockPos pos = event.getPos();

        // Check if block is fire and player holds a SaplingClayJarItem
        if (world.getBlockState(pos).getBlock() == Blocks.FIRE) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getItem() instanceof SaplingClayJarItem) {

                Player player = event.getEntity();
                Inventory inventory = player.getInventory();

                // Reduce item stack by 1
                itemStack.shrink(1);

                // Create new FIRE_CLAY_JAR item and add to player's inventory
                ItemStack newItem = new ItemStack(ModItems.FIRE_CLAY_JAR.get());
                if (!inventory.add(newItem)) {
                    player.drop(newItem, false);
                }

                // Set block to air
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                // Mark event as successful
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
