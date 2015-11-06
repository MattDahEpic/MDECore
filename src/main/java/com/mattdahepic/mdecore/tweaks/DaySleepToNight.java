package com.mattdahepic.mdecore.tweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class DaySleepToNight {
    public DaySleepToNight () {}
    @SubscribeEvent
    public void playerSleep (PlayerSleepInBedEvent e) {
        if (!e.entityPlayer.worldObj.isRemote) { //server only
            e.result = EntityPlayer.EnumStatus.OK; //i will sleep whenever i want thank you very much
            if (e.entityPlayer.isRiding()) {e.entityPlayer.mountEntity(null);} //get off your high horse/pig/boat
            e.entityPlayer.setPosition((double) ((float) e.pos.getX() + 0.5F), (double) ((float) e.pos.getY() + 0.6875F), (double) ((float) e.pos.getZ() + 0.5F));
            try {
                Field sleeping = ReflectionHelper.findField(EntityPlayer.class, "sleeping", "field_71083_bS");
                sleeping.setAccessible(true);
                sleeping.setBoolean(e.entityPlayer, true); //EntityPlayer.sleeping = true;
                Field sleepTimer = ReflectionHelper.findField(EntityPlayer.class, "sleepTimer", "field_71076_b");
                sleepTimer.setAccessible(true);
                sleepTimer.setInt(e.entityPlayer, 0); //EntityPlayer.sleepTimer = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.entityPlayer.playerLocation = e.pos;
            e.entityPlayer.motionX = e.entityPlayer.motionZ = e.entityPlayer.motionY = 0.0D;
            if (!e.entityPlayer.worldObj.isRemote) e.entityPlayer.worldObj.updateAllPlayersSleepingFlag();
        }
    }
    @SubscribeEvent
    public void playerWake (PlayerWakeUpEvent e) {
        if (!e.entityPlayer.worldObj.isRemote) { //server only
            if (e.entityPlayer.worldObj.isDaytime()) {
                e.entityPlayer.worldObj.setWorldTime(13000L); //day -> night
            } else {
                e.entityPlayer.worldObj.setWorldTime(0L); //night -> day
            }
        }
    }
}
