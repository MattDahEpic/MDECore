package com.mattdahepic.mdecore.redstone;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.Config;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WaterproofRedstone {
    public static void setup () {
        if (!Config.waterBreaksRedstone) {
            try {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);

                MDECore.logger.info("Well look at that! A bottle of redstone waterproofer: Now patching redstone components");
                Block[] waterproofable = new Block[]{Blocks.redstone_wire,Blocks.powered_repeater,Blocks.unpowered_repeater,Blocks.redstone_torch,Blocks.unlit_redstone_torch,Blocks.powered_comparator,Blocks.unpowered_comparator,Blocks.rail,Blocks.activator_rail,Blocks.detector_rail,Blocks.golden_rail};
                for (Block block : waterproofable) {
                    Class c = block.getClass().getSuperclass();
                    while (!c.getTypeName().equalsIgnoreCase("net.minecraft.block.Block")) { //TODO: will this crash in non-deobf environments?
                        c = c.getSuperclass(); //go up the class tree till we get to a point where we can access blockMaterial
                    }
                    Field f = ReflectionHelper.findField(c, "blockMaterial"); //TODO: will THIS crash in non-deobf environments?
                    f.setAccessible(true);
                    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    f.set(block,MDECore.waterproof_circuits);
                    MDECore.logger.info("Waterproofed " + block.getLocalizedName());
                }
            } catch (Exception e) {
                MDECore.logger.error("well, you're screwed.");
                throw new RuntimeException(e);
            }
        } else {
            MDECore.logger.info("Redstone waterproofing is out of stock. To order some check your local config for availability.");
        }
    }
}
