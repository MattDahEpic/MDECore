package com.mattdahepic.mdecore.debug;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class DebugItem extends Item {
    public DebugItem () {
        super();
        this.setCreativeTab(CreativeTabs.tabAllSearch);
        this.setUnlocalizedName("debugItem");
    }
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        MinecraftForge.EVENT_BUS.post(new MDEDebugEvent.ItemRightClick(itemStackIn,worldIn,playerIn));
        return itemStackIn;
    }
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        MinecraftForge.EVENT_BUS.post(new MDEDebugEvent.BlockRightClick(stack,playerIn,worldIn,pos,side,hitX,hitY,hitZ));
        return false;
    }
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        MinecraftForge.EVENT_BUS.post(new MDEDebugEvent.HitEntity(stack,target,attacker));
        return false;
    }
}
