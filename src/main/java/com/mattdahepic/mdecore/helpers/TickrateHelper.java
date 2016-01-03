package com.mattdahepic.mdecore.helpers;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.network.PacketHandler;
import com.mattdahepic.mdecore.network.TickratePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

/**
 * Credit to Guichaguri's TickrateChanger mod: https://github.com/Guichaguri/TickrateChanger
 */
public class TickrateHelper {
    private static final TickrateHelper INSTANCE = new TickrateHelper();
    public static TickrateHelper instance () {
        return INSTANCE;
    }

    public static float DEFAULT_TICKRATE = 20; //default tickrate
    public static float TICKS_PER_SECOND = 20; //client-side tickrate
    public static long MILISECONDS_PER_TICK = 50L; //server-side tickrate (in milliseconds)
    public static final float MIN_TICKRATE = 0.01f; //min tickrate (one tick every 20 seconds)
    public static final float MAX_TICKRATE = 1000; //max tickrate (one thousand ticks per second)

    /** HELPER METHODS */

    public static void setTickrate (float tickrate) {
        setAllClientTickrate(tickrate);
        setServerTickrate(tickrate);
    }
    public static void setServerTickrate (float tickrate) {
        instance().updateServerTickrate(tickrate);
    }
    public static void setAllClientTickrate (float tickrate) {
        if (EnvironmentHelper.isServer) {
            for (EntityPlayer p : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                setClientTickrate(p, tickrate);
            }
        } else {
            setClientTickrate(null,tickrate);
        }
    }
    public static void setClientTickrate (EntityPlayer p, float tickrate) {
        if (p == null || p.worldObj.isRemote) { //client
            if (FMLCommonHandler.instance().getSide() != Side.CLIENT) return; //sanity
            if (p != null && p != Minecraft.getMinecraft().thePlayer) return; //sanity
            instance().updateClientTickrate(tickrate);
        } else { //server
            PacketHandler.net.sendTo(new TickratePacket.TickrateMessage(tickrate), (EntityPlayerMP) p);
        }
    }
    public static void resetClientTickrate () {
        setClientTickrate(null,DEFAULT_TICKRATE);
    }
    public static void setClientToCurrentServerTickrate (EntityPlayer p) {
        setClientTickrate(p,getServerTickrate());
    }
    public static float getClientTickrate () {
        return TICKS_PER_SECOND;
    }
    public static long getServerTickrate () {
        return 1000L / MILISECONDS_PER_TICK;
    }

    /** DOING METHODS */

    private Field clientTimer = null;
    @SideOnly(Side.CLIENT)
    private void updateClientTickrate (float tickrate) {
        if (!isTickrateValid(tickrate)) {
            MDECore.logger.info("Ignoring invalid tickrate: "+tickrate);
            return;
        }
        if (MDEConfig.debugLogging) MDECore.logger.info("Updating client tickrate to "+tickrate);
        TICKS_PER_SECOND = tickrate;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return; //why?
        try {
            if (clientTimer == null) {
                if (MDEConfig.debugLogging) MDECore.logger.info("Creating reflection instances...");
                for (Field f : mc.getClass().getDeclaredFields()) {
                    if (f.getType() == Timer.class) {
                        clientTimer = f;
                        clientTimer.setAccessible(true);
                        break;
                    }
                }
            }
            clientTimer.set(mc,new Timer(TICKS_PER_SECOND));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void updateServerTickrate (float tickrate) {
        if (!isTickrateValid(tickrate)) {
            MDECore.logger.info("Ignoring invalid tickrate: "+tickrate);
            return;
        }
        if (MDEConfig.debugLogging) MDECore.logger.info("Updating server tickrate to "+tickrate);
        MILISECONDS_PER_TICK = (long)(1000L/tickrate);
    }

    public static boolean isTickrateValid (float tickrate) {
        return tickrate > 0f;
    }
}
