package com.mattdahepic.mdecore.tweaks;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryExtras {
    public static void preInit () {
        //wooden stairs
        OreDictionary.registerOre("stair",Blocks.oak_stairs);
        OreDictionary.registerOre("stairWood",Blocks.oak_stairs);
        OreDictionary.registerOre("stair",Blocks.spruce_stairs);
        OreDictionary.registerOre("stairWood",Blocks.spruce_stairs);
        OreDictionary.registerOre("stair",Blocks.birch_stairs);
        OreDictionary.registerOre("stairWood",Blocks.birch_stairs);
        OreDictionary.registerOre("stair",Blocks.jungle_stairs);
        OreDictionary.registerOre("stairWood",Blocks.jungle_stairs);
        OreDictionary.registerOre("stair", Blocks.acacia_stairs);
        OreDictionary.registerOre("stairWood",Blocks.acacia_stairs);
        OreDictionary.registerOre("stair",Blocks.dark_oak_stairs);
        OreDictionary.registerOre("stairWood",Blocks.dark_oak_stairs);
        //other stairs
        OreDictionary.registerOre("stair",Blocks.sandstone_stairs);
        OreDictionary.registerOre("stairSandstone",Blocks.sandstone_stairs);
        OreDictionary.registerOre("stair",Blocks.red_sandstone_stairs);
        OreDictionary.registerOre("stairSandstone",Blocks.red_sandstone_stairs);
        OreDictionary.registerOre("stair",Blocks.stone_stairs);
        OreDictionary.registerOre("stairCobblestone",Blocks.stone_stairs);
        OreDictionary.registerOre("stair",Blocks.brick_stairs);
        OreDictionary.registerOre("stairBrick",Blocks.brick_stairs);
        OreDictionary.registerOre("stair",Blocks.stone_brick_stairs);
        OreDictionary.registerOre("stairStonebrick",Blocks.stone_brick_stairs);
        OreDictionary.registerOre("stair",Blocks.nether_brick_stairs);
        OreDictionary.registerOre("stairNetherbrick",Blocks.nether_brick_stairs);
        OreDictionary.registerOre("stair",Blocks.quartz_stairs);
        OreDictionary.registerOre("stairQuartz",Blocks.quartz_stairs);
        //wooden slabs
        OreDictionary.registerOre("slab",Blocks.wooden_slab);
        OreDictionary.registerOre("slabWood",Blocks.wooden_slab);
        //other slabs
        OreDictionary.registerOre("slab",Blocks.stone_slab);
        OreDictionary.registerOre("slabStone",new ItemStack(Blocks.stone_slab,1,0));
        OreDictionary.registerOre("slabSandstone",new ItemStack(Blocks.stone_slab,1,1));
        OreDictionary.registerOre("slabCobblestone",new ItemStack(Blocks.stone_slab,1,3));
        OreDictionary.registerOre("slabBrick",new ItemStack(Blocks.stone_slab,1,4)); //inb4 borderlands reference
        OreDictionary.registerOre("slabStonebrick",new ItemStack(Blocks.stone_slab,1,5));
        OreDictionary.registerOre("slabNetherbrick",new ItemStack(Blocks.stone_slab,1,6));
        OreDictionary.registerOre("slabQuartz",new ItemStack(Blocks.stone_slab,1,7));
        OreDictionary.registerOre("slab",Blocks.stone_slab2);
        OreDictionary.registerOre("slabSandstone",new ItemStack(Blocks.stone_slab2,1,0));
        //smooth stone variants
        OreDictionary.registerOre("stone",Blocks.stone);
        OreDictionary.registerOre("stoneGranite",new ItemStack(Blocks.stone,1,1));
        OreDictionary.registerOre("stoneDiorite",new ItemStack(Blocks.stone,1,3));
        OreDictionary.registerOre("stoneAndesite",new ItemStack(Blocks.stone,1,5));
        OreDictionary.registerOre("stoneGranitePolished",new ItemStack(Blocks.stone,1,2));
        OreDictionary.registerOre("stoneDioritePolished",new ItemStack(Blocks.stone,1,4));
        OreDictionary.registerOre("stoneAndesitePolished",new ItemStack(Blocks.stone,1,6));
        //mossy cobble
        OreDictionary.registerOre("cobblestoneMoss",Blocks.mossy_cobblestone);
        //red sandstone
        OreDictionary.registerOre("sandstone",Blocks.red_sandstone);
        //fences
        OreDictionary.registerOre("fence",Blocks.oak_fence);
        OreDictionary.registerOre("fenceWood",Blocks.oak_fence);
        OreDictionary.registerOre("fence",Blocks.spruce_fence);
        OreDictionary.registerOre("fenceWood",Blocks.spruce_fence);
        OreDictionary.registerOre("fence",Blocks.birch_fence);
        OreDictionary.registerOre("fenceWood",Blocks.birch_fence);
        OreDictionary.registerOre("fence",Blocks.jungle_fence);
        OreDictionary.registerOre("fenceWood",Blocks.jungle_fence);
        OreDictionary.registerOre("fence",Blocks.dark_oak_fence);
        OreDictionary.registerOre("fenceWood",Blocks.dark_oak_fence);
        OreDictionary.registerOre("fence",Blocks.acacia_fence);
        OreDictionary.registerOre("fenceWood",Blocks.acacia_fence);
        OreDictionary.registerOre("fence",Blocks.nether_brick_fence);
        OreDictionary.registerOre("fenceNetherbrick",Blocks.nether_brick_fence);
        //fence gates
        OreDictionary.registerOre("fencegate",Blocks.oak_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.oak_fence_gate);
        OreDictionary.registerOre("fencegate",Blocks.spruce_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.spruce_fence_gate);
        OreDictionary.registerOre("fencegate",Blocks.birch_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.birch_fence_gate);
        OreDictionary.registerOre("fencegate",Blocks.jungle_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.jungle_fence_gate);
        OreDictionary.registerOre("fencegate",Blocks.dark_oak_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.dark_oak_fence_gate);
        OreDictionary.registerOre("fencegate",Blocks.acacia_fence_gate);
        OreDictionary.registerOre("fencegateWood",Blocks.acacia_fence_gate);
        //walls
        OreDictionary.registerOre("wall",Blocks.cobblestone_wall);
        OreDictionary.registerOre("wallCobblestone",new ItemStack(Blocks.cobblestone_wall,1,0));
        OreDictionary.registerOre("wallMoss",new ItemStack(Blocks.cobblestone_wall,1,1));
    }
}
