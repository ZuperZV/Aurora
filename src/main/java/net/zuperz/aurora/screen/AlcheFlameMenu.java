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
import net.zuperz.aurora.block.entity.custom.AlcheFlameBlockEntity;

public class AlcheFlameMenu extends AbstractContainerMenu {
    private final BlockPos pos;
    private final AlcheFlameBlockEntity blockentity;
    public AlcheFlameMenu(int containerId, Player player, BlockPos pos) {
        super(ModMenuTypes.ALCHE_FLAME_MENU.get(), containerId);
        AlcheFlameBlockEntity alcheFlameBlockEntity;
        this.pos = pos;

        if (player.level().getBlockEntity(pos) instanceof AlcheFlameBlockEntity blockentity) {
            alcheFlameBlockEntity = blockentity;
            this.addDataSlots(blockentity.data);

            addSlot(new SlotItemHandler(blockentity.getInputItems(), 0, 73, 54));
            addSlot(new SlotItemHandler(blockentity.getOutputItems(), 0, 108, 54) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });

            addSlot(new SlotItemHandler(blockentity.getInputItems(), 1, 48, 20));
            addSlot(new SlotItemHandler(blockentity.getInputItems(), 2, 48, 54));
            addSlot(new SlotItemHandler(blockentity.getOutputItems(), 1, 108, 20) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });

        } else {
            alcheFlameBlockEntity = null;
            System.err.println("Invalid block entity at position: " + pos);
        }

        addPlayerInventory(player.getInventory());
        addPlayerHotbar(player.getInventory());

        this.blockentity = alcheFlameBlockEntity;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockentity.getLevel(), blockentity.getBlockPos()), player, ModBlocks.ALCHE_FLAME.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();

            if (index < 2) {
                if (!moveItemStackTo(stackInSlot, 2, 4, false)) {
                    if (!moveItemStackTo(stackInSlot, 5, slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (index < 4) {
                return ItemStack.EMPTY;
            }
            else if (index == 4) {
                if (!moveItemStackTo(stackInSlot, 5, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!moveItemStackTo(stackInSlot, 0, 2, false)) {
                    if (!moveItemStackTo(stackInSlot, 2, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.set(stackInSlot);
            }

            slot.setChanged();

            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return itemStack;
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

    public int getScaledFuelBurnTime() {
        int fuelBurnTime = blockentity.data.get(2);
        int maxFuelBurnTime = blockentity.getMaxFuelBurnTime();
        int fuelBarHeight = 14;

        return maxFuelBurnTime != 0 && fuelBurnTime != 0 ? fuelBurnTime * fuelBarHeight / maxFuelBurnTime : 0;
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