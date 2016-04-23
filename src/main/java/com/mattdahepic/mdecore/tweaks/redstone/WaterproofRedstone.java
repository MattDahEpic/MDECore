package com.mattdahepic.mdecore.tweaks.redstone;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WaterproofRedstone {
    public static void setup () {
        if (!MDEConfig.waterBreaksRedstone) {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);

                MDECore.logger.info("Waterproofing redstone components");
                Block[] waterproofable = new Block[]{Blocks.REDSTONE_WIRE,Blocks.POWERED_REPEATER,Blocks.UNPOWERED_REPEATER,Blocks.REDSTONE_TORCH,Blocks.UNLIT_REDSTONE_TORCH,Blocks.POWERED_COMPARATOR,Blocks.UNPOWERED_COMPARATOR,Blocks.RAIL,Blocks.ACTIVATOR_RAIL,Blocks.DETECTOR_RAIL,Blocks.GOLDEN_RAIL};
                for (Block block : waterproofable) {
                    Class c = block.getClass();
                    while (c != Block.class) {
                        c = c.getSuperclass(); //go up the class tree till we get to a point where we can access blockMaterial
                    }
                    Field f = ReflectionHelper.findField(c,"blockMaterial","field_149764_J");
                    f.setAccessible(true);
                    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    f.set(block,MDECore.waterproof_circuits);
                    MDECore.logger.debug("Waterproofed " + block.getLocalizedName());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
