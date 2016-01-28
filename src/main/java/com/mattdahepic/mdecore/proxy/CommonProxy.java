package com.mattdahepic.mdecore.proxy;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.EnvironmentHelper;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void setupTextures () {}
    public void setupItems () {
        if (EnvironmentHelper.isDeobf) {
            GameRegistry.registerItem(MDECore.debugItem, "debugItem");
        }
    }
}
