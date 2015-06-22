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
            Class c = Blocks.redstone_wire.getClass();
            Field redstone_wire = c.getField("blockMaterial");
            redstone_wire.set(Blocks.redstone_wire,MDECore.waterproof_circuits);
            //repeat for other redstone type blocks, but since im probably doing it wrong i dont want to type all them up yet
        } catch (Exception e) {
            LogHelper.error(MDECore.MODID,"EVERYTHING DED, SOMETHING DOESNT EXIST");
            throw new RuntimeException(e);
        }
    }
}
