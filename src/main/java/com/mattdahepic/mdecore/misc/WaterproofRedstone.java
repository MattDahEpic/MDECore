package com.mattdahepic.mdecore.misc;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.init.Blocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WaterproofRedstone {
    public static void doIt () {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            //wire
            Class wire_class = Blocks.redstone_wire.getClass().getSuperclass();
            Field wire_material = wire_class.getDeclaredField("blockMaterial");
            wire_material.setAccessible(true);
            modifiersField.setInt(wire_material,wire_material.getModifiers() & ~Modifier.FINAL);
            wire_material.set(Blocks.redstone_wire, MDECore.waterproof_circuits);
            //repeat for other redstone type blocks, but since im probably doing it wrong i dont want to type all them up yet
        } catch (Exception e) {
            LogHelper.error(MDECore.MODID,"EVERYTHING DED, SOMETHING DOESNT EXIST");
            throw new RuntimeException(e);
        }
    }
}
