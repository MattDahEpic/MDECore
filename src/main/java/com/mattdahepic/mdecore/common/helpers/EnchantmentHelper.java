package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class EnchantmentHelper extends net.minecraft.world.item.enchantment.EnchantmentHelper {
    /**
     *
     * @param ench The enchantment to remove
     * @param item The item to remove the enchantment from
     * @return If the enchantment was removed
     */
    public static boolean removeEnchantment (EnchantmentInstance ench, ItemStack item) {
        Map<Enchantment,Integer> enchantments = getEnchantments(item);
        boolean removed = enchantments.remove(ench.enchantment) != null;
        setEnchantments(enchantments,item);
        return removed;
    }
    public static ItemStack getEnchantedBookWithEnchants (List<EnchantmentInstance> ench) {
        final ItemStack ret = new ItemStack(Items.ENCHANTED_BOOK,1);
        ench.forEach(enchantmentData -> ret.enchant(enchantmentData.enchantment, enchantmentData.level));
        return ret;
    }
}
