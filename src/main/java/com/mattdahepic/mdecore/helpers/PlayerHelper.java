package com.mattdahepic.mdecore.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class PlayerHelper {
    @Deprecated
    public BlockPos getPlayerPosAsBlockPos (EntityPlayer player) {
        return new BlockPos(player.posX,player.posY,player.posZ);
    }
    @Deprecated
    public static int[] getPlayerPosAsIntegerArray (EntityPlayer player) {
        int posX = MathHelper.floor_double(player.posX);
        int posY = MathHelper.floor_double(player.posY);
        int posZ = MathHelper.floor_double(player.posZ);
        return new int[]{posX,posY,posZ};
    }
    @Deprecated
    //Use World
    public static int getDistanceFromXZ (EntityPlayer player, int pointX, int pointZ) {
        int[] playerPos = getPlayerPosAsIntegerArray(player);
        int playerX = playerPos[0];
        int playerZ = playerPos[2];
        int xLength = pointX-playerX;
        int zLength = pointZ-playerZ;
        return Math.abs(MathHelper.floor_double(findHyp(xLength,zLength)));
    }
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
    @Deprecated
    public static int getDistanceFrom (EntityPlayer player, int pointX, int pointY, int pointZ) {
        int horizontalLength = getDistanceFromXZ(player,pointX,pointZ);
        int verticalLength = pointY-getPlayerPosAsIntegerArray(player)[1];
        return Math.abs(MathHelper.floor_double(findHyp(horizontalLength,verticalLength)));
    }
    @Deprecated
    private static double findHyp (double side1, double side2) {
        //c^2=a^2+b^2 aka pythagorean theorem
        return Math.sqrt((side1 * side1)+(side2*side2));
    }
}
