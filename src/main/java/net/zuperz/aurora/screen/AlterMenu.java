package net.zuperz.aurora.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;

public class AlterMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private final AlterBlockEntity blockentity;
    public AlterMenu(int containerId, Player player, BlockPos pos) {
        super(ModMenuTypes.ALTER_MENU.get(), containerId);
        AlterBlockEntity AlterBlockEntity;
        this.pos = pos;

        if (player.level().getBlockEntity(pos) instanceof AlterBlockEntity blockentity) {
            AlterBlockEntity = blockentity;
            this.addDataSlots(blockentity.data);

            addSlot(new SlotItemHandler(blockentity.getInputItems(), 0, 73, 54));
            addSlot(new SlotItemHandler(blockentity.getInputItems(), 1, 53, 54));
            addSlot(new SlotItemHandler(blockentity.getInputItems(), 2, 43, 54));
            addSlot(new SlotItemHandler(blockentity.getInputItems(), 3, 23, 54));
            addSlot(new SlotItemHandler(blockentity.getInputItems(), 4, 13, 54));

            addSlot(new SlotItemHandler(blockentity.getOutputItems(), 0, 108, 54) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });

        } else {
            AlterBlockEntity = null;
            System.err.println("Invalid block entity at position: " + pos);
        }

        addPlayerInventory(player.getInventory());
        addPlayerHotbar(player.getInventory());

        this.blockentity = AlterBlockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockentity.getLevel(), blockentity.getBlockPos()), player, ModBlocks.ALTER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 2) {
                if (!this.moveItemStackTo(itemstack1, 2, 11, false)) {
                    if (!this.moveItemStackTo(itemstack1, 11, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (index >= 2 && index < 38) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public boolean isCrafting() {
        return blockentity.getProgress() > 0;
    }

    public int getScaledProgress() {
        int progress = blockentity.data.get(0);
        int maxProgress = blockentity.data.get(1);
        int progressArrowSize = 22;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }


    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}