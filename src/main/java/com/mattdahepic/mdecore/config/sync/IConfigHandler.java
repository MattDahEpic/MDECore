package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

public interface IConfigHandler {
    void initialize(FMLPreInitializationEvent e);

    List<ConfigSyncable.Section> getSections();

    ConfigCategory getCategory(String name);

    String getConfigName();
}
