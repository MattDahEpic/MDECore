package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.command.ui.PlayerInvChest;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class InvseeLogic implements ICommandLogic {
    public static InvseeLogic instance = new InvseeLogic();

    @Override
    public String getLogicalCommandName () {
        return "invsee";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde invsee <player>";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            EntityPlayer looker = CommandBase.getCommandSenderAsPlayer(sender);
            if (!looker.worldObj.isRemote) {
                EntityPlayer lookee = CommandBase.getPlayer(server,sender,args[1]);
                if (looker.getName().equals(lookee.getName())) throw new CommandException("That's you, silly!");
                looker.closeScreen();
                looker.displayGUIChest(new PlayerInvChest(lookee,looker));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            AbstractCommand.throwUsages(instance);
        } catch (PlayerNotFoundException e) {
            AbstractCommand.throwNoPlayer();
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
