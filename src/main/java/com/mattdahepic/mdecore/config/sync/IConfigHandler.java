package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IConfigHandler {
    void initialize(FMLPreInitializationEvent e);

    String getConfigName();
}
