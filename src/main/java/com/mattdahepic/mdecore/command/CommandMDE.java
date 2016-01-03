package com.mattdahepic.mdecore.command;

import com.mattdahepic.mdecore.command.logic.*;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.*;

public class CommandMDE extends CommandBase {
    //Thanks to CoFH: https://github.com/CoFH/CoFHCore
    public static CommandMDE instance = new CommandMDE();
    private static Map<String, ICommandLogic> commands = new HashMap<String,ICommandLogic>();

    static {
        registerCommandLogic(PosLogic.instance);
        registerCommandLogic(TPSLogic.instance);
        registerCommandLogic(TPXLogic.instance);
        registerCommandLogic(HelpLogic.instance);
        registerCommandLogic(VersionLogic.instance);
        registerCommandLogic(KillAllLogic.instance);
        registerCommandLogic(PregenLogic.instance);
        registerCommandLogic(RegenLogic.instance);
        registerCommandLogic(TickrateLogic.instance);
    }

    public static void init(FMLServerStartingEvent e) {
        e.registerServerCommand(instance);
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
    public static boolean canUseCommand (ICommandSender sender, int requiredPermission, String name) {
        if (getCommandExists(name)) {
            return sender.canCommandSenderUseCommand(requiredPermission, "mde "+name) || (sender instanceof EntityPlayerMP && requiredPermission <= 0);
        }
        return false;
    }
    @Override
    public int getRequiredPermissionLevel () {
        return -1;
    }
    @Override
    public String getCommandName () {
        return "mde";
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
            if (canUseCommand(sender,command.getPermissionLevel(),command.getCommandName())) {
                command.handleCommand(sender,args);
                return;
            }
            throw new CommandException("commands.generic.permission");
        }
        throw new CommandNotFoundException();
    }
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args,commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).addTabCompletionOptions(sender,args,pos);
        }
        return null;
    }
}
