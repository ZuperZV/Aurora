package net.zuperz.aurora.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.item.ModItems;
import net.zuperz.aurora.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
                              CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, aurora.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        tag(ModTags.Items.SOUL_SHARD)
                .add(ModItems.SOUL_SHARD.get().asItem())
                .add(ModItems.SOUL_SHARD_ZOMBIE.get().asItem())
                .add(ModItems.SOUL_SHARD_SKELETON.get().asItem())
                .add(ModItems.SOUL_SHARD_ENDERMAN.get().asItem())
                .add(ModItems.SOUL_SHARD_ALLAY.get().asItem())
                .add(ModItems.SOUL_SHARD_ARMADILLO.get().asItem())
                .add(ModItems.SOUL_SHARD_AXOLOTL.get().asItem())
                .add(ModItems.SOUL_SHARD_BAT.get().asItem())
                .add(ModItems.SOUL_SHARD_BEE.get().asItem())
                .add(ModItems.SOUL_SHARD_BOGGED.get().asItem())
                .add(ModItems.SOUL_SHARD_BLAZE.get().asItem())
                .add(ModItems.SOUL_SHARD_BREEZE.get().asItem())
                .add(ModItems.SOUL_SHARD_CAMEL.get().asItem())
                .add(ModItems.SOUL_SHARD_CAT.get().asItem())
                .add(ModItems.SOUL_SHARD_CAVE_SPIDER.get().asItem())
                .add(ModItems.SOUL_SHARD_CHICKEN.get().asItem())
                .add(ModItems.SOUL_SHARD_CREEPER.get().asItem())
                .add(ModItems.SOUL_SHARD_PIG.get().asItem())
                .add(ModItems.SOUL_SHARD_COW.get().asItem())
                .add(ModItems.SOUL_SHARD_SHEEP.get().asItem())
                .add(ModItems.SOUL_SHARD_RABBIT.get().asItem())
                .add(ModItems.SOUL_SHARD_WOLF.get().asItem())
                .add(ModItems.SOUL_SHARD_GHAST.get().asItem())
                .add(ModItems.SOUL_SHARD_PILLAGER.get().asItem())
                .add(ModItems.SOUL_SHARD_VINDICATOR.get().asItem())
                .add(ModItems.SOUL_SHARD_WITCH.get().asItem())
                .add(ModItems.SOUL_SHARD_ENDERMITE.get().asItem())
                .add(ModItems.SOUL_SHARD_PHANTOM.get().asItem())
                .add(ModItems.SOUL_SHARD_DROWNED.get().asItem())
                .add(ModItems.SOUL_SHARD_WARDEN.get().asItem());

        tag(ModTags.Items.MOB_SOUL_SHARD)
                .add(ModItems.SOUL_SHARD_ZOMBIE.get().asItem())
                .add(ModItems.SOUL_SHARD_SKELETON.get().asItem())
                .add(ModItems.SOUL_SHARD_ENDERMAN.get().asItem())
                .add(ModItems.SOUL_SHARD_ALLAY.get().asItem())
                .add(ModItems.SOUL_SHARD_ARMADILLO.get().asItem())
                .add(ModItems.SOUL_SHARD_AXOLOTL.get().asItem())
                .add(ModItems.SOUL_SHARD_BAT.get().asItem())
                .add(ModItems.SOUL_SHARD_BEE.get().asItem())
                .add(ModItems.SOUL_SHARD_BOGGED.get().asItem())
                .add(ModItems.SOUL_SHARD_BLAZE.get().asItem())
                .add(ModItems.SOUL_SHARD_BREEZE.get().asItem())
                .add(ModItems.SOUL_SHARD_CAMEL.get().asItem())
                .add(ModItems.SOUL_SHARD_CAT.get().asItem())
                .add(ModItems.SOUL_SHARD_CAVE_SPIDER.get().asItem())
                .add(ModItems.SOUL_SHARD_CHICKEN.get().asItem())
                .add(ModItems.SOUL_SHARD_CREEPER.get().asItem())
                .add(ModItems.SOUL_SHARD_PIG.get().asItem())
                .add(ModItems.SOUL_SHARD_COW.get().asItem())
                .add(ModItems.SOUL_SHARD_SHEEP.get().asItem())
                .add(ModItems.SOUL_SHARD_RABBIT.get().asItem())
                .add(ModItems.SOUL_SHARD_WOLF.get().asItem())
                .add(ModItems.SOUL_SHARD_GHAST.get().asItem())
                .add(ModItems.SOUL_SHARD_PILLAGER.get().asItem())
                .add(ModItems.SOUL_SHARD_VINDICATOR.get().asItem())
                .add(ModItems.SOUL_SHARD_WITCH.get().asItem())
                .add(ModItems.SOUL_SHARD_ENDERMITE.get().asItem())
                .add(ModItems.SOUL_SHARD_PHANTOM.get().asItem())
                .add(ModItems.SOUL_SHARD_DROWNED.get().asItem())
                .add(ModItems.SOUL_SHARD_WARDEN.get().asItem());

        tag(ModTags.Items.SUMMONING_SCROLL)
                .add(ModItems.SUMMONING_SCROLL_ZOMBIE.get().asItem())
                .add(ModItems.SUMMONING_SCROLL_ENDERMAN.get().asItem())
                .add(ModItems.SUMMONING_SCROLL_GHAST.get().asItem());
    }
}