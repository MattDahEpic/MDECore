package com.mattdahepic.mdecore.helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EnchantmentHelper extends net.minecraft.enchantment.EnchantmentHelper {
    public static List<EnchantmentData> getEnchantmentsFromItem (ItemStack item) {
        if (!item.isItemEnchanted()) return null;
        final List<EnchantmentData> ret = new ArrayList<EnchantmentData>();
        net.minecraft.enchantment.EnchantmentHelper.getEnchantments(item).forEach(new BiConsumer<Enchantment, Integer>() {
            @Override
            public void accept(Enchantment ench, Integer lvl) {
                ret.add(new EnchantmentData(ench,lvl));
            }
        });
        return ret;
    }
    public static List<EnchantmentData> getEnchantmentsFromBook (ItemStack enchantedBook) {
        if (enchantedBook.getItem() != Items.ENCHANTED_BOOK) throw new RuntimeException("Attempted to retrieve enchantments on an ItemStack that isn't a book!");
        List<EnchantmentData> ret = new ArrayList<EnchantmentData>();
        NBTTagList enchantmentsRaw = Items.ENCHANTED_BOOK.getEnchantments(enchantedBook);
        for (int i = 0; i < enchantmentsRaw.tagCount(); i++) {
            NBTTagCompound enchantRaw = enchantmentsRaw.getCompoundTagAt(i);
            ret.add(new EnchantmentData(Enchantment.getEnchantmentByID(enchantRaw.getShort("id")),enchantRaw.getShort("lvl")));
        }
        return ret;
    }

    /**
     *
     * @param ench The enchantment to remove
     * @param item The item to remove the enchantment from
     * @return If the enchantment was removed
     */
    public static boolean removeEnchantment (EnchantmentData ench, ItemStack item) {
        boolean flag = false;
        for (int i = 0; i < item.getEnchantmentTagList().tagCount(); i++) {
            EnchantmentData enchantmentData = new EnchantmentData(Enchantment.getEnchantmentByID(item.getEnchantmentTagList().getCompoundTagAt(i).getShort("id")),item.getEnchantmentTagList().getCompoundTagAt(i).getShort("lvl"));
            if (enchantmentData.enchantmentobj == ench.enchantmentobj) {
                if (ench.enchantmentLevel < enchantmentData.enchantmentLevel) {
                    NBTTagCompound tag = item.getEnchantmentTagList().getCompoundTagAt(i);
                    tag.setShort("lvl",(short)(enchantmentData.enchantmentLevel-ench.enchantmentLevel));
                    item.getEnchantmentTagList().set(i,tag);
                    flag = true;
                } else if (ench.enchantmentLevel == enchantmentData.enchantmentLevel) {
                    item.getEnchantmentTagList().removeTag(i);
                    flag = true;
                }
            }
        }
        if (item.getEnchantmentTagList().tagCount() == 0) item.getTagCompound().removeTag("ench");
        return flag;
    }
    public static ItemStack getEnchantedBookWithEnchants (List<EnchantmentData> ench) {
        final ItemStack ret = new ItemStack(Items.ENCHANTED_BOOK,1,0);
        ench.forEach(new Consumer<EnchantmentData>() {
            @Override
            public void accept(EnchantmentData enchantmentData) {
                Items.ENCHANTED_BOOK.addEnchantment(ret,enchantmentData);
            }
        });
        return ret;
    }
}
