package com.mattdahepic.mdecore.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;

/**
 * A wrapper to allow a command logic to be registered as a base level command
 *
 * The arguments passed to the logic are as follows: {command name, arguments...}
 */
public abstract class AbstractSingleLogicCommand extends CommandBase implements ICommandLogic {
    public abstract int getPermissionLevel ();
    public abstract String getCommandLogicName();
    public abstract String getCommandSyntax ();
    public abstract void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
    public abstract List<String> getTabCompletionList(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
    
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(this);
    }
    
    @Override
    public String getCommandName () {
        return getCommandLogicName();
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
        if (sender.canCommandSenderUseCommand(getPermissionLevel(), getCommandLogicName())) {
            handleCommand(server,sender, ArrayUtils.add(args,0,getCommandLogicName()));
        } else {
            throw new CommandException("commands.generic.permission");
        }
    }
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return getTabCompletionList(server,sender,ArrayUtils.add(args,0,getCommandLogicName()),pos);
    }
}
