package com.mattdahepic.mdecore.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Collections;
import java.util.List;

public abstract class AbstractSingleLogicCommand extends CommandBase {
    public abstract int getPermissionLevel ();
    public abstract String getCommandName ();
    public abstract String getCommandSyntax ();
    public abstract void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
    public abstract List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
    
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(this);
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        return getPermissionLevel();
    }
    @Override
    public List getCommandAliases () {
        return Collections.emptyList();
    }
    @Override
    public String getCommandUsage (ICommandSender sender) {
        return getCommandSyntax();
    }
    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {
        return true;
    }
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.canCommandSenderUseCommand(getPermissionLevel(),getCommandName())) {
            handleCommand(server,sender,args);
        } else {
            throw new CommandException("commands.generic.permission");
        }
    }
}
