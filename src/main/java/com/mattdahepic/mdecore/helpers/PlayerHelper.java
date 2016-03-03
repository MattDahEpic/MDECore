package com.mattdahepic.mdecore.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerHelper {
    public static EntityPlayerMP getPlayerFromUsername (String username) {
        return MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(username);
    }
    public static boolean isPlayerFake (EntityPlayer player) {
        return player.worldObj == null?true:(player.worldObj.isRemote?false:(player.getClass() == EntityPlayerMP.class?false:!MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player)));
    }
    public static boolean isPlayerFake (EntityPlayerMP player) {
        if(player.getClass() != EntityPlayerMP.class) {
            return true;
        } else if(player.playerNetServerHandler == null) {
            return true;
        } else {
            try {
                player.getPlayerIP().length();
                player.playerNetServerHandler.netManager.getRemoteAddress().toString();
            } catch (Exception e) {
                return true;
            }
            return !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
        }
    }
    public static boolean isPlayerReal (EntityPlayer player) {
        return !isPlayerFake(player);
    }
    public static boolean isPlayerReal (EntityPlayerMP player) {
        return !isPlayerFake(player);
    }
}
