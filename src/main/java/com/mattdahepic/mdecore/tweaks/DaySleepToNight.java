package com.mattdahepic.mdecore.tweaks;

import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class DaySleepToNight {
    @SubscribeEvent
    public void playerSleep (PlayerSleepInBedEvent e) {
        if (MDEConfig.sleepDuringDayChangesToNight && !e.getEntityPlayer().worldObj.isRemote) { //server only
            e.setResult(EntityPlayer.EnumStatus.OK); //i will sleep whenever i want thank you very much
            if (e.getEntityPlayer().isRiding()) {e.getEntityPlayer().dismountRidingEntity();} //get off your high horse/pig/boat
            e.getEntityPlayer().setPosition((double) ((float) e.getPos().getX() + 0.5F), (double) ((float) e.getPos().getY() + 0.6875F), (double) ((float) e.getPos().getZ() + 0.5F));
            try {
                Field sleeping = ReflectionHelper.findField(EntityPlayer.class, "sleeping", "field_71083_bS");
                sleeping.setAccessible(true);
                sleeping.setBoolean(e.getEntityPlayer(), true); //EntityPlayer.sleeping = true;
                Field sleepTimer = ReflectionHelper.findField(EntityPlayer.class, "sleepTimer", "field_71076_b");
                sleepTimer.setAccessible(true);
                sleepTimer.setInt(e.getEntityPlayer(), 0); //EntityPlayer.sleepTimer = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.getEntityPlayer().playerLocation = e.getPos();
            e.getEntityPlayer().motionX = e.getEntityPlayer().motionZ = e.getEntityPlayer().motionY = 0.0D;
            e.getEntityPlayer().worldObj.updateAllPlayersSleepingFlag();
        }
    }
    @SubscribeEvent
    public void playerWake (PlayerWakeUpEvent e) {
        if (MDEConfig.sleepDuringDayChangesToNight && !e.getEntityPlayer().worldObj.isRemote) { //server only
            if (e.getEntityPlayer().worldObj.isDaytime()) {
                e.getEntityPlayer().worldObj.setWorldTime(13000L); //day -> night
            } else {
                e.getEntityPlayer().worldObj.setWorldTime(0L); //night -> day
            }
        }
    }
}
