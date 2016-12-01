package com.mattdahepic.mdecore.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ICommandLogic {
    int getPermissionLevel ();
    String getCommandLogicName ();
    String getCommandSyntax ();
    void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;
    List<String> getTabCompletionList (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
}
