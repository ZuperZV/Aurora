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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.zuperz.aurora.Recipes.AlterRecipe;
import net.zuperz.aurora.Recipes.ArcanePowerTableRecipe;
import net.zuperz.aurora.Recipes.AuroraAlterRecipe;
import net.zuperz.aurora.Recipes.ModRecipes;
import net.zuperz.aurora.block.custom.Alter;
import net.zuperz.aurora.block.entity.ItemHandler.CustomItemHandler;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.screen.AlterMenu;
import net.zuperz.aurora.screen.ArcanePowerTableMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;

public class ArcanePowerTableBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler inputItems = createItemHandler(3);
    private final ItemStackHandler outputItems = createItemHandler(1);

    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> new CustomItemHandler(inputItems));
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> new CustomItemHandler(outputItems));

    private float rotation;
    private int myInt = 0;
    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int tickCounter = 0;
    private int energyStored = 0;
    private int maxEnergyStored = 100;
    private int energyGenerationRate = 1;
    private boolean hasAddedExtraEnergy = false;

    private static final int MAX_DISTANCE = 20;

    public ArcanePowerTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARCANE_POWER_TABLE_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ArcanePowerTableBlockEntity.this.progress;
                    case 1 -> ArcanePowerTableBlockEntity.this.maxProgress;
                    case 2 -> ArcanePowerTableBlockEntity.this.energyStored;
                    case 3 -> ArcanePowerTableBlockEntity.this.maxEnergyStored;
                    case 4 -> ArcanePowerTableBlockEntity.this.tickCounter;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int pValue) {
                switch (index) {
                    case 0 -> ArcanePowerTableBlockEntity.this.progress = pValue;
                    case 1 -> ArcanePowerTableBlockEntity.this.maxProgress = pValue;
                    case 2 -> ArcanePowerTableBlockEntity.this.energyStored = pValue;
                    case 3 -> ArcanePowerTableBlockEntity.this.maxEnergyStored = pValue;
                    case 4 -> ArcanePowerTableBlockEntity.this.tickCounter = pValue;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };

    }

    public void tick(BlockPos pos) {
        BlockPos abovePos = pos.above();
        Block blockAbove = level.getBlockState(abovePos).getBlock();
        ArcanePowerTableBlockEntity arcanePowerTable = findNearestArcanePowerTable(this.getBlockPos());

        tickCounter++;
        if (tickCounter >= 20) {
            generateEnergy();
            tickCounter = 0;
        }

        if (blockAbove == Blocks.SOUL_LANTERN) {
            if (!hasAddedExtraEnergy) {
                increaseMaxEnergyStorage();
            }
        } else {
            if (hasAddedExtraEnergy) {
                decreaseMaxEnergyStorage();
            }
        }

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

    private void resetProgress() {
        this.progress = 0;
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

        Optional<RecipeHolder<ArcanePowerTableRecipe>> auroraRecipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.AURORA_POWER_TABLE_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        if (auroraRecipeOptional.isPresent()) {
            craftWithRecipe(auroraRecipeOptional.get().value(), inventory);
            return;
        }

        /*Optional<RecipeHolder<AlterRecipe>> recipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        if (recipeOptional.isPresent()) {
            craftWithRecipe(recipeOptional.get().value(), inventory);
        }
         */
    }

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

        for (int i = 0; i < inputItems.getSlots(); i++) {
            inputItems.extractItem(i, 1, false);
        }
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

    private void notifyPlayerIfOutOfEnergy() {
        if (level == null || level.isClientSide) return;  // Only run on the server side

        Player nearestPlayer = level.getNearestPlayer(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2.0, false);

        if (nearestPlayer == null) return;
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

    private void generateEnergy() {
        if (energyStored < maxEnergyStored) {
            energyStored += energyGenerationRate;
            if (energyStored > maxEnergyStored) {
                energyStored = maxEnergyStored;
            }
        }
    }

    public int getEnergyStored() {
        return energyStored;
    }

    public int getMaxEnergyStored() {
        return maxEnergyStored;
    }

    private void increaseMaxEnergyStorage() {
        if (this.maxEnergyStored < 150) {
            this.maxEnergyStored += 50;
            if (this.maxEnergyStored > 150) {
                this.maxEnergyStored = 150;
            }

            hasAddedExtraEnergy = true;

            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private void decreaseMaxEnergyStorage() {
        if (this.maxEnergyStored >= 100) {
            this.maxEnergyStored -= 50;
            if (this.maxEnergyStored < 100) {
                this.maxEnergyStored = 100;
            }

            if (this.energyStored > this.maxEnergyStored) {
                this.energyStored = this.maxEnergyStored;
            }

            hasAddedExtraEnergy = false;

            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private boolean hasBlock() {
        BlockPos abovePos = getBlockPos().above();
        BlockState blockAbove = level.getBlockState(abovePos);
        boolean hasExpansionBlock = blockAbove.getBlock() == Blocks.SOUL_LANTERN;

        if (hasExpansionBlock) {

        }
        return hasExpansionBlock;
    }

    private boolean hasRecipe() {
        Level level = this.level;
        BlockPos pos = this.getBlockPos();


        if (level == null) return false;

        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots());
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }

        Optional<RecipeHolder<ArcanePowerTableRecipe>> auroraRecipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.AURORA_POWER_TABLE_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        if (auroraRecipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, auroraRecipe.get().value().output.copy().getItem().getDefaultInstance())) {
            return true;
        }

        /*if (hasClayWire) {
            Optional<RecipeHolder<AlterRecipe>> recipe = level.getRecipeManager()
                    .getRecipeFor(ModRecipes.ALTER_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

            if (recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                    canInsertItemIntoOutputSlot(inventory, recipe.get().value().output.copy().getItem().getDefaultInstance())) {
                return true;
            }
        }
         */

        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("arcane_power_table_entity.my_int", myInt);
        tag.putInt("arcane_power_table_entity.progress", this.progress);
        tag.putInt("arcane_power_table_entity.max_progress", this.maxProgress);
        tag.putInt("arcane_power_table_entity.energy_stored", this.energyStored);
        tag.putInt("arcane_power_table_entity.max_energy_stored", this.maxEnergyStored);
        tag.put("arcane_power_table_entity.inputs", inputItems.serializeNBT(registries));
        tag.put("arcane_power_table_entity.outputs", outputItems.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        myInt = tag.getInt("arcane_power_table_entity.my_int");
        progress = tag.getInt("arcane_power_table_entity.progress");
        maxProgress = tag.getInt("arcane_power_table_entity.max_progress");
        energyStored = tag.getInt("arcane_power_table_entity.energy_stored");
        maxEnergyStored = tag.getInt("arcane_power_table_entity.max_energy_stored");
        inputItems.deserializeNBT(registries, tag.getCompound("arcane_power_table_entity.inputs"));
        outputItems.deserializeNBT(registries, tag.getCompound("arcane_power_table_entity.outputs"));
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

    public int getProgress() {
        return progress;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.aurora.arcane_power_table");
    }

    @Nullable
    @Override
    public @javax.annotation.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ArcanePowerTableMenu(containerId, player, this.getBlockPos());
    }

    public float getRenderingRotation() {
        rotation += 0.1f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
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

    public boolean isInputEmpty(int slot) {
        return inputItems.getStackInSlot(slot).isEmpty();
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot < 3) {
            inputItems.setStackInSlot(slot, stack);
        } else {
            outputItems.setStackInSlot(slot - 3, stack);
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
        if (pSlot < 3) {
            return inputItems.getStackInSlot(pSlot);
        } else {
            return outputItems.getStackInSlot(pSlot - 3);
        }
    }

}