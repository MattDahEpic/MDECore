package com.mattdahepic.mdecore.misc;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.lang.reflect.Field;

public class WaterproofRedstone {
    public static void doIt () {
        try {
            Class c = Material.class;
            Field circuits = c.getDeclaredField("circuits");
            circuits.set(new Material(MapColor.airColor),MDECore.waterproof_circuits);
        } catch (Exception e) {
            LogHelper.error(MDECore.MODID,"EVERYTHING DED, circuits DOESNT EXIST");
            throw new RuntimeException(e);
        }
    }
}
