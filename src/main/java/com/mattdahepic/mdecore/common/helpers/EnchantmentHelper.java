package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Map;

public class EnchantmentHelper extends net.minecraft.enchantment.EnchantmentHelper {
    /**
     *
     * @param ench The enchantment to remove
     * @param item The item to remove the enchantment from
     * @return If the enchantment was removed
     */
    public static boolean removeEnchantment (EnchantmentData ench, ItemStack item) {
        Map<Enchantment,Integer> enchantments = getEnchantments(item);
        boolean removed = enchantments.remove(ench.enchantment) != null;
        setEnchantments(enchantments,item);
        return removed;
    }
    public static ItemStack getEnchantedBookWithEnchants (List<EnchantmentData> ench) {
        final ItemStack ret = new ItemStack(Items.ENCHANTED_BOOK,1);
        ench.forEach(enchantmentData -> ret.addEnchantment(enchantmentData.enchantment, enchantmentData.enchantmentLevel));
        return ret;
    }
}
