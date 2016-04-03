package com.mattdahepic.mdecore.client;

import com.mattdahepic.mdecore.helpers.RandomHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MattRenderTweaks {
    private final int randomRender;
    public MattRenderTweaks () {
        this.randomRender = RandomHelper.randomIntInRange(0,3);
    }
    @SubscribeEvent
    public void mattRender (RenderPlayerEvent.Pre e) {
        if (e.getEntity().getName().equals("MattDahEpic")) {
            switch (randomRender) {
                case 0: //upside down
                    GlStateManager.translate(0f, e.getEntity().height + .1f, 0f);
                    GlStateManager.rotate(180f, 0f, 0f, 1f);
                    break;
                case 1: //slightly offsetting tilt
                    GlStateManager.rotate(3f, 0f, 0f, 1f);
                    break;
                case 2: //sideways
                    GlStateManager.translate(.5f, (e.getEntity().height / 2) + 0.1f, 0f);
                    GlStateManager.rotate(90f, 0f, 0f, 1f);
                    break;
                case 3: //tiny
                    GlStateManager.scale(.65f, .65f, .65f);
                    break;
            }
        }
    }
}
