package com.mattdahepic.mdecore.common.registries;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class BlockRegistry {
    protected abstract ItemGroup getGroup();
    public abstract void register (RegistryEvent.Register<Block> event);

    //thanks to biomes o plenty
    public Block registerBlock (Block block, String name) {
        return registerBlock(block,name,getGroup());
    }
    public Block registerBlockNoGroup (Block block, String name) {
        return registerBlock(block,name,null);
    }
    public Block registerBlock(Block block, String name, ItemGroup group) {
        BlockItem item = new BlockItem(block,new Item.Properties().group(group));
        block.setRegistryName(name);
        item.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
        return block;
    }
}
