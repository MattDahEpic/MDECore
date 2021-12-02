package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlayerHelper {
    public static ServerPlayer getPlayerFromUsername (String username) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(username);
    }
    public static boolean isPlayerFake (Player player) {
        return player.level == null || !player.level.isClientSide || player.getClass() != ServerPlayer.class || !ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().contains(player);
    }
    public static boolean isPlayerFake (ServerPlayer player) {
        if(player.getClass() != ServerPlayer.class) {
            return true;
        } else if(player.connection == null) {
            return true;
        } else {
            try {
                player.getIpAddress().length();
                player.connection.connection.getRemoteAddress().toString();
            } catch (Exception e) {
                return true;
            }
            return !ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().contains(player);
        }
    }
    public static boolean isPlayerReal (Player player) {
        return !isPlayerFake(player);
    }
    public static boolean isPlayerReal (ServerPlayer player) {
        return !isPlayerFake(player);
    }
}
