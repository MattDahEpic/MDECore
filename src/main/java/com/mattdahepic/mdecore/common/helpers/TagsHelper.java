package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Function;

public class TagsHelper {
    private static <T> ITag.INamedTag<T> getOrRegister (List<? extends ITag.INamedTag<T>> existingTags, Function<ResourceLocation,ITag.INamedTag<T>> registerFunction, ResourceLocation name) {
        for (ITag.INamedTag<T> existing : existingTags) {
            if (existing.getName().equals(name)) return existing;
        }
        return registerFunction.apply(name);
    }
    public static class Item {
        public static ITag.INamedTag<net.minecraft.item.Item> tag (String name) {
            return ItemTags.makeWrapperTag(name);
        }
        public static ITag.INamedTag<net.minecraft.item.Item> forgeTag (String name) {
            return getOrRegister(ItemTags.getAllTags(),loc -> ItemTags.makeWrapperTag(loc.toString()),new ResourceLocation("forge",name));
        }
    }
    public static class Block {
        public static ITag.INamedTag<net.minecraft.block.Block> tag (String name) {
            return BlockTags.makeWrapperTag(name);
        }
        public static ITag.INamedTag<net.minecraft.block.Block> forgeTag (String name) {
            return getOrRegister(BlockTags.getAllTags(),loc -> BlockTags.makeWrapperTag(loc.toString()),new ResourceLocation("forge",name));
        }
    }
}
