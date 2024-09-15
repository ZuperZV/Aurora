package net.zuperz.aurora.datagen;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.AURORA_PEDESTAL.get());
        dropSelf(ModBlocks.GOLDEN_CAULDRON.get());
        dropSelf(ModBlocks.PEDESTAL_SLAB.get());
        dropSelf(ModBlocks.MY_BLOCK.get());

        dropSelf(ModBlocks.ALCHE_FLAME.get());

        dropSelf(ModBlocks.AURORA_PILLER.get());
        dropSelf(ModBlocks.BEAM.get());

        this.add(ModBlocks.AURORA_WIRE.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.AURORA_DUST.get()))
                        )
        );
        this.add(ModBlocks.CLAY_WIRE.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.CLAY_DUST.get()))
                        )
        );

        this.add(ModBlocks.UPPER_AURORA_PILLER.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModBlocks.AURORA_PILLER.get()))
                        )
        );

        this.add(ModBlocks.UPPER_BEAM.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModBlocks.BEAM.get()))
                        )
        );

        this.add(ModBlocks.BLUESTONE.get(),
                block -> LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 4)))
                                .add(LootItem.lootTableItem(ModItems.BLUESTONE_DUST.get()))
                        )
        );
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock, this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    protected LootTable.Builder createMultipleItemLogDrops(Block pBlock, Item log, Item item, float minDrops, float maxDrops) {
        LootPool.Builder logPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(log)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

        LootPool.Builder itemPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .when(LootItemRandomChanceCondition.randomChance(0.8f)));

        return LootTable.lootTable()
                .withPool(logPool)
                .withPool(itemPool);
    }

    protected LootTable.Builder createMultipleOreDropsAmber(Block block, Item item, Item ekstraItem, Item item2, Item item3, Item item4, float minDrops, float maxDrops) {
        LootPool.Builder amberPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))));

        LootPool.Builder ekstraItemPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(ekstraItem)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops))))
                        .when(LootItemRandomChanceCondition.randomChance(0.8f))
                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModItems.SKRAP_AURORA.get())));



        LootPool.Builder item2Pool = LootPool.lootPool()
                .add(LootItem.lootTableItem(item2)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(LootItemRandomChanceCondition.randomChance(0.08f)));

        LootPool.Builder item3Pool = LootPool.lootPool()
                .add(LootItem.lootTableItem(item3)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(LootItemRandomChanceCondition.randomChance(0.05f)));

        LootPool.Builder item4Pool = LootPool.lootPool()
                .add(LootItem.lootTableItem(item4)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(LootItemRandomChanceCondition.randomChance(0.05f)));

        return LootTable.lootTable()
                .withPool(amberPool)
                .withPool(ekstraItemPool)
                .withPool(item2Pool)
                .withPool(item3Pool)
                .withPool(item4Pool);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}