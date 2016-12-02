package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class EnderchestLogic implements ICommandLogic {
    public static EnderchestLogic instance = new EnderchestLogic();

    @Override
    public String getCommandName() {
        return "enderchest";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde enderchest <player>";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            EntityPlayer looker = CommandBase.getCommandSenderAsPlayer(sender);
            if (!looker.world.isRemote) {
                EntityPlayer lookee = CommandBase.getPlayer(server,sender,args[1]);
                if (looker.getName().equals(lookee.getName())) throw new CommandException("That's you, silly!");
                looker.closeScreen();
                InventoryEnderChest enderChest = lookee.getInventoryEnderChest();
                enderChest.setCustomName(lookee.getName()+"'s Ender Chest");
                looker.displayGUIChest(enderChest);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            AbstractCommand.throwUsages(instance);
        }
    }
    @Override
    public List<String> getTabCompletionList(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return AbstractCommand.getPlayerNamesStartingWithLastArg(server,args);
        }
        return null;
    }
}
