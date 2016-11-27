package com.mattdahepic.mdecore.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.*;

public abstract class AbstractCommand extends CommandBase {
    private Map<String, ICommandLogic> commands = new HashMap<String,ICommandLogic>();

    public abstract String getCommandName ();

    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(this);
    }
    public String getCommandSyntax (String name) {
        if (getCommandExists(name)) {
            return commands.get(name).getCommandSyntax();
        }
        return null;
    }
    public boolean registerCommandLogic (ICommandLogic commandLogic) {
        if (!commands.containsKey(commandLogic.getCommandName())) {
            commands.put(commandLogic.getCommandName(), commandLogic);
            return true;
        }
        return false;
    }
    public Set<String> getCommandList() {
        return commands.keySet();
    }
    public int getCommandPermission (String command) {
        return getCommandExists(command) ? commands.get(command).getPermissionLevel() : Integer.MAX_VALUE;
    }
    public boolean getCommandExists (String command) {
        return commands.containsKey(command);
    }
    public boolean canUseCommand (ICommandSender sender, int requiredPermission, AbstractCommand baseCommand, String name) {
        if (getCommandExists(name)) {
            return sender.canCommandSenderUseCommand(requiredPermission, baseCommand.getCommandName()+" "+name) || (sender instanceof EntityPlayerMP && requiredPermission <= 0);
        }
        return false;
    }
    @Override
    public int getRequiredPermissionLevel () {
        return -1;
    }
    @Override
    public List getCommandAliases () {
        return Collections.emptyList();
    }
    @Override
    public String getCommandUsage (ICommandSender sender) {
        return "/"+getCommandName()+" help";
    }
    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {
        return true;
    }
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) args = new String[]{"help"};
        ICommandLogic command = commands.get(args[0]);
        if (command != null) {
            if (canUseCommand(sender,command.getPermissionLevel(),this,command.getCommandName())) {
                command.handleCommand(server,sender,args);
                return;
            }
            throw new CommandException("commands.generic.permission");
        }
        throwNoCommand();
    }
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).getTabCompletionOptions(server, sender, args, pos);
        }
        return null;
    }

    /* UTILITIES */

    public static List<String> getPlayerNamesStartingWithLastArg (MinecraftServer server, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getAllUsernames());
    }
    public static void throwUsages (ICommandLogic command) throws WrongUsageException {
        throw new WrongUsageException(command.getCommandSyntax());
    }
    public static void throwNoPlayer () throws PlayerNotFoundException {
        throw new PlayerNotFoundException();
    }
    public static void throwInvalidNumber (String notNumber) throws NumberInvalidException {
        throw new NumberInvalidException("commands.generic.num.invalid",notNumber);
    }
    public static void throwNoWorld () throws CommandException {
        throw new CommandException("Specified world not found!");
    }
    public static void throwNoCommand () throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }
    public static boolean doesCommandExist (MinecraftServer server, String commandName) {
        return server.commandManager.getCommands().containsKey(commandName);
    }
}
