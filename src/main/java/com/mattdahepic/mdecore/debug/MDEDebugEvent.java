package com.mattdahepic.mdecore.debug;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MDEDebugEvent {
    public static class ItemRightClick extends Event {
        public final ItemStack stack;
        public final World world;
        public final EntityPlayer player;
        public ItemRightClick (ItemStack stack, World world, EntityPlayer clicker) {
            this.stack = stack;
            this.world = world;
            this.player = clicker;
        }
    }
    public static class BlockRightClick extends Event {
        public final ItemStack stack;
        public final EntityPlayer player;
        public final World world;
        public final BlockPos pos;
        public final EnumFacing facing;
        public final float hitX;
        public final float hitY;
        public final float hitZ;
        public BlockRightClick (ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
            this.stack = stack;
            this.player = playerIn;
            this.world = worldIn;
            this.pos = pos;
            this.facing = side;
            this.hitX = hitX;
            this.hitY = hitY;
            this.hitZ = hitZ;
        }
    }
    public static class HitEntity extends Event {
        public final ItemStack stack;
        public final EntityLivingBase target;
        public final EntityLivingBase attacker;
        public HitEntity (ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
            this.stack = stack;
            this.target = target;
            this.attacker = attacker;
        }
    }
}
