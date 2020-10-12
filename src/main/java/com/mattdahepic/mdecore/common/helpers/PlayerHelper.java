package com.mattdahepic.mdecore.common.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class PlayerHelper {
    public static ServerPlayerEntity getPlayerFromUsername (String username) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(username);
    }
    public static boolean isPlayerFake (PlayerEntity player) {
        return player.world == null || !player.world.isRemote || player.getClass() != ServerPlayerEntity.class || !ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().contains(player);
    }
    public static boolean isPlayerFake (ServerPlayerEntity player) {
        if(player.getClass() != ServerPlayerEntity.class) {
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
            return !ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().contains(player);
        }
    }
    public static boolean isPlayerReal (PlayerEntity player) {
        return !isPlayerFake(player);
    }
    public static boolean isPlayerReal (ServerPlayerEntity player) {
        return !isPlayerFake(player);
    }
}
