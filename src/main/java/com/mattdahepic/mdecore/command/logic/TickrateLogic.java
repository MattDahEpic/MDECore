package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TickrateHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
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
        return "/mde tickrate <tickrate> <all|server|client|player name>";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            sender.addChatMessage(new TextComponentString(TextFormatting.YELLOW+String.format("Client: %dtps | Server: %dtps", TickrateHelper.getClientTickrate(), TickrateHelper.getServerTickrate())));
            return;
        }
        try {
            float inputTicks = Float.parseFloat(args[1]);
            if (!TickrateHelper.isTickrateValid(inputTicks)) {
                sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Invalid tickrate, must be greater than 0. You can't just go around stopping time!"));
                return;
            }
            if (args.length == 2 || args[2].equals("all")) {
                TickrateHelper.setTickrate(server,inputTicks);
                sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + String.format("Tickrate changed to %dtps.", inputTicks)));
            } else if (args[2].equals("server")) {
                TickrateHelper.setServerTickrate(inputTicks);
                sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + String.format("Server tickrate changed to %dtps.",inputTicks)));
            } else if (args[2].equals("client")) {
                TickrateHelper.setAllClientTickrate(server,inputTicks);
                sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + String.format("All connected players tickrate set to %dtps.",inputTicks)));
            } else {
                EntityPlayer p = CommandBase.getPlayer(server,sender,args[1]);
                TickrateHelper.setClientTickrate(p, inputTicks);
                sender.addChatMessage(new TextComponentString(TextFormatting.GREEN+String.format("%s's client tickrate set to %dtps.",p.getName(),inputTicks)));
            }
        } catch (NumberFormatException ne) {
            AbstractCommand.throwInvalidNumber(args[1]);
        } catch (PlayerNotFoundException e) {
            AbstractCommand.throwNoPlayer();
        } catch (Exception ex) {
            throw new CommandException("Something went wrong! Try again.");
        }
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
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
            tab.addAll(Arrays.asList(server.getAllUsernames()));
        } else {
            tab = null;
        }
        return tab;
    }
}
