package com.mattdahepic.mdecore.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {
    public static ItemStack getItemFromName (String modid, String item_name, int meta) {
        ItemStack ret = GameRegistry.makeItemStack(modid+":"+item_name,meta,1,null);
        if (ret == null) throw new RuntimeException(String.format("The item %s:%s@%d does not exist.",modid,item_name,meta));
        return ret;
    }
    public static ItemStack getItemFromName (String name, int meta) {
        return getItemFromName(name.substring(0,name.indexOf(":")),name.substring(name.indexOf(":")+1),meta);
    }
    public static ItemStack getItemFromName (String nameAndMeta) throws NumberFormatException {
        return getItemFromName(nameAndMeta.substring(0,nameAndMeta.indexOf('@')),Integer.parseInt(nameAndMeta.substring(nameAndMeta.indexOf('@'))));
    }
    public static String getNameFromItemStack (ItemStack item) {
        return Item.REGISTRY.getNameForObject(item.getItem()).toString()+"@"+item.getMetadata();
    }
    public static boolean isSameIgnoreStackSize (ItemStack template, ItemStack compare, boolean compareNBT) {
        if ((template == null || compare == null) && !(template == null && compare == null)) return false; //if either are null but not both
        if (template == null && compare == null) return true;
        return (template.getItem() == compare.getItem()) && (template.getItemDamage() == compare.getItemDamage() || template.getItemDamage() == OreDictionary.WILDCARD_VALUE) && (compareNBT ? NBTUtil.areNBTEquals(template.getTagCompound(),compare.getTagCompound(),true) : true);
    }
    //TODO: wait until ItemStates exist or meta goes away then change to states
}
