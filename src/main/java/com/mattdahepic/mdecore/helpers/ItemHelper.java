package com.mattdahepic.mdecore.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemHelper {
    @Deprecated
    public static ItemStack getItemFromName (String modid, String item_name, int meta) {
        ItemStack ret = new ItemStack(GameRegistry.findItem(modid,item_name),1,meta);
        if (ret == null) throw new RuntimeException(String.format("The item %s:%s@%d does not exist.",modid,item_name,meta));
        return ret;
    }
    @Deprecated
    public static ItemStack getItemFromName (String name, int meta) {
        return getItemFromName(name.substring(0,name.indexOf(":")),name.substring(name.indexOf(":")+1),meta);
    }
    @Deprecated
    public static String getNameFromItemStack (ItemStack item) {
        return Item.itemRegistry.getNameForObject(item.getItem()).toString();
    }
    public static boolean isSameIgnoreStackSize (ItemStack template, ItemStack compare) {
        return template.getItem() == compare.getItem() && template.getItemDamage() == compare.getItemDamage();
    }
    //TODO: change to blockstates
}
