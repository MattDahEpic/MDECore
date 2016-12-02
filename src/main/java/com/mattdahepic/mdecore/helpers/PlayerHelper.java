package com.mattdahepic.mdecore.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerHelper {
    public static EntityPlayerMP getPlayerFromUsername (String username) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
    }
    public static boolean isPlayerFake (EntityPlayer player) {
        return player.world == null || !player.world.isRemote || player.getClass() != EntityPlayerMP.class || !FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().contains(player);
    }
    public static boolean isPlayerFake (EntityPlayerMP player) {
        if(player.getClass() != EntityPlayerMP.class) {
            return true;
        } else if(player.connection == null) {
            return true;
        } else {
            try {
                player.getPlayerIP().length();
                player.connection.netManager.getRemoteAddress().toString();
            } catch (Exception e) {
                return true;
            }
            return !FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().contains(player);
        }
    }
    public static boolean isPlayerReal (EntityPlayer player) {
        return !isPlayerFake(player);
    }
    public static boolean isPlayerReal (EntityPlayerMP player) {
        return !isPlayerFake(player);
    }
}
