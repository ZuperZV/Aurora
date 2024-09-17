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
import net.zuperz.aurora.block.ModBlocks;
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

                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        } else if (world.getBlockState(pos).getBlock() == Blocks.LODESTONE) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getItem() instanceof StoneSkullTwig) {

                itemStack.shrink(1);

                world.setBlockAndUpdate(pos, ModBlocks.ALTER.get().defaultBlockState());

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        } else if (world.getBlockState(pos).getBlock() == Blocks.STONE_BRICKS) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.getItem() instanceof StoneSkullTwig && world.getBlockState(pos.offset(0, -1, 0)).getBlock() == Blocks.STONE_BRICKS) {

                world.setBlockAndUpdate(pos.offset(0, -1, 0), ModBlocks.BEAM.get().defaultBlockState());
                world.setBlockAndUpdate(pos, ModBlocks.UPPER_BEAM.get().defaultBlockState());

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
