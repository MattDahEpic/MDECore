package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TPSLogic implements ICommandLogic {
    public static TPSLogic instance = new TPSLogic();

    @Override
    public String getCommandName () {
        return "tps";
    }
    @Override
    public int getPermissionLevel () {
        return 0;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.ocmmand.tps.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        DecimalFormat floatfmt = new DecimalFormat("##0.00");
        if (args.length == 1) { //empty arguments
            double tps = getTps(null);
            double tickms = getTickMs(null);

            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.noargs.overall", tps, tickms, (int) (tps / 20D * 100D)));

            for (World world : MinecraftServer.getServer().worldServers) {
                tps = getTps(world);
                tickms = getTickMs(world);
                sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.noargs.world",world.provider.getDimensionName(),world.provider.getDimensionId(),tps,tickms,(int)(tps/20D*100D)));
            }
        } else if (args[1].toLowerCase().charAt(0) == 'o') { //overall
            double tickms = getTickMs(null);
            double tps = getTps(null);

            sender.addChatMessage(TranslationHelper.getTranslatedChat("mdecore.command.tps.success.overall.title"));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.overall.tps", tps, 20L, (int) (tps / 20D * 100D)));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.overall.time",tickms,50L));
        } else if (args[1].toLowerCase().charAt(0) == 'a') { //all
            double tickms = getTickMs(null);
            double tps = getTps(null);

            sender.addChatMessage(TranslationHelper.getTranslatedChat("mdecore.command.tps.success.overall.title"));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.overall.tps", tps, 20L, (int) (tps / 20D * 100D)));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.overall.time",tickms,50L));

            int loadedChunks = 0;
            int entities = 0;
            int te = 0;
            int worlds = 0;

            for (World world : MinecraftServer.getServer().worldServers) {
                loadedChunks += world.getChunkProvider().getLoadedChunkCount();
                entities += world.loadedEntityList.size();
                te += world.loadedTileEntityList.size();
                worlds += 1;
            }
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.all.world",worlds,loadedChunks));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.all.entity",entities,te));
        } else { //dimension
            int dim = 0;
            try {
                dim = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new CommandException(TranslationHelper.getTranslatedString("mdecore.numberformatex"));
            }

            World world = MinecraftServer.getServer().worldServerForDimension(dim);
            if (world == null) {
                throw new CommandException(TranslationHelper.getTranslatedString("mdecore.worldnotfound"));
            }

            double tickms = getTickMs(world);
            double tps = getTps(world);

            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.dimension.world",world.provider.getDimensionId(),world.provider.getDimensionName(),world.getChunkProvider().getLoadedChunkCount()));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.dimension.time",tps,20L,(int)(tps/20D*100D),tickms,50L));
            sender.addChatMessage(TranslationHelper.getTranslatedChatFormatted("mdecore.command.tps.success.dimension.entity",world.loadedEntityList.size(),world.loadedTileEntityList.size()));
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            List<String> worldIDs = new ArrayList<String>();
            worldIDs.add("o");
            worldIDs.add("a");
            for (World world : MinecraftServer.getServer().worldServers) {
                worldIDs.add(Integer.toString(world.provider.getDimensionId()));
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, worldIDs.toArray(new String[]{""}));
        }
        return null;
    }
    private static double getTickTimeSum(long[] times) {
        long timesum = 0L;
        if (times == null) {
            return 0.0D;
        }
        for (int i = 0; i < times.length; i++) {
            timesum += times[i];
        }

        return timesum / times.length;
    }
    private static double getTickMs(World world) {
        return getTickTimeSum(world == null ? MinecraftServer.getServer().tickTimeArray : MinecraftServer.getServer().worldTickTimes.get(Integer.valueOf(world.provider.getDimensionId()))) * 1.0E-006D;
    }

    private static double getTps(World world) {
        double tps = 1000.0D / getTickMs(world);
        return tps > 20.0D ? 20.0D : tps;
    }
}
