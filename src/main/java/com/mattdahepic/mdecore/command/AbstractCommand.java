package com.mattdahepic.mdecore.command;

import com.mattdahepic.mdecore.helpers.TranslationHelper;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.*;

public abstract class AbstractCommand extends CommandBase {
    private static Map<String, ICommandLogic> commands = new HashMap<String,ICommandLogic>();

    public abstract String getCommandName ();

    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(this);
    }
    public static String getCommandSyntax (String name) {
        if (getCommandExists(name)) {
            return commands.get(name).getCommandSyntax();
        }
        return null;
    }
    public static boolean registerCommandLogic (ICommandLogic commandLogic) {
        if (!commands.containsKey(commandLogic.getCommandName())) {
            commands.put(commandLogic.getCommandName(), commandLogic);
            return true;
        }
        return false;
    }
    public static Set<String> getCommandList() {
        return commands.keySet();
    }
    public static int getCommandPermission (String command) {
        return getCommandExists(command) ? commands.get(command).getPermissionLevel() : Integer.MAX_VALUE;
    }
    public static boolean getCommandExists (String command) {
        return commands.containsKey(command);
    }
    public static boolean canUseCommand (ICommandSender sender, int requiredPermission, AbstractCommand baseCommand, String name) {
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
    public boolean canCommandSenderUseCommand (ICommandSender sender) {
        return true;
    }
    @Override
    public void processCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) args = new String[]{"help"};
        ICommandLogic command = commands.get(args[0]);
        if (command != null) {
            if (canUseCommand(sender,command.getPermissionLevel(),this,command.getCommandName())) {
                command.handleCommand(sender,args);
                return;
            }
            throw new CommandException("commands.generic.permission");
        }
        throwNoCommand();
    }
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).addTabCompletionOptions(sender, args, pos);
        }
        return null;
    }

    /* UTILITIES */

    public static List<String> getPlayerNamesStartingWithLastArg (String[] args) {
        return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
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
        throw new CommandException(TranslationHelper.getTranslatedString("mdecore.worldnotfound"));
    }
    public static void throwNoCommand () throws CommandNotFoundException {
        throw new CommandNotFoundException();
    }
}
