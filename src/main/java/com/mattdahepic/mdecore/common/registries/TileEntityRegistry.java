package com.mattdahepic.mdecore.common.registries;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;

public abstract class TileEntityRegistry {
    public abstract void register(RegistryEvent.Register<TileEntityType<?>> event);
}
