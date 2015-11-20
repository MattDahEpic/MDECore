package com.mattdahepic.mdecore.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FingerprintGUILoader {
    private String erroringFile;
    private static boolean hasOpenedThisLaunch = false;
    public FingerprintGUILoader (String erroringFile) {
        this.erroringFile = erroringFile;
    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void openGui(GuiOpenEvent e) {
        if (e.gui instanceof GuiMainMenu && !hasOpenedThisLaunch) {
            hasOpenedThisLaunch = true; //only open on game load
            e.gui = new GuiFingerprintWarning(erroringFile);
        }
    }
}
