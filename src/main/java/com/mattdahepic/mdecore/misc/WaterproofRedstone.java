package com.mattdahepic.mdecore.misc;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.lang.reflect.Field;

public class WaterproofRedstone {
    public static void doIt () {
        try {
            //wire
            Class wire_class = Blocks.redstone_wire.getClass().getSuperclass();
            Field wire_material = wire_class.getDeclaredField("blockMaterial");
            wire_material.setAccessible(true);
            wire_material.set(Blocks.redstone_wire, MDECore.waterproof_circuits);
            System.out.println(Blocks.redstone_wire.getMaterial().toString());
            //repeat for other redstone type blocks, but since im probably doing it wrong i dont want to type all them up yet
        } catch (Exception e) {
            LogHelper.error(MDECore.MODID,"EVERYTHING DED, SOMETHING DOESNT EXIST");
            throw new RuntimeException(e);
        }
    }
}
