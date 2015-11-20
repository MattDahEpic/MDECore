package com.mattdahepic.mdecore.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class GuiFingerprintWarning extends GuiScreen {
    private String mod;
    //Thanks to TehNut for their LaunchGui mod: https://github.com/TehNut/LaunchGui
    public GuiFingerprintWarning (String mod) {
        this.mod = mod;
    }
    @SuppressWarnings("unchecked")
    @Override
    public void initGui () {
        this.buttonList.add(new GuiButton(0,this.width/2-144,this.height/2+96,288,20,"Continue"));
    }
    @Override
    public void drawScreen (int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.fontRendererObj,"Warning!\nOne of your mods has been modified since being downloaded.\nThis could be the result of a corrupted download, the pack creator changing the mod, or something else.\nIf at all possible, please re-download the mod.\nThe mod in question is "+mod,this.width/2,this.height/2,0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void actionPerformed (GuiButton butt) {
        switch (butt.id) {
            case 0:
                for (GuiButton b : (List<GuiButton>) buttonList) b.enabled = false;
                this.mc.displayGuiScreen(null);
                break;
        }
    }
    @Override
    protected void keyTyped (char key, int keyCode) {}
}
