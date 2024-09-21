package net.zuperz.aurora.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.zuperz.aurora.Recipes.*;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.custom.Alter;
import net.zuperz.aurora.block.entity.ItemHandler.ContainerRecipeInput;
import net.zuperz.aurora.block.entity.ItemHandler.CustomItemHandler;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.screen.AlterMenu;
import net.zuperz.aurora.screen.MyMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class AlterBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inputItems = createItemHandler(5);
    private final ItemStackHandler outputItems = createItemHandler(1);

    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> new CustomItemHandler(inputItems));
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> new CustomItemHandler(outputItems));

    private float rotation;
    private int myInt = 0;
    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;

    private static final int MAX_DISTANCE = 20;
    private static final int ENERGY_CONSUMPTION_PER_TICK = 5;

    public AlterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALTER_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AlterBlockEntity.this.progress;
                    case 1 -> AlterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AlterBlockEntity.this.progress = pValue;
                    case 1 -> AlterBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void tick() {
        ArcanePowerTableBlockEntity arcanePowerTable = findNearestArcanePowerTable(this.getBlockPos());

        if (isWithinRangeAndPowered()) {
            increaseCraftingProcess(arcanePowerTable);
            myInt = new Random().nextInt(0, 10);
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

            if (hasRecipe()) {
                increaseCraftingProcess(arcanePowerTable);
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

                if (hasProgressFinished()) {
                    craftItem();
                    resetProgress();
                }
            } else {
                resetProgress();
            }
        } else {
            resetProgress();
            notifyPlayerIfOutOfEnergy();
        }
    }

    private void notifyPlayerIfOutOfEnergy() {
        if (level == null || level.isClientSide) return;  // Only run on the server side

        Player nearestPlayer = level.getNearestPlayer(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2.0, false);

        if (nearestPlayer == null) return;

        ArcanePowerTableBlockEntity arcanePowerTable = findNearestArcanePowerTable(getBlockPos());

        if (arcanePowerTable == null || arcanePowerTable.getEnergyStored() == 0) {
            nearestPlayer.displayClientMessage(Component.literal("No Arcane Power Tablet that is close enough"), true);
        }
    }


    private boolean isWithinRangeAndPowered() {
        BlockPos currentPos = this.getBlockPos();
        ArcanePowerTableBlockEntity nearestPowerTable = findNearestArcanePowerTable(currentPos);

        if (nearestPowerTable != null) {
            int energy = nearestPowerTable.getEnergyStored();
            double distance = currentPos.distSqr(nearestPowerTable.getBlockPos());
            return distance <= MAX_DISTANCE * MAX_DISTANCE && energy > 0;
        }
        return false;
    }

    @Nullable
    private ArcanePowerTableBlockEntity findNearestArcanePowerTable(BlockPos currentPos) {
        for (int dx = -MAX_DISTANCE; dx <= MAX_DISTANCE; dx++) {
            for (int dy = -MAX_DISTANCE; dy <= MAX_DISTANCE; dy++) {
                for (int dz = -MAX_DISTANCE; dz <= MAX_DISTANCE; dz++) {
                    BlockPos nearbyPos = currentPos.offset(dx, dy, dz);
                    BlockEntity blockEntity = level.getBlockEntity(nearbyPos);

                    if (blockEntity instanceof ArcanePowerTableBlockEntity arcanePowerTable) {
                        return arcanePowerTable;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProcess(ArcanePowerTableBlockEntity powerTable) {
            this.progress++;
    }

    public void dropItems() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.get().getSlots());
        for (int i = 0; i < itemHandler.get().getSlots(); i++) {
            inventory.setItem(i, itemHandler.get().getStackInSlot(i));
        }
        Containers.dropContents(level, worldPosition, inventory);
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        Level level = this.level;
        BlockPos pos = this.getBlockPos();

        // Check for wire configuration (Clay or Aurora)
        boolean hasAuroraWire = Alter.arePedestalPositionsAuroraWire(level, pos);
        boolean hasClayWire = Alter.arePedestalPositionsClayWire(level, pos);

        if (!hasAuroraWire && !hasClayWire) {
            return false; // No valid configuration, return false
        }

        if (level == null) return false;

        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots());
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }

        if (hasAuroraWire) {
            Optional<RecipeHolder<AuroraAlterRecipe>> auroraRecipe = level.getRecipeManager()
                    .getRecipeFor(ModRecipes.AURORA_ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

            if (auroraRecipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                    canInsertItemIntoOutputSlot(inventory, auroraRecipe.get().value().output.copy().getItem().getDefaultInstance())) {
                return true;
            }
        }

        if (hasClayWire) {
            Optional<RecipeHolder<AlterRecipe>> recipe = level.getRecipeManager()
                    .getRecipeFor(ModRecipes.ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

            if (recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                    canInsertItemIntoOutputSlot(inventory, recipe.get().value().output.copy().getItem().getDefaultInstance())) {
                return true;
            }
        }

        return false;
    }

    private RecipeInput getRecipeInput(SimpleContainer inventory) {
        return new RecipeInput() {
            @Override
            public ItemStack getItem(int index) {
                return inventory.getItem(index).copy();
            }

            @Override
            public int size() {
                return inventory.getContainerSize();
            }
        };
    }

    private boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        ItemStack outputStack = outputItems.getStackInSlot(0);
        return outputStack.getMaxStackSize() > outputStack.getCount();
    }

    private boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        ItemStack outputStack = outputItems.getStackInSlot(0);
        return outputStack.isEmpty() || (outputStack.getItem() == stack.getItem() && outputStack.getCount() < stack.getMaxStackSize());
    }

    private void craftItem() {
        Level level = this.level;
        if (level == null) return;

        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots());
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }

        // Check for Aurora wire first and use AuroraAlterRecipe
        Optional<RecipeHolder<AuroraAlterRecipe>> auroraRecipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.AURORA_ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        if (auroraRecipeOptional.isPresent()) {
            craftWithRecipe(auroraRecipeOptional.get().value(), inventory);
            return;  // Exit after crafting
        }

        // Check for Clay wire and use AlterRecipe if Aurora recipe wasn't found
        Optional<RecipeHolder<AlterRecipe>> recipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        if (recipeOptional.isPresent()) {
            craftWithRecipe(recipeOptional.get().value(), inventory);
        }
    }

    // Helper method to handle the actual crafting logic
    private void craftWithRecipe(Recipe<?> recipe, SimpleContainer inventory) {
        ItemStack outputStack = recipe.getResultItem(level.registryAccess());

        ItemStack existingOutput = outputItems.getStackInSlot(0);

        int maxStackSize = outputStack.getMaxStackSize();
        int availableSpace = maxStackSize - existingOutput.getCount();
        int amountToAdd = Math.min(availableSpace, outputStack.getCount());

        if (existingOutput.isEmpty()) {
            outputItems.setStackInSlot(0, new ItemStack(outputStack.getItem(), amountToAdd));
        } else if (existingOutput.getItem() == outputStack.getItem()) {
            existingOutput.grow(amountToAdd);
        }

        // Remove items from input slots after crafting
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inputItems.extractItem(i, 1, false);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("alter_block_entity.my_int", myInt);
        tag.putInt("alter_block_entity.progress", this.progress);
        tag.putInt("alter_block_entity.max_progress", this.maxProgress);
        tag.put("alter_block_entity.inputs", inputItems.serializeNBT(registries));
        tag.put("alter_block_entity.outputs", outputItems.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        myInt = tag.getInt("alter_block_entity.my_int");
        progress = tag.getInt("alter_block_entity.progress");
        maxProgress = tag.getInt("alter_block_entity.max_progress");
        inputItems.deserializeNBT(registries, tag.getCompound("alter_block_entity.inputs"));
        outputItems.deserializeNBT(registries, tag.getCompound("alter_block_entity.outputs"));
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
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        loadAdditional(tag, lookupProvider);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public Lazy<IItemHandler> getItemHandler() {
        return itemHandler;
    }

    public ItemStackHandler getInputItems() {
        return inputItems;
    }

    public ItemStackHandler getOutputItems() {
        return outputItems;
    }

    public Lazy<IItemHandler> getInputItemHandler() {
        return inputItemHandler;
    }

    public Lazy<IItemHandler> getOutputItemHandler() {
        return outputItemHandler;
    }

    public int getMyInt() {
        return myInt;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    @Nullable
    @Override
    public @javax.annotation.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AlterMenu(containerId, player, this.getBlockPos());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.aurora.alter");
    }

    public float getRenderingRotation() {
        rotation += 0.1f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    public boolean isInputEmpty(int slot) {
        return inputItems.getStackInSlot(slot).isEmpty();
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

    public Lazy<IItemHandler> getItems() {
        return itemHandler;
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

    public ItemStack getItem(int pSlot) {
        if (pSlot < 5) {
            return inputItems.getStackInSlot(pSlot);
        } else {
            return outputItems.getStackInSlot(pSlot - 5);
        }
    }
}