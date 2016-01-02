package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TickrateHelper;

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
    public static final String USAGE = "/mde tickrate <tickrate> <all|server|client|player name>";
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
        return USAGE;
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length  == 1) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA+"Client: "+EnumChatFormatting.YELLOW+TickrateHelper.getClientTickrate()+"tps"+EnumChatFormatting.WHITE+","+EnumChatFormatting.AQUA+"Server: "+EnumChatFormatting.YELLOW+TickrateHelper.getServerTickrate()+"tps"));
        }
        try {
            float inputTicks = Float.parseFloat(args[1]);
            if (!TickrateHelper.isTickrateValid(inputTicks)) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"Invalid tickrate! Must be greater than 0."));
                return;
            }
            if (args.length < 2 || args[2].equals("all")) {
                TickrateHelper.setTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Tickrate changed to "+inputTicks+"tps."));
            } else if (args[2].equals("server")) {
                TickrateHelper.setServerTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Server tickrate changed to "+inputTicks+"tps."));
            } else if (args[2].equals("client")) {
                TickrateHelper.setAllClientTickrate(inputTicks);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"All connected players tickrate set to "+inputTicks+"tps."));
            } else {
                EntityPlayer p = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(args[2]);
                if (p == null) {
                    throw new PlayerNotFoundException();
                }
                TickrateHelper.setClientTickrate(p,inputTicks);
                sender.addChatMessage(new ChatComponentText(p.getDisplayName()+"'s client tickrate set to "+inputTicks+"tps."));
            }
        } catch (Exception ex) {
            throw new CommandException("Something went wrong! Try again.");
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
