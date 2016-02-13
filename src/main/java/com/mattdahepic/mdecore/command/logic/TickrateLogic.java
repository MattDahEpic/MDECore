package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TickrateHelper;
import com.mattdahepic.mdecore.helpers.TranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class TickrateLogic implements ICommandLogic {
    public static TickrateLogic instance = new TickrateLogic();

    @Override
    public String getCommandName () {
        return "tickrate";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.command.tickrate.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW+TranslationHelper.getTranslatedStringFormatted("mdecore.command.tickrate.noargs", TickrateHelper.getClientTickrate(), TickrateHelper.getServerTickrate())));
            return;
        }
        try {
            float inputTicks = Float.parseFloat(args[1]);
            if (!TickrateHelper.isTickrateValid(inputTicks)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + TranslationHelper.getTranslatedString("mdecore.command.tickrate.failure.invalidtickrate")));
                return;
            }
            if (args.length == 2 || args[2].equals("all")) {
                TickrateHelper.setTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + TranslationHelper.getTranslatedStringFormatted("mdecore.command.tickrate.success.all",inputTicks)));
            } else if (args[2].equals("server")) {
                TickrateHelper.setServerTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + TranslationHelper.getTranslatedStringFormatted("mdecore.command.tickrate.success.server",inputTicks)));
            } else if (args[2].equals("client")) {
                TickrateHelper.setAllClientTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + TranslationHelper.getTranslatedStringFormatted("mdecore.command.tickrate.success.client",inputTicks)));
            } else {
                EntityPlayer p = CommandBase.getPlayer(sender,args[1]);
                TickrateHelper.setClientTickrate(p, inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+TranslationHelper.getTranslatedStringFormatted("mdecore.command.tickrate.success.name",p.getName(),inputTicks)));
            }
        } catch (NumberFormatException ne) {
            AbstractCommand.throwInvalidNumber(args[1]);
        } catch (PlayerNotFoundException e) {
            AbstractCommand.throwNoPlayer();
        } catch (Exception ex) {
            throw new CommandException(TranslationHelper.getTranslatedString("mdecore.commanderror"));
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        List<String> tab = new ArrayList<String>();
        if (args.length == 2) { // mde tickrate ...
            tab.add("0.01");
            tab.add("0.5");
            tab.add("1");
            tab.add("2");
            tab.add("5");
            tab.add("10");
            tab.add("20");
            tab.add("50");
            tab.add("100");
            tab.add("200");
            tab.add("500");
            tab.add("1000");
        } else if (args.length == 3) { // mde tickrate xx ...
            tab.add("all");
            tab.add("server");
            tab.add("client");
            for (EntityPlayer p : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
                tab.add(p.getDisplayNameString());
            }
        } else {
            tab = null;
        }
        return tab;
    }
}
