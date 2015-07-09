package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.helpers.PlayerHelper;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class PosLogic {
    public static void go (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
            EntityPlayerMP targetPlayer;
            try {
                targetPlayer = CommandBase.getPlayer(sender, args[1]);
            } catch (PlayerNotFoundException e) {
                throw new WrongUsageException("Specified player does not exist!");
            }
            String playerName = "";
            if (senderPlayer.equals(targetPlayer)) {
                playerName = "You are";
            } else {
                playerName = targetPlayer.getName()+" is";
            }
            int[] pos = PlayerHelper.getPlayerPosAsIntegerArray(targetPlayer);
            sender.addChatMessage(new ChatComponentText(playerName + " at the coordinates ("+pos[0]+","+pos[1]+","+pos[2]+") in the dimension "+targetPlayer.dimension+"."));
        } else {
            throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
        }
    }
}
