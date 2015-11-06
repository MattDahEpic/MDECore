package com.mattdahepic.mdecore.tweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DaySleepToNight {
    public DaySleepToNight () {}
    @SubscribeEvent
    public void playerSleep (PlayerSleepInBedEvent e) {
        e.result = EntityPlayer.EnumStatus.OK; //i will sleep whenever i want thank you very much
    }
    @SubscribeEvent
    public void playerWake (PlayerWakeUpEvent e) {
        if (e.entityPlayer.worldObj.isDaytime()) {
            e.entityPlayer.worldObj.setWorldTime(12100L); //make it night if sleep during day
        } else {
            e.entityPlayer.worldObj.setWorldTime(0L); //and day if sleep during night
        }
    }
}
