package com.mattdahepic.mdecore.misc;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WaterproofRedstone {
    public static void doIt () {
        if (!Config.waterBreaksRedstone) {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);

                LogHelper.info(MDECore.MODID,"Disabling water breaking redstone components.");
                Object[] waterproofable = new Object[]{Blocks.redstone_wire,Blocks.powered_repeater,Blocks.unpowered_repeater,Blocks.redstone_torch,Blocks.unlit_redstone_torch,Blocks.powered_comparator,Blocks.unpowered_comparator};
                for (Object obj : waterproofable) {
                    Class c = ((Block) obj).getClass().getSuperclass();
                    while (!c.getTypeName().equalsIgnoreCase("net.minecraft.block.Block")) {
                        c = c.getSuperclass();
                    }
                    Field f = c.getDeclaredField("blockMaterial");
                    f.setAccessible(true);
                    modifiersField.setInt(f,f.getModifiers() & ~Modifier.FINAL);
                    f.set(obj,MDECore.waterproof_circuits);
                    LogHelper.info(MDECore.MODID,"Patched "+ ((Block) obj).getLocalizedName());
                }
            } catch (Exception e) {
                LogHelper.error(MDECore.MODID, "EVERYTHING DED, SOMETHING DOESNT EXIST");
                throw new RuntimeException(e);
            }
        }
    }
}
