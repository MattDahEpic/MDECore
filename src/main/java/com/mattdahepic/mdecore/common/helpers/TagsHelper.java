package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagCollection;

import java.util.List;
import java.util.function.Function;

public class TagsHelper {
    private static <T> Tag<T> getOrRegister (TagCollection<T> existingTags, Function<ResourceLocation,Tag<T>> registerFunction, ResourceLocation name) {
        if (existingTags.hasTag(name)) return existingTags.getTag(name);
        return registerFunction.apply(name);
    }
    public static class Item {
        public static Tag<net.minecraft.world.item.Item> tag (String name) {
            return ItemTags.bind(name);
        }
        public static Tag<net.minecraft.world.item.Item> forgeTag (String name) {
            return getOrRegister(ItemTags.getAllTags(), loc -> ItemTags.bind(loc.toString()),new ResourceLocation("forge",name));
        }
    }
    public static class Block {
        public static Tag<net.minecraft.world.level.block.Block> tag (String name) {
            return BlockTags.bind(name);
        }
        public static Tag<net.minecraft.world.level.block.Block> forgeTag (String name) {
            return getOrRegister(BlockTags.getAllTags(),loc -> BlockTags.bind(loc.toString()),new ResourceLocation("forge",name));
        }
    }
}
