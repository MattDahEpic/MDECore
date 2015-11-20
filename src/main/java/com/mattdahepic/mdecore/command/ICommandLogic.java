package com.mattdahepic.mdecore.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public interface ICommandLogic {
    public int getPermissionLevel ();
    public String getCommandName ();
    public String getCommandSyntax ();
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException;
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos);
}
