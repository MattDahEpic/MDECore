package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class PosLogic implements ICommandLogic {
    public static PosLogic instance = new PosLogic();

    @Override
    public String getLogicalCommandName () {
        return "pos";
    }
    @Override
    public int getPermissionLevel () {
        return 1;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde pos <player>";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            try {
                EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
                EntityPlayerMP targetPlayer = CommandBase.getPlayer(server, sender, args[1]);
                String playerName = String.format((senderPlayer.equals(targetPlayer) ? "You are" : "%s is"), targetPlayer.getDisplayNameString());
                sender.addChatMessage(new TextComponentString(playerName + " " + String.format("at the coordinates (%d, %d, %d) in the dimension %d.", (int) targetPlayer.posX, (int) targetPlayer.posY, (int) targetPlayer.posZ, targetPlayer.dimension)));
            } catch (PlayerNotFoundException e) {
                AbstractCommand.throwNoPlayer();
            }
        } else {
            AbstractCommand.throwUsages(instance);
        }
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return AbstractCommand.getPlayerNamesStartingWithLastArg(server,args);
        }
        return null;
    }
}
