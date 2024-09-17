package net.zuperz.aurora.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zuperz.aurora.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ArcanePowerTableBlockEntity extends BlockEntity {

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

    public ArcanePowerTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARCANE_POWER_TABLE_BLOCK_ENTITY.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 1 -> ArcanePowerTableBlockEntity.this.energyStored;
                    case 2 -> ArcanePowerTableBlockEntity.this.maxEnergyStored;
                    case 3 -> ArcanePowerTableBlockEntity.this.tickCounter;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 1 -> ArcanePowerTableBlockEntity.this.energyStored = value;
                    case 2 -> ArcanePowerTableBlockEntity.this.maxEnergyStored = value;
                    case 3 -> ArcanePowerTableBlockEntity.this.tickCounter = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

    }

    public void tick(BlockPos pos) {
        BlockPos abovePos = pos.above();
        Block blockAbove = level.getBlockState(abovePos).getBlock();

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


    private boolean hasRecipe() {
        BlockPos abovePos = getBlockPos().above();
        BlockState blockAbove = level.getBlockState(abovePos);
        boolean hasExpansionBlock = blockAbove.getBlock() == Blocks.SOUL_LANTERN;

        if (hasExpansionBlock) {

        }
        return hasExpansionBlock;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("arcane_power_table_entity.my_int", myInt);
        tag.putInt("arcane_power_table_entity.progress", this.progress);
        tag.putInt("arcane_power_table_entity.max_progress", this.maxProgress);
        tag.putInt("arcane_power_table_entity.energy_stored", this.energyStored); // Corrected key
        tag.putInt("arcane_power_table_entity.max_energy_stored", this.maxEnergyStored); // Corrected key
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        myInt = tag.getInt("arcane_power_table_entity.my_int");
        progress = tag.getInt("arcane_power_table_entity.progress");
        maxProgress = tag.getInt("arcane_power_table_entity.max_progress");
        energyStored = tag.getInt("arcane_power_table_entity.energy_stored"); // Corrected key
        maxEnergyStored = tag.getInt("arcane_power_table_entity.max_energy_stored"); // Corrected key
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
}