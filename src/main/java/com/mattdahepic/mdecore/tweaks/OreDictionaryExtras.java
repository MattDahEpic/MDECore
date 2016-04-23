package com.mattdahepic.mdecore.tweaks;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryExtras {
    public static void preInit () {
        //wooden STAIRS
        OreDictionary.registerOre("stair",Blocks.OAK_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.OAK_STAIRS);
        OreDictionary.registerOre("stair",Blocks.SPRUCE_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.SPRUCE_STAIRS);
        OreDictionary.registerOre("stair",Blocks.BIRCH_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.BIRCH_STAIRS);
        OreDictionary.registerOre("stair",Blocks.JUNGLE_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.JUNGLE_STAIRS);
        OreDictionary.registerOre("stair", Blocks.ACACIA_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.ACACIA_STAIRS);
        OreDictionary.registerOre("stair",Blocks.DARK_OAK_STAIRS);
        OreDictionary.registerOre("stairWood",Blocks.DARK_OAK_STAIRS);
        //other STAIRS
        OreDictionary.registerOre("stair",Blocks.SANDSTONE_STAIRS);
        OreDictionary.registerOre("stairSandstone",Blocks.SANDSTONE_STAIRS);
        OreDictionary.registerOre("stair",Blocks.RED_SANDSTONE_STAIRS);
        OreDictionary.registerOre("stairSandstone",Blocks.RED_SANDSTONE_STAIRS);
        OreDictionary.registerOre("stair",Blocks.STONE_STAIRS);
        OreDictionary.registerOre("stairCobblestone",Blocks.STONE_STAIRS);
        OreDictionary.registerOre("stair",Blocks.BRICK_STAIRS);
        OreDictionary.registerOre("stairBrick",Blocks.BRICK_STAIRS);
        OreDictionary.registerOre("stair",Blocks.STONE_BRICK_STAIRS);
        OreDictionary.registerOre("stairStoneBrick",Blocks.STONE_BRICK_STAIRS);
        OreDictionary.registerOre("stair",Blocks.NETHER_BRICK_STAIRS);
        OreDictionary.registerOre("stairNetherBrick",Blocks.NETHER_BRICK_STAIRS);
        OreDictionary.registerOre("stair",Blocks.QUARTZ_STAIRS);
        OreDictionary.registerOre("stairQuartz",Blocks.QUARTZ_STAIRS);
        //wooden slabs
        OreDictionary.registerOre("slab",Blocks.WOODEN_SLAB);
        OreDictionary.registerOre("slabWood",Blocks.WOODEN_SLAB);
        //other slabs
        OreDictionary.registerOre("slab",Blocks.STONE_SLAB);
        OreDictionary.registerOre("slabStone",new ItemStack(Blocks.STONE_SLAB,1,0));
        OreDictionary.registerOre("slabSandstone",new ItemStack(Blocks.STONE_SLAB,1,1));
        OreDictionary.registerOre("slabCobblestone",new ItemStack(Blocks.STONE_SLAB,1,3));
        OreDictionary.registerOre("slabBrick",new ItemStack(Blocks.STONE_SLAB,1,4)); //inb4 borderlands reference
        OreDictionary.registerOre("slabStoneBrick",new ItemStack(Blocks.STONE_SLAB,1,5));
        OreDictionary.registerOre("slabNetherBrick",new ItemStack(Blocks.STONE_SLAB,1,6));
        OreDictionary.registerOre("slabQuartz",new ItemStack(Blocks.STONE_SLAB,1,7));
        OreDictionary.registerOre("slab",Blocks.STONE_SLAB2);
        OreDictionary.registerOre("slabSandstone",new ItemStack(Blocks.STONE_SLAB2,1,0));
        //mossy cobble
        OreDictionary.registerOre("cobblestoneMoss",Blocks.MOSSY_COBBLESTONE);
        //fences
        OreDictionary.registerOre("fence",Blocks.OAK_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.OAK_FENCE);
        OreDictionary.registerOre("fence",Blocks.SPRUCE_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.SPRUCE_FENCE);
        OreDictionary.registerOre("fence",Blocks.BIRCH_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.BIRCH_FENCE);
        OreDictionary.registerOre("fence",Blocks.JUNGLE_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.JUNGLE_FENCE);
        OreDictionary.registerOre("fence",Blocks.DARK_OAK_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.DARK_OAK_FENCE);
        OreDictionary.registerOre("fence",Blocks.ACACIA_FENCE);
        OreDictionary.registerOre("fenceWood",Blocks.ACACIA_FENCE);
        OreDictionary.registerOre("fence",Blocks.NETHER_BRICK_FENCE);
        OreDictionary.registerOre("fenceNETHERBRICK",Blocks.NETHER_BRICK_FENCE);
        //fence gates
        OreDictionary.registerOre("fenceGate",Blocks.OAK_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.OAK_FENCE_GATE);
        OreDictionary.registerOre("fenceGate",Blocks.SPRUCE_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.SPRUCE_FENCE_GATE);
        OreDictionary.registerOre("fenceGate",Blocks.BIRCH_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.BIRCH_FENCE_GATE);
        OreDictionary.registerOre("fenceGate",Blocks.JUNGLE_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.JUNGLE_FENCE_GATE);
        OreDictionary.registerOre("fenceGate",Blocks.DARK_OAK_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.DARK_OAK_FENCE_GATE);
        OreDictionary.registerOre("fenceGate",Blocks.ACACIA_FENCE_GATE);
        OreDictionary.registerOre("fenceGateWood",Blocks.ACACIA_FENCE_GATE);
        //walls
        OreDictionary.registerOre("wall",Blocks.COBBLESTONE_WALL);
        OreDictionary.registerOre("wallCobblestone",new ItemStack(Blocks.COBBLESTONE_WALL,1,0));
        OreDictionary.registerOre("wallMoss",new ItemStack(Blocks.COBBLESTONE_WALL,1,1));
    }
}
