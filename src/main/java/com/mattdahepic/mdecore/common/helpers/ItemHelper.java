package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemHelper {
    public static ItemStack getItemFromName (String modid, String item_name, int meta) {
        ItemStack ret = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(modid,item_name)));
        return ret;
    }
    public static ItemStack getItemFromName (String name, int meta) {
        return getItemFromName(name.substring(0,name.indexOf(":")),name.substring(name.indexOf(":")+1),meta);
    }
    public static ItemStack getItemFromName (String nameAndMeta) throws NumberFormatException {
        return getItemFromName(nameAndMeta.substring(0,nameAndMeta.indexOf('@')),Integer.parseInt(nameAndMeta.substring(nameAndMeta.indexOf('@')+1)));
    }
    public static String getNameFromItemStack (ItemStack item) {
        return GameRegistry.findRegistry(Item.class).getKey(item.getItem()).toString();
    }
    public static boolean isSameIgnoreStackSize (ItemStack template, ItemStack compare, boolean compareNBT) {
        if (template == null && compare == null) return true;
        if ((template == null || compare == null) && !(template == null && compare == null)) return false; //if either are null but not both
        return (template.getItem() == compare.getItem()) &&  (!compareNBT || NBTUtil.areNBTEquals(template.getTag(), compare.getTag(), true));
    }
    public static boolean isSameIgnoreStackSize (ItemStack template, ItemStack compare) {
        return isSameIgnoreStackSize(template,compare,false);
    }
}
