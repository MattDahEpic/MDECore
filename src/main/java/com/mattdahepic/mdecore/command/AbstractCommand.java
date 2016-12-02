package com.mattdahepic.mdecore.command;

import com.mattdahepic.mdecore.MDECore;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.*;

/**
 * A command that can have several sub commands
 *
 * The arguments passed to the logic are as follows: {command name, arguments...}
 */
public abstract class AbstractCommand extends CommandBase {
    private Map<String, ICommandLogic> commands = new HashMap<String,ICommandLogic>();

    public abstract String getName ();

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
        MDECore.logger.debug("Registering command "+commandLogic.getClass().getName());
        try {
            if (!commands.containsKey(commandLogic.getCommandName())) {
                commands.put(commandLogic.getCommandName(), commandLogic);
                return true;
            }
            return false;
        } catch (Error e) {
            MDECore.logger.fatal("Error initializing command "+commandLogic.getClass().getName()+". Please report this to the mod author.");
            throw new RuntimeException(e);
        }
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
            return sender.canUseCommand(requiredPermission, baseCommand.getName()+" "+name) || (sender instanceof EntityPlayerMP && requiredPermission <= 0);
        }
        return false;
    }
    @Override
    public int getRequiredPermissionLevel () {
        return -1;
    }
    @Override
    public List getAliases () {
        return Collections.emptyList();
    }
    @Override
    public String getUsage (ICommandSender sender) {
        return "/"+getName()+" help";
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
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).getTabCompletionList(server, sender, args, pos);
        }
        return null;
    }

    /* UTILITIES */

    public static List<String> getPlayerNamesStartingWithLastArg (MinecraftServer server, String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, server.getPlayerList().getOnlinePlayerNames());
    }
    public static void throwUsages (ICommandLogic command) throws WrongUsageException {
        throw new WrongUsageException(command.getCommandSyntax());
    }
    public static void throwNoPlayer (String name) throws PlayerNotFoundException {
        throw new PlayerNotFoundException(name);
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
