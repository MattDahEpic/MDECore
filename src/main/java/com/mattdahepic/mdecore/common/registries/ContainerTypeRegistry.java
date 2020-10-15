package com.mattdahepic.mdecore.common.registries;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;

public abstract class ContainerTypeRegistry {
    public abstract void register(RegistryEvent.Register<ContainerType<?>> event);
}
