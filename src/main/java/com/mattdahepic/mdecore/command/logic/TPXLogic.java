package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.helpers.TeleportHelper;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.DimensionManager;

public class TPXLogic {
    public static void go (ICommandSender sender, String[] arguments) throws CommandException {
        //disclaimer: ripped straight from COFHCore, as it's not updated to 1.8
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
                            TeleportHelper.transferPlayerToDimension(player, playerSender.dimension, playerSender.mcServer.getConfigurationManager());
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
                        TeleportHelper.transferPlayerToDimension(playerSender, dimension, playerSender.mcServer.getConfigurationManager());
                    }
                    TeleportHelper.sendPlayerToSpawnInCurrentWorld(playerSender);
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
                            TeleportHelper.transferPlayerToDimension(player, otherPlayer.dimension, otherPlayer.mcServer.getConfigurationManager());
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
                        TeleportHelper.transferPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
                    }
                    TeleportHelper.sendPlayerToSpawnInCurrentWorld(player);
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
                        TeleportHelper.transferPlayerToDimension(playerSender, dimension, playerSender.mcServer.getConfigurationManager());
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
                    TeleportHelper.transferPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
                }
                try {
                    player.setPositionAndUpdate(Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));
                } catch (Exception e) {
                    throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
                }
                break;
        }
    }
}
