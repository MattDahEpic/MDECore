package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class PosLogic implements ICommandLogic {
    public static PosLogic instance = new PosLogic();

    @Override
    public String getCommandName () {
        return "pos";
    }
    @Override
    public int getPermissionLevel () {
        return 1;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.command.pos.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            try {
                EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
                EntityPlayerMP targetPlayer = CommandBase.getPlayer(sender, args[1]);
                String playerName = TranslationHelper.getTranslatedStringFormatted((senderPlayer.equals(targetPlayer) ? "mdecore.command.pos.success.player.self" : "mdecore.command.pos.success.player.other"), targetPlayer.getDisplayNameString());
                sender.addChatMessage(new ChatComponentText(playerName + " " + TranslationHelper.getTranslatedStringFormatted("mdecore.command.pos.success", (int) targetPlayer.posX, (int) targetPlayer.posY, (int) targetPlayer.posZ, targetPlayer.dimension)));
            } catch (PlayerNotFoundException e) {
                AbstractCommand.throwNoPlayer();
            }
        } else {
            AbstractCommand.throwUsages(instance);
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return AbstractCommand.getPlayerNamesStartingWithLastArg(args);
        }
        return null;
    }
}
