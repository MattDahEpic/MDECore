package com.mattdahepic.mdecore.asm;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.ModMetadata;

public class MDECoreCoreDummyMod extends DummyModContainer {
    public static ModMetadata makeModInfo () {
        ModMetadata md = new ModMetadata();
        md.modId = "mdecore-core";
        md.name = "MDE Core - ASM Tweaks";
        md.version = "1.0";
        return md;
    }
    public MDECoreCoreDummyMod() {
        super(makeModInfo());
    }
}
