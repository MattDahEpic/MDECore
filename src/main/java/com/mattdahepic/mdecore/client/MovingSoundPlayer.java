package com.mattdahepic.mdecore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MovingSoundPlayer extends TickableSound {
    private final ClientPlayerEntity player;
    private final String sound;
    public MovingSoundPlayer (String sound) {
        super(new SoundEvent(new ResourceLocation(sound)), SoundCategory.MASTER);
        this.player = Minecraft.getInstance().player;
        this.sound = sound;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = false;
    }
    public void tick () {}
}
