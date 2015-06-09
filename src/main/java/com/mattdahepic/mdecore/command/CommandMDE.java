package com.mattdahepic.mdecore.command;

import com.google.common.base.Throwables;
import com.mattdahepic.mdecore.MDECore;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandMDE extends CommandBase {
    @Override
    public String getCommandName () {
        return "mde";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/mde help";
    }
    @Override
    public List getCommandAliases() {
        List aliases = new ArrayList();
        aliases.add("mde");
        return aliases;
    }
    @Override
    public void processCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException("Use /mde help to see command usage.");
        } else { //has a sub command
            if (args[0].equalsIgnoreCase("help")) {
                sender.addChatMessage(new ChatComponentText("/mde tpx {<player>|<dimension>} {<player>|<dimension>|<x><y><z>}"));
                sender.addChatMessage(new ChatComponentText("/mde tps {o|a|<dimension>}"));
            } else if (args[0].equalsIgnoreCase("tpx")) { //disclaimer: ripped straight from COFHCore, as it's not updated to 1.8
                String[] arguments = args.clone();
                switch (arguments.length) {
                    case 1: // (tpx) invalid command
                        throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
                    case 2: // (tpx {<player>|<dimension>}) teleporting player to self, or self to dimension
                        EntityPlayerMP playerSender = CommandBase.getCommandSenderAsPlayer(sender);
                        try {
                            EntityPlayerMP player = CommandBase.getPlayer(sender, arguments[1]);
                            if (!player.equals(playerSender)) {
                                if (playerSender.dimension == player.dimension) {
                                    player.setPositionAndUpdate(playerSender.posX, playerSender.posY, playerSender.posZ);
                                } else {
                                    transferPlayerToDimension(player, playerSender.dimension, playerSender.mcServer.getConfigurationManager());
                                    player.setPositionAndUpdate(playerSender.posX, playerSender.posY, playerSender.posZ);
                                }
                            } else {
                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE+"Don't teleport yourself!"));
                            }
                            break;
                        } catch (PlayerNotFoundException t) {
                            int dimension = 0;
                            try {
                                dimension = Integer.parseInt(arguments[1]);
                            } catch (Exception e) {
                                throw t;
                            }
                            if (!DimensionManager.isDimensionRegistered(dimension)) {
                                throw new CommandException("Specified world does not exist!");
                            }
                            if (playerSender.dimension != dimension) {
                                transferPlayerToDimension(playerSender, dimension, playerSender.mcServer.getConfigurationManager());
                            }
                            playerSender.setPositionAndUpdate(playerSender.posX, playerSender.posY, playerSender.posZ);
                        }
                        break;
                    case 3: // (tpx <player> {<player>|<dimension>}) teleporting player to player or player to dimension
                        EntityPlayerMP player = CommandBase.getPlayer(sender, arguments[1]);
                        try {
                            EntityPlayerMP otherPlayer = CommandBase.getPlayer(sender, arguments[2]);
                            if (!player.equals(otherPlayer)) {
                                if (otherPlayer.dimension == player.dimension) {
                                    player.setPositionAndUpdate(otherPlayer.posX, otherPlayer.posY, otherPlayer.posZ);
                                } else {
                                    transferPlayerToDimension(player, otherPlayer.dimension, otherPlayer.mcServer.getConfigurationManager());
                                    player.setPositionAndUpdate(otherPlayer.posX, otherPlayer.posY, otherPlayer.posZ);
                                }
                            } else {
                                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA+"Don't teleport to yourself!"));
                            }
                            break;
                        } catch (PlayerNotFoundException t) {
                            int dimension = 0;
                            try {
                                dimension = Integer.parseInt(arguments[2]);
                            } catch (Exception e) { // not a number, assume they wanted a player
                                throw t;
                            }
                            if (!DimensionManager.isDimensionRegistered(dimension)) {
                                throw new CommandException("Specified world does not exist!");
                            }
                            if (player.dimension != dimension) {
                                transferPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
                            }
                            player.setPositionAndUpdate(player.posX, player.posY, player.posZ);
                        }
                        break;
                    case 4: // (tpx <x> <y> <z>) teleporting self within dimension
                        playerSender = CommandBase.getCommandSenderAsPlayer(sender);
                        try {
                            playerSender.setPositionAndUpdate(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]));
                        } catch (Exception e) {
                            throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
                        }
                        break;
                    case 5: // (tpx {<player> <x> <y> <z> | <x> <y> <z> <dimension>}) teleporting player within player's dimension or self to dimension
                        try {
                            player = CommandBase.getPlayer(sender, arguments[1]);
                            try {
                                player.setPositionAndUpdate(Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
                            } catch (Exception e) {
                                throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
                            }
                        } catch (PlayerNotFoundException t) {
                            int dimension;
                            try {
                                dimension = Integer.parseInt(arguments[4]);
                            } catch (Exception e) {
                                throw t;
                            }
                            playerSender = CommandBase.getCommandSenderAsPlayer(sender);
                            if (!DimensionManager.isDimensionRegistered(dimension)) {
                                throw new CommandException("Specified world does not exist!");
                            }
                            if (playerSender.dimension != dimension) {
                                transferPlayerToDimension(playerSender, dimension, playerSender.mcServer.getConfigurationManager());
                            }
                            playerSender.setPositionAndUpdate(playerSender.posX, playerSender.posY, playerSender.posZ);
                        }
                        break;
                    case 6: // (tpx <player> <x> <y> <z> <dimension>) teleporting player to dimension and location
                    default: // ignore excess tokens. warn?
                        player = CommandBase.getPlayer(sender, arguments[1]);
                        int dimension = Integer.parseInt(arguments[5]);

                        if (!DimensionManager.isDimensionRegistered(dimension)) {
                            throw new CommandException("Specified world does not exist!");
                        }
                        if (player.dimension != dimension) {
                            transferPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
                        }
                        try {
                            player.setPositionAndUpdate(Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
                        } catch (Exception e) {
                            throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
                        }
                        break;
                }
            } else if (args[0].equalsIgnoreCase("tps")) { //credit to COFH again
                String[] arguments = args.clone();
                DecimalFormat floatfmt = new DecimalFormat("##0.00");
                if (arguments.length == 1) { //empty arguments
                    double tps = getTps(null);
                    double tickms = getTickMs(null);

                    sender.addChatMessage(new ChatComponentText("Overall: " + floatfmt.format(tps) + " TPS/" + floatfmt.format(tickms) + "MS ("
                            + (int) (tps / 20.0D * 100.0D) + "%)"));

                    for (World world : MDECore.server.worldServers) {
                        tps = getTps(world);
                        tickms = getTickMs(world);
                        sender.addChatMessage(new ChatComponentText(world.provider.getDimensionName() + " [" + world.provider.getDimensionId() + "]: "
                                + floatfmt.format(tps) + " TPS/" + floatfmt.format(tickms) + "MS (" + (int) (tps / 20.0D * 100.0D) + "%)"));
                    }
                } else if (arguments[1].toLowerCase().charAt(0) == 'o') { //overall
                    double tickms = getTickMs(null);
                    double tps = getTps(null);

                    sender.addChatMessage(new ChatComponentText("Overall server tick"));
                    sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + " TPS of " + floatfmt.format(20L) + " TPS ("
                            + (int) (tps / 20.0D * 100.0D) + "%)"));
                    sender.addChatMessage(new ChatComponentText("Tick time: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
                } else if (arguments[1].toLowerCase().charAt(0) == 'a') { //all
                    double tickms = getTickMs(null);
                    double tps = getTps(null);

                    sender.addChatMessage(new ChatComponentText("Overall server tick"));
                    sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + " TPS of " + floatfmt.format(20L) + " TPS ("
                            + (int) (tps / 20.0D * 100.0D) + "%)"));
                    sender.addChatMessage(new ChatComponentText("Tick time: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
                    int loadedChunks = 0;
                    int entities = 0;
                    int te = 0;
                    int worlds = 0;

                    for (World world : MDECore.server.worldServers) {
                        loadedChunks += world.getChunkProvider().getLoadedChunkCount();
                        entities += world.loadedEntityList.size();
                        te += world.loadedTileEntityList.size();
                        worlds += 1;
                    }
                    sender.addChatMessage(new ChatComponentText("Total Loaded Worlds/Chunks: " + worlds + "/" + loadedChunks));
                    sender.addChatMessage(new ChatComponentText("Total Entities/TileEntities: " + entities + "/" + te));
                } else { //dimension
                    int dim = 0;
                    try {
                        dim = Integer.parseInt(arguments[1]);
                    } catch (Throwable e) {
                        sender.addChatMessage(new ChatComponentText("Invalid Usage! Type /mde help for usage"));
                        Throwables.propagate(e);
                    }

                    World world = MDECore.server.worldServerForDimension(dim);
                    if (world == null) {
                        throw new CommandException("Specified world does not exist!");
                    }

                    double tickms = getTickMs(world);
                    double tps = getTps(world);

                    sender.addChatMessage(new ChatComponentText("World " + world.provider.getDimensionId() + ": " + world.provider.getDimensionName() + " - Loaded chunks: "
                            + world.getChunkProvider().getLoadedChunkCount()));
                    sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + "/" + floatfmt.format(20L) + " TPS (" + (int) (tps / 20.0D * 100.0D)
                            + "%) - Tick: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
                    sender.addChatMessage(new ChatComponentText("Entities: " + world.loadedEntityList.size() + " - Tile entities: " + world.loadedTileEntityList.size()));
                }
            }
        }
    }
    public void transferPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager) {
        //credit to COFH
        int oldDim = player.dimension;
        WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(player);
        if (player.riddenByEntity != null) {
            player.riddenByEntity.mountEntity(null);
        }
        if (player.ridingEntity != null) {
            player.mountEntity(null);
        }
        player.isDead = false;
        transferEntityToWorld(player, worldserver, worldserver1);
        manager.func_72375_a(player, worldserver);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(player, worldserver1);
        manager.syncPlayerInventory(player);
        Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();

        while (iterator.hasNext()) {
            PotionEffect potioneffect = iterator.next();
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }
    public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {
        //credit to COFH
        WorldProvider pOld = oldWorld.provider;
        WorldProvider pNew = newWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;

        oldWorld.theProfiler.startSection("placing");
        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);

        if (entity.isEntityAlive()) {
            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            newWorld.spawnEntityInWorld(entity);
            newWorld.updateEntityWithOptionalForce(entity, false);
        }

        oldWorld.theProfiler.endSection();

        entity.setWorld(newWorld);
    }
    private double getTickTimeSum(long[] times) {
        long timesum = 0L;
        if (times == null) {
            return 0.0D;
        }
        for (int i = 0; i < times.length; i++) {
            timesum += times[i];
        }

        return timesum / times.length;
    }

    private double getTickMs(World world) {
        return getTickTimeSum(world == null ? MDECore.server.tickTimeArray : MDECore.server.worldTickTimes.get(Integer.valueOf(world.provider.getDimensionId()))) * 1.0E-006D;
    }

    private double getTps(World world) {
        double tps = 1000.0D / getTickMs(world);
        return tps > 20.0D ? 20.0D : tps;
    }
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
