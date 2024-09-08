package net.zuperz.aurora.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperz.aurora.block.custom.AuroraPedestalBlock;
import net.zuperz.aurora.block.custom.GoldenCauldronBlock;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.item.ModItems;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.Level;
import net.zuperz.aurora.block.ModBlocks;

import javax.swing.*;

public class GoldenCauldronBlockEntity extends BlockEntity implements Container {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private float rotation;

    public GoldenCauldronBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GOLDEN_CAULDRON_BE.get(), pPos, pBlockState);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        setChanged();
        return inventory.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        setChanged();
        ItemStack stack = inventory.get(pSlot);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (stack.getCount() <= pAmount) {
            inventory.set(pSlot, ItemStack.EMPTY);
            return stack;
        } else {
            ItemStack result = stack.copy(); // Copy the stack before shrinking
            result.setCount(pAmount);
            stack.shrink(pAmount);
            inventory.set(pSlot, stack);
            return result;
        }
    }



    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        setChanged();
        return ContainerHelper.takeItem(inventory, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        setChanged();
        if (pStack.isEmpty()) {
            inventory.set(pSlot, ItemStack.EMPTY);
        } else {
            inventory.set(pSlot, pStack.copy());
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        ContainerHelper.saveAllItems(pTag, inventory, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        ContainerHelper.loadAllItems(pTag, inventory, pRegistries);
    }

    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GoldenCauldronBlockEntity cauldron) {
        if (level.isClientSide) {
            return;
        }

        // Check for Aurora Pedestals around the cauldron
        BlockPos[] pedestalPositions = new BlockPos[]{
                pos.offset(2, 0, 0),
                pos.offset(-2, 0, 0),
                pos.offset(0, 0, 2),
                pos.offset(0, 0, -2)
        };

        boolean allPedestalsPresent = true;
        for (BlockPos pedestalPos : pedestalPositions) {
            if (!(level.getBlockState(pedestalPos).getBlock() == ModBlocks.AURORA_PEDESTAL.get())) {
                allPedestalsPresent = false;
                break;
            }
        }

        if (allPedestalsPresent) {
            cauldron.performCrafting(level, pos);
        } else {
        }
    }

    private void performCrafting(Level level, BlockPos pos) {
        ItemStack cauldronItem = this.getItem(0);

        // List to store items from pedestals
        NonNullList<ItemStack> pedestalItems = NonNullList.create();

        // Manually check the items on the pedestals
        BlockPos[] pedestalPositions = new BlockPos[]{
                pos.offset(2, 0, 0),
                pos.offset(-2, 0, 0),
                pos.offset(0, 0, 2),
                pos.offset(0, 0, -2)
        };

        // Collect items from pedestals
        for (BlockPos pedestalPos : pedestalPositions) {
            BlockEntity entity = level.getBlockEntity(pedestalPos);
            if (entity instanceof AuroraPedestalBlockEntity pedestal) {
                ItemStack pedestalItem = pedestal.getItem(0);
                if (!pedestalItem.isEmpty()) {
                    pedestalItems.add(pedestalItem);
                } else {
                    return; // Abort if any pedestal is empty
                }
            }
        }

        // Proceed only if the cauldron contains an item and all pedestals have items
        if (!cauldronItem.isEmpty() && pedestalItems.size() == 4) {
            // Example crafting logic (Replace this with your crafting logic)
            ItemStack craftedItem = new ItemStack(ModItems.AURORA_SKULL.get()); // Replace with your crafted item

            // Consume (delete) the cauldron item
            this.setItem(0, ItemStack.EMPTY);

            // Completely remove and replace each pedestal block
            for (BlockPos pedestalPos : pedestalPositions) {
                level.removeBlockEntity(pedestalPos); // Ensure this removes the block entity properly
                level.setBlock(pedestalPos, Blocks.AIR.defaultBlockState(), 3); // Ensure this removes the block
                level.sendBlockUpdated(pedestalPos, level.getBlockState(pedestalPos), Blocks.AIR.defaultBlockState(), 3);

                // Place a new Aurora Pedestal block at the same position
                level.setBlock(pedestalPos, ModBlocks.AURORA_PEDESTAL.get().defaultBlockState(), 3);

                // Send a block update to ensure the client sees the change
                level.sendBlockUpdated(pedestalPos, level.getBlockState(pedestalPos), level.getBlockState(pedestalPos), 3);
            }

            // Place the crafted item in the cauldron
            this.setItem(0, craftedItem);

            // Update the cauldron's state
            this.setChanged();
            level.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private BlockPos[] getOffsetsTwoBlocksAway() {
        return new BlockPos[]{
                new BlockPos(2, 0, 0),
                new BlockPos(-2, 0, 0),
                new BlockPos(0, 0, 2),
                new BlockPos(0, 0, -2),
        };
    }


    private BlockPos[] getPedestalPositions(BlockPos cauldronPos) {
        return new BlockPos[]{
                cauldronPos.offset(2, 0, 0),
                cauldronPos.offset(-2, 0, 0),
                cauldronPos.offset(0, 0, 2),
                cauldronPos.offset(0, 0, -2)
        };
    }
}