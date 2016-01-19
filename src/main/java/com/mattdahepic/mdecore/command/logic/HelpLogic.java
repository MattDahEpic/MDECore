package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.CommandMDE;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.command.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpLogic implements ICommandLogic {
    public static HelpLogic instance = new HelpLogic();

    @Override
    public String getCommandName () {
        return "help";
    }
    @Override
    public int getPermissionLevel () {
        return -1;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.command.help.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        switch (args.length) {
            case 1:
                StringBuilder output = new StringBuilder(TranslationHelper.getTranslatedString("mdecore.command.help.success.list.begin")+" ");
                List<String> commandList = new ArrayList<String>(CommandMDE.getCommandList());
                Collections.sort(commandList,String.CASE_INSENSITIVE_ORDER);

                int commands = 0;
                for (int i = 0; i < commandList.size() - 1; i++) { //all commands except last one
                    String name = commandList.get(i);
                    if (CommandMDE.canUseCommand(sender, CommandMDE.getCommandPermission(name), name)) {
                        output.append("/mde "+ EnumChatFormatting.YELLOW+commandList.get(i)+EnumChatFormatting.WHITE+", ");
                        commands++;
                    }
                }
                if (commands > 0) output.delete(output.length() - 2, output.length()); //delete final comma
                String name = commandList.get(commandList.size() - 1);
                if (CommandMDE.canUseCommand(sender, CommandMDE.getCommandPermission(name), name)) { //final command and formatting
                    if (commands > 0) output.append(" "+TranslationHelper.getTranslatedString("mdecore.command.help.success.list.end")+" ");
                    output.append("/mde "+EnumChatFormatting.YELLOW+name+EnumChatFormatting.WHITE+".");
                }
                sender.addChatMessage(new ChatComponentText(output.toString()));
                break;
            case 2:
                String commandName = args[1];
                if (!CommandMDE.getCommandExists(commandName)) throw new CommandNotFoundException();
                sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.help.success.usage", CommandMDE.getCommandSyntax(commandName)));
                break;
            default:
                throw new WrongUsageException(getCommandSyntax());
        }
    }
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, CommandMDE.getCommandList()); //get all possible commands to "/mde help" on
        }
        return null;
    }
}
