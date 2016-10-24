package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
        return "/mde version";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) {
        List<ModContainer> mods = new ArrayList<ModContainer>();
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getMetadata().authorList.contains("MattDahEpic") || mod.getMetadata().authorList.contains("mattdahepic")) {
                mods.add(mod);
            }
        }
        StringBuilder output = new StringBuilder("Loaded MattDahEpic mods are: ");
        if (mods.size() == 1) {
            ModContainer mod = mods.get(0);
            output.append(TextFormatting.YELLOW+mod.getModId()+TextFormatting.WHITE+" at version "+TextFormatting.AQUA+mod.getVersion()+TextFormatting.WHITE);
        } else {
            for (int i = 0; i < mods.size() - 1; i++) {
                output.append(TextFormatting.YELLOW + mods.get(i).getModId() + TextFormatting.WHITE + " at version " + TextFormatting.AQUA + mods.get(i).getVersion() + TextFormatting.WHITE + ", ");
            }
            output.delete(output.length() - 2, output.length()); //remove last command and space for formatting
            output.append(" and ");

            ModContainer mod = mods.get(mods.size() - 1);
            output.append(TextFormatting.YELLOW + mod.getModId() + TextFormatting.WHITE + " at version " + TextFormatting.AQUA + mod.getVersion() + TextFormatting.WHITE + ", ");
        }
        sender.addChatMessage(new TextComponentString(output.toString()));
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
