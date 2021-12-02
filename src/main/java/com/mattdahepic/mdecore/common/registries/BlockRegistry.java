package com.mattdahepic.mdecore.common.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class BlockRegistry {
    protected abstract CreativeModeTab getGroup();
    public abstract void register (RegistryEvent.Register<Block> event);

    //thanks to biomes o plenty
    public Block registerBlock (Block block, String name) {
        return registerBlock(block,name,getGroup());
    }
    public Block registerBlockNoGroup (Block block, String name) {
        return registerBlock(block,name,null);
    }
    public Block registerBlock(Block block, String name, CreativeModeTab group) {
        BlockItem item = new BlockItem(block,new Item.Properties().tab(group));
        block.setRegistryName(name);
        item.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
        return block;
    }
}
