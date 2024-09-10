package net.zuperz.aurora.block.entity.custom;

import com.google.common.collect.Maps;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.zuperz.aurora.Recipes.*;
import net.zuperz.aurora.block.entity.ItemHandler.CustomItemHandler;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import net.zuperz.aurora.screen.AlcheFlameMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.buildFuels;

public class AlcheFlameBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler inputItems = createItemHandler(3);
    private final ItemStackHandler outputItems = createItemHandler(2);

    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> new CombinedInvWrapper(inputItems, outputItems));
    private final Lazy<IItemHandler> inputItemHandler = Lazy.of(() -> new CustomItemHandler(inputItems));
    private final Lazy<IItemHandler> outputItemHandler = Lazy.of(() -> new CustomItemHandler(outputItems));

    private int myInt = 0;
    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;

    private int fuelBurnTime = 0;
    private int maxFuelBurnTime = 0;

    public AlcheFlameBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHE_FLAME_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AlcheFlameBlockEntity.this.progress;
                    case 1 -> AlcheFlameBlockEntity.this.maxProgress;
                    case 2 -> AlcheFlameBlockEntity.this.fuelBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AlcheFlameBlockEntity.this.progress = pValue;
                    case 1 -> AlcheFlameBlockEntity.this.maxProgress = pValue;
                    case 2 -> AlcheFlameBlockEntity.this.fuelBurnTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    public void tick() {
        Random r = new Random();
        increaseCraftingProcess();
        myInt = r.nextInt(0, 10);
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

        if (hasRecipe(this, (ServerLevel) level, inputItems.getStackInSlot(0))) {
            increaseCraftingProcess();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);

            if (hasProgressFinished()) {
                craftItem(this, inputItems.getStackInSlot(0));
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProcess() {
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

    private boolean hasRecipe(BlockEntity blockEntity, ServerLevel serverLevel, ItemStack itemStack) {
        Level level = this.level;
        if (level == null) return false;

        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots());
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }

        Optional<RecipeHolder<AlcheFlameRecipe>> alcheRecipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALCHE_FLAME_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipe = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(inputItems.getStackInSlot(1)), level);

        return (alcheRecipe.isPresent() && smeltingRecipe.isPresent() && canInsertAmountIntoOutputSlot(inventory)) ||
                (smeltingRecipe.isPresent() && canInsertAmountIntoOutputSlot(inventory));
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

    private void craftItem(AlcheFlameBlockEntity serverLevel, ItemStack itemStack) {
        Level level = this.level;
        if (level == null) return;

        SimpleContainer inventory = new SimpleContainer(inputItems.getSlots());
        for (int i = 0; i < inputItems.getSlots(); i++) {
            inventory.setItem(i, inputItems.getStackInSlot(i));
        }

        Optional<RecipeHolder<AlcheFlameRecipe>> alcheRecipeOptional = level.getRecipeManager()
                .getRecipeFor(ModRecipes.ALCHE_FLAME_RECIPE_TYPE.get(), getRecipeInput(inventory), level);

        Optional<RecipeHolder<SmeltingRecipe>> smeltingRecipeOptional = serverLevel.getLevel()
                .getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(inputItems.getStackInSlot(1)), level);

        if (alcheRecipeOptional.isPresent() && smeltingRecipeOptional.isPresent()) {
            AlcheFlameRecipe recipe = alcheRecipeOptional.get().value();
            ItemStack result = recipe.getResultItem(level.registryAccess());

            // Legg resultatet til i output-spor
            ItemStack outputStack = outputItems.getStackInSlot(0);
            if (outputStack.isEmpty()) {
                outputItems.setStackInSlot(0, result.copy());
            } else if (outputStack.getItem() == result.getItem()) {
                outputStack.grow(result.getCount());
            }

            inputItems.extractItem(0, 1, false);
        }

        if (smeltingRecipeOptional.isPresent()) {
            SmeltingRecipe smeltingRecipe = smeltingRecipeOptional.get().value();
            ItemStack result = smeltingRecipe.getResultItem(level.registryAccess());

            ItemStack outputStack = outputItems.getStackInSlot(1);
            if (outputStack.isEmpty()) {
                outputItems.setStackInSlot(1, result.copy());
            } else if (outputStack.getItem() == result.getItem()) {
                outputStack.grow(result.getCount());
            }

            inputItems.extractItem(1, 1, false);
        }
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("my_block_entity.my_int", myInt);
        tag.putInt("my_block_entity.progress", this.progress);
        tag.putInt("my_block_entity.max_progress", this.maxProgress);
        tag.put("my_block_entity.inputs", inputItems.serializeNBT(registries));
        tag.put("my_block_entity.outputs", outputItems.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        myInt = tag.getInt("my_block_entity.my_int");
        progress = tag.getInt("my_block_entity.progress");
        maxProgress = tag.getInt("my_block_entity.max_progress");
        inputItems.deserializeNBT(registries, tag.getCompound("my_block_entity.inputs"));
        outputItems.deserializeNBT(registries, tag.getCompound("my_block_entity.outputs"));
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
        return new AlcheFlameMenu(containerId, player, this.getBlockPos());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.aurora.alche_flame");
    }
}