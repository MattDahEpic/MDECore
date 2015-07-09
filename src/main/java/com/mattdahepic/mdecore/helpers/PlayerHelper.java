package com.mattdahepic.mdecore.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.List;

public class PlayerHelper {
    public BlockPos getPlayerPosAsBlockPos (EntityPlayer player) {
        return new BlockPos(player.posX,player.posY,player.posZ);
    }
    public static int[] getPlayerPosAsIntegerArray (EntityPlayer player) {
        int posX = MathHelper.floor_double(player.posX);
        int posY = MathHelper.floor_double(player.posY);
        int posZ = MathHelper.floor_double(player.posZ);
        return new int[]{posX,posY,posZ};
    }
}
