package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.PlayerHelper;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

public class PosLogic implements ICommandLogic {
    public static final String USAGE = "/mde pos <player>";
    public static PosLogic instance = new PosLogic();

    @Override
    public String getCommandName () {
        return "pos";
    }
    @Override
    public int getPermissionLevel () {
        return 1;
    }
    @Override
    public String getCommandSyntax () {
        return USAGE;
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
            EntityPlayerMP targetPlayer;
            try {
                targetPlayer = CommandBase.getPlayer(sender, args[1]);
            } catch (PlayerNotFoundException e) {
                throw new CommandException("Specified player does not exist!");
            }
            String playerName = senderPlayer.equals(targetPlayer) ? "You are" : (targetPlayer.getCommandSenderName() + "is");
            int[] pos = PlayerHelper.getPlayerPosAsIntegerArray(targetPlayer);
            sender.addChatMessage(new ChatComponentText(playerName + " at the coordinates ("+pos[0]+","+pos[1]+","+pos[2]+") in the dimension "+targetPlayer.dimension+"."));
        } else {
            throw new WrongUsageException("Invalid Usage! Type /mde help "+getCommandName()+" for usage");
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return Arrays.asList(MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
