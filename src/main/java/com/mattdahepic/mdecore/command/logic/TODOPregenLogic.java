package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TODOPregenLogic implements ICommandLogic {
    public static final String USAGE = "/mde pregen {<user> <x chunk radius> <z chunk radius> | <x chunk start> <z chunk start> <x chunk end> <z chunk end>}";
    public static TODOPregenLogic instance = new TODOPregenLogic();

    @Override
    public String getCommandName () {
        return "pregen";
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
        if (args.length < 4) {
            throw new WrongUsageException("Invalid Usage! Type /mde help "+getCommandName()+" for usage");
        }
        World world = sender.getEntityWorld();
        if (world.isRemote) return;

        //TODO: https://github.com/CoFH/CoFHCore/blob/master/src/main/java/cofh/core/command/CommandPregen.java
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
