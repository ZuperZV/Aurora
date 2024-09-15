package net.zuperz.aurora.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.zuperz.aurora.Recipes.*;
import net.zuperz.aurora.block.custom.Alter;
import net.zuperz.aurora.block.entity.ItemHandler.ContainerRecipeInput;
import net.zuperz.aurora.block.entity.ItemHandler.CustomItemHandler;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlterBlockEntity extends BlockEntity {
    private final ItemStackHandler inputItems = createItemHandler(5);
    private final ItemStackHandler outputItems = createItemHandler(1);

    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> new CustomItemHandler(inputItems));
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> new CustomItemHandler(outputItems));

    private float rotation;
    private int progressTickCounter = 0;

    public AlterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ALTER_BE.get(), pPos, pBlockState);
    }

    public boolean isInputEmpty(int slot) {
        return inputItems.getStackInSlot(slot).isEmpty();
    }

    public boolean isOutputEmpty(int slot) {
        return outputItems.getStackInSlot(slot).isEmpty();
    }

    public void clearInput(int slot) {
        inputItems.setStackInSlot(slot, ItemStack.EMPTY);
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public void clearOutput(int slot) {
        outputItems.setStackInSlot(slot, ItemStack.EMPTY);
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    private ItemStackHandler createItemHandler(int slots) {
        return new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    public static void tick(Level level, BlockPos pos, AlterBlockEntity pedestal) {
        SimpleContainer container = new SimpleContainer(5);
        for (int i = 0; i < 5; i++) {
            container.setItem(i, pedestal.inputItems.getStackInSlot(i));
        }

        RecipeInput recipeInput = new ContainerRecipeInput(container);

        boolean hasAuroraPiller = Alter.arePedestalPositionsClayWire(level, pos);
        if (!hasAuroraPiller) {
            return;
        }

        // Try to get a recipe for the current inputs
        Optional<RecipeHolder<AlterRecipe>> alterRecipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALTER_RECIPE_TYPE.get(), recipeInput, level);

        if (alterRecipeOptional.isPresent()) {
            Recipe<?> recipe = alterRecipeOptional.get().value();
            pedestal.progressTickCounter++;

            if (pedestal.progressTickCounter >= 50) {
                ItemStack outputStack = recipe.getResultItem(level.registryAccess());

                if (!outputStack.isEmpty()) {
                    if (!level.isClientSide) {

                        pedestal.outputItems.setStackInSlot(0, outputStack);
                        pedestal.progressTickCounter = 0;

                        level.sendBlockUpdated(pos, pedestal.getBlockState(), pedestal.getBlockState(), Block.UPDATE_ALL);
                    }
                    pedestal.inputItems.setStackInSlot(0, Items.AIR.getDefaultInstance());
                    pedestal.inputItems.setStackInSlot(1, Items.AIR.getDefaultInstance());
                    pedestal.inputItems.setStackInSlot(2, Items.AIR.getDefaultInstance());
                    pedestal.inputItems.setStackInSlot(3, Items.AIR.getDefaultInstance());
                    pedestal.inputItems.setStackInSlot(4, Items.AIR.getDefaultInstance());
                    pedestal.setChanged();

                    pedestal.clearInput(0);
                    pedestal.clearInput(1);
                    pedestal.clearInput(2);
                    pedestal.clearInput(3);
                    pedestal.clearInput(4);
                    pedestal.setChanged();

                }
            }

        }
    }

    public ItemStack getItem(int pSlot) {
        if (pSlot < 5) {
            return inputItems.getStackInSlot(pSlot);
        } else {
            return outputItems.getStackInSlot(pSlot - 5);
        }
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 5) {
            inputItems.setStackInSlot(slot, stack);
        } else {
            outputItems.setStackInSlot(slot - 5, stack);
        }
        setChanged();
        if (!level.isClientSide) {
            markForUpdate();
        }
    }

    private void markForUpdate() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
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
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public Lazy<IItemHandler> getItems() {
        return itemHandler;
    }
}
