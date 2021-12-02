package com.mattdahepic.mdecore.common.registries;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;

public abstract class ContainerTypeRegistry {
    public abstract void register(RegistryEvent.Register<MenuType<?>> event);
}
