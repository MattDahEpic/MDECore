package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.PlayerHelper;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
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
            EntityPlayerMP senderPlayer = CommandBase.getCommandSenderAsPlayer(sender);
            EntityPlayerMP targetPlayer;
            try {
                targetPlayer = CommandBase.getPlayer(sender, args[1]);
            } catch (PlayerNotFoundException e) {
                throw new CommandException(TranslationHelper.getTranslatedString("mdecore.playernotfound"));
            }
            String playerName = TranslationHelper.getTranslatedStringFormatted((senderPlayer.equals(targetPlayer) ? "mdecore.command.pos.success.player.self" : "mdecore.command.pos.success.player.other"), targetPlayer.getDisplayName());
            int[] pos = PlayerHelper.getPlayerPosAsIntegerArray(targetPlayer);
            sender.addChatMessage(new ChatComponentText(playerName + " " + TranslationHelper.getTranslatedStringFormatted("mdecore.command.pos.success",targetPlayer.posX,targetPlayer.posY,targetPlayer.posZ,targetPlayer.dimension)));
        } else {
            throw new WrongUsageException(getCommandSyntax());
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return Arrays.asList(MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
