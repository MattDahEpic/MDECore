package com.mattdahepic.mdecore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingSoundPlayer extends AbstractTickableSoundInstance {
    private final LocalPlayer player;
    private final String sound;
    public MovingSoundPlayer (String sound) {
        super(new SoundEvent(new ResourceLocation(sound)), SoundSource.MASTER);
        this.player = Minecraft.getInstance().player;
        this.sound = sound;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.looping = false;
    }
    public void tick () {}
}
