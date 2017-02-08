package com.mattdahepic.mdecore.helpers;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldHelper {
    public static void dropItemsFromInventory (Random rand, IInventory inv, World world, BlockPos pos) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack item = inv.getStackInSlot(i);
            if (item != null && item.stackSize > 0) {
                EntityItem entityItem = new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),item);
                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor + 0.2F;
                world.spawnEntityInWorld(entityItem);
                inv.setInventorySlotContents(i,null);
            }
        }
    }
    public static void turnBlockToFallingSand (World world, BlockPos pos) {
        EntityFallingBlock snd = new EntityFallingBlock(world,pos.getX()+0.5,pos.getY(),pos.getZ()+0.5,world.getBlockState(pos));
        world.spawnEntityInWorld(snd);
    }
    public static double getTickTimeSum(long[] times) {
        long timesum = 0L;
        if (times == null) {
            return 0.0D;
        }
        for (int i = 0; i < times.length; i++) {
            timesum += times[i];
        }
        
        return timesum / times.length;
    }
    public static double getTickMs(MinecraftServer server, World world) {
        return getTickTimeSum(world == null ? server.tickTimeArray : server.worldTickTimes.get(Integer.valueOf(world.provider.getDimension()))) * 1.0E-006D;
    }
    
    public static double getTps(MinecraftServer server, World world) {
        double tps = 1000.0D / getTickMs(server, world);
        return tps > 20.0D ? 20.0D : tps;
    }
}
