package com.mattdahepic.mdecore.common.registries;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ItemRegistry {
    public abstract void register (RegistryEvent.Register<Item> event);

    public Item registerItem(Item item, String name) {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
