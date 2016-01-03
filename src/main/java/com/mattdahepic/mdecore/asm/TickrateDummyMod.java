package com.mattdahepic.mdecore.asm;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class TickrateDummyMod extends DummyModContainer {
    public static ModMetadata makeModInfo () {
        ModMetadata md = new ModMetadata();
        md.modId = "mdecore-tickrate";
        md.name = "MDE Core - Tickrate Core Mod";
        md.version = "1.0";
        return md;
    }
    public TickrateDummyMod() {
        super(makeModInfo());
    }
}
