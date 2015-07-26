package com.mattdahepic.mdecore.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemHelper {
    public static ItemStack getItemFromName (String modid, String item_name, int meta) {
        ItemStack ret = new ItemStack(GameRegistry.findItem(modid,item_name));
        ret.setItemDamage(meta);
        ret.stackSize = 1;
        return ret;
    }
    public static ItemStack getItemFromName (String name, int meta) {
        return getItemFromName(name.substring(0,name.indexOf(":")),name.substring(name.indexOf(":")+1),meta);
    }
    public static String getNameFromItemStack (ItemStack item) {
        return (String)Item.itemRegistry.getNameForObject(item.getItem());
    }
}
