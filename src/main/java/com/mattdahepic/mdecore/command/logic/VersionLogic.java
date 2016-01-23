package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.ArrayList;
import java.util.List;

public class VersionLogic implements ICommandLogic {
    public static VersionLogic instance = new VersionLogic();

    @Override
    public String getCommandName () {
        return "version";
    }
    @Override
    public int getPermissionLevel () {
        return 0;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.command.version.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) {
        List<ModContainer> mods = new ArrayList<ModContainer>();
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getMetadata().authorList.contains("MattDahEpic") || mod.getMetadata().authorList.contains("mattdahepic")) {
                mods.add(mod);
            }
        }
        StringBuilder output = new StringBuilder(TranslationHelper.getTranslatedString("mdecore.command.version.success.title")+" ");
        if (mods.size() == 1) {
            ModContainer mod = mods.get(0);
            output.append(EnumChatFormatting.YELLOW+mod.getModId()+EnumChatFormatting.WHITE+" at version "+EnumChatFormatting.AQUA+mod.getVersion()+EnumChatFormatting.WHITE);
        } else {
            for (int i = 0; i < mods.size() - 1; i++) {
                output.append(EnumChatFormatting.YELLOW + mods.get(i).getModId() + EnumChatFormatting.WHITE + " at version " + EnumChatFormatting.AQUA + mods.get(i).getVersion() + EnumChatFormatting.WHITE + ", ");
            }
            output.delete(output.length() - 2, output.length()); //remove last command and space for formatting
            output.append(" and ");

            ModContainer mod = mods.get(mods.size() - 1);
            output.append(EnumChatFormatting.YELLOW + mod.getModId() + EnumChatFormatting.WHITE + " at version " + EnumChatFormatting.AQUA + mod.getVersion() + EnumChatFormatting.WHITE + ", ");
        }
        sender.addChatMessage(new ChatComponentText(output.toString()));
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
