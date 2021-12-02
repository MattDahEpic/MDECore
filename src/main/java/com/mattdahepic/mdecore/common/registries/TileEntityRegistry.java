package com.mattdahepic.mdecore.common.registries;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;

public abstract class TileEntityRegistry {
    public abstract void register(RegistryEvent.Register<BlockEntityType<?>> event);
}
