package com.mattdahepic.mdecore.command;

import com.mattdahepic.mdecore.command.logic.PosLogic;
import com.mattdahepic.mdecore.command.logic.TPSLogic;
import com.mattdahepic.mdecore.command.logic.TPXLogic;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class CommandMDE extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    @Override
    public List getAliases() {
        List aliases = new ArrayList();
        aliases.add("mde");
        return aliases;
    }
    @Override
    public String getName () {
        return "mde";
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/mde help";
    }
    @Override
    public void execute (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new WrongUsageException("Use /mde help to see command usage.");
        } else { //has a sub command
            if (args[0].equalsIgnoreCase("help")) {
                sender.addChatMessage(new ChatComponentText("/mde tpx {<player>|<dimension>} {<player>|<dimension>|<x><y><z>}"));
                sender.addChatMessage(new ChatComponentText("/mde tps {o|a|<dimension>}"));
                sender.addChatMessage(new ChatComponentText("/mde pos <player>"));
            } else if (args[0].equalsIgnoreCase("tpx")) {
                TPXLogic.go(sender, args);
            } else if (args[0].equalsIgnoreCase("tps")) { //credit to COFH again
                TPSLogic.go(sender, args);
            } else if (args[0].equalsIgnoreCase("pos")) {
                PosLogic.go(sender, args);
            } else { //invalid command
                throw new WrongUsageException("Invalid Usage! Type /mde help for usage");
            }
        }
    }
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        System.out.println("calling tab list with args length "+args.length);
        ServerConfigurationManager manager;
        try {
            manager = CommandBase.getCommandSenderAsPlayer(sender).mcServer.getConfigurationManager();
        } catch (Exception e) {
            return null; //there will always be a player in this case, so this will never happen
        }
        switch (args.length) {
            case 1:
                List options = new ArrayList();
                options.add("help");
                options.add("tpx");
                options.add("tps");
                options.add("pos");
                return options;
            case 2:
                if (args[1].equalsIgnoreCase("tpx") || args[1].equalsIgnoreCase("pos")) { // (tpx) <player> or {pos) <player>
                    return getListOfStringsMatchingLastWord(args, manager.getAllUsernames());
                }
            case 3:
                if (args[1].equalsIgnoreCase("tpx")) { // (tpx) arg <player>
                    return getListOfStringsMatchingLastWord(args, manager.getAllUsernames());
                }
        }
        return null;
    }
}
