package com.mattdahepic.mdecore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovingSoundPlayer extends MovingSound {
    private final EntityPlayer player;
    private final String sound;
    public MovingSoundPlayer (String sound) {
        super(new SoundEvent(new ResourceLocation(sound)), SoundCategory.MASTER);
        this.player = Minecraft.getMinecraft().player;
        this.sound = sound;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = false;
    }
    public void update () {}
}
