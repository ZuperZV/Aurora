package net.zuperz.aurora.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.zuperz.aurora.aurora;

public class ModTags {

    public static class Items {

        public static final TagKey<Item> SOUL_SHARD = tag("soul_shard");

        public static final TagKey<Item> MOB_SOUL_SHARD = tag("mob_soul_shard");

        public static final TagKey<Item> SUMMONING_SCROLL = tag("summoning_scroll");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, name));
        }

        private static TagKey<Item> moddedTag(String modid, String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modid, name));
        }
    }

    public static class Blocks {

        //public static final TagKey<Block> NEEDS_ROTTEN_BONE_TOOL = tag("needs_rotten_bone_tool");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", name));
        }
    }

    public static class Entities {

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, name));
        }
    }
}
