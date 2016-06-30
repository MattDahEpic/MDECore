package com.mattdahepic.mdecore.helpers;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportHelper {
    /*
        Credit to CoFH
     */
    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, PlayerList list) {
        if (!player.worldObj.isRemote) {
            int oldDim = player.dimension;
            WorldServer worldServerOld = list.getServerInstance().worldServerForDimension(oldDim);
            WorldServer worldServerNew = list.getServerInstance().worldServerForDimension(dimension);

            //prepare and remove player
            player.dimension = dimension;
            player.connection.sendPacket(new SPacketRespawn(dimension, worldServerNew.getDifficulty(), worldServerNew.getWorldType(), player.interactionManager.getGameType()));
            if (player.isBeingRidden()) player.dismountRidingEntity();
            if (player.isRiding()) player.getRidingEntity().dismountRidingEntity();
            worldServerOld.removeEntityDangerously(player);
            player.isDead = false;

            //move
            transferEntityToWorld(player, worldServerOld, worldServerNew);

            //sync data
            list.preparePlayer(player, worldServerOld);
            //BlockPos spawn = worldServerNew.getTopSolidOrLiquidBlock(worldServerNew.getSpawnCoordinate()); //TODO: fix crash here
            //player.playerNetServerHandler.setPlayerLocation(spawn.getX(), spawn.getY(), spawn.getZ(), player.rotationYaw, player.rotationPitch);
            player.connection.setPlayerLocation(player.posX,player.posY,player.posZ,player.rotationYaw,player.rotationPitch);
            player.interactionManager.setWorld(worldServerNew);
            list.updateTimeAndWeatherForPlayer(player, worldServerNew);
            list.syncPlayerInventory(player);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), effect));
            }

            //cleanup
            worldServerOld.resetUpdateEntityTick();
            worldServerNew.resetUpdateEntityTick();
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
        }
    }
    /*
        Credit to CoFH
     */
    public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {
        double moveFactor = oldWorld.provider.getMovementFactor() / newWorld.provider.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;

        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);

        entity.setWorld(newWorld);

        entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
        newWorld.spawnEntityInWorld(entity);
        newWorld.updateEntityWithOptionalForce(entity, false);
    }
    public static void sendPlayerToSpawnInCurrentWorld (ICommandSender sender) {
        World world = sender.getEntityWorld();
        BlockPos spawn = world.getSpawnPoint();
        double spawnX = spawn.getX() + 0.5;
        double spawnY = spawn.getY() + 0.5;
        double spawnZ = spawn.getZ() + 0.5;
        while (!world.canSeeSky(new BlockPos(spawnX,spawnY-1,spawnZ))) { //make sure ur on the surface
            spawnY++;
        }
        try {
            CommandBase.getCommandSenderAsPlayer(sender).setPositionAndUpdate(spawnX, spawnY, spawnZ);
        } catch (Exception e) {}
    }
    public static boolean isSafeLandingPosition (World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.AIR && world.getBlockState(pos.up()).getBlock() == Blocks.AIR && world.isSideSolid(pos.down(), EnumFacing.UP);
    }
}
