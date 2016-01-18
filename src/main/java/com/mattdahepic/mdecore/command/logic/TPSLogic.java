package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
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

            sender.addChatMessage(new ChatComponentText(TranslationHelper.getTranslatedStringFormatted("mdecore.command.tps.success.noargs.overall",floatfmt.format(tps),floatfmt.format(tickms),(int)(tps/20D*100D))));

            for (World world : MinecraftServer.getServer().worldServers) {
                tps = getTps(world);
                tickms = getTickMs(world);
                sender.addChatMessage(new ChatComponentText(TranslationHelper.getTranslatedStringFormatted("mdecore.command.tps.success.noargs.world",world.provider.getDimensionName(),world.provider.getDimensionId(),floatfmt.format(tps),floatfmt.format(tickms),(int)(tps/20D*100D))));
            }
        } else if (args[1].toLowerCase().charAt(0) == 'o') { //overall
            double tickms = getTickMs(null);
            double tps = getTps(null);

            sender.addChatMessage(new ChatComponentText(TranslationHelper.getTranslatedString("mdecore.command.tps.success.overall.title")));
            sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + " TPS of " + floatfmt.format(20L) + " TPS ("
                    + (int) (tps / 20.0D * 100.0D) + "%)"));
            sender.addChatMessage(new ChatComponentText("Tick time: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
        } else if (args[1].toLowerCase().charAt(0) == 'a') { //all
            double tickms = getTickMs(null);
            double tps = getTps(null);

            sender.addChatMessage(new ChatComponentText("Overall server tick"));
            sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + " TPS of " + floatfmt.format(20L) + " TPS ("
                    + (int) (tps / 20.0D * 100.0D) + "%)"));
            sender.addChatMessage(new ChatComponentText("Tick time: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
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
            sender.addChatMessage(new ChatComponentText("Total Loaded Worlds/Chunks: " + worlds + "/" + loadedChunks));
            sender.addChatMessage(new ChatComponentText("Total Entities/TileEntities: " + entities + "/" + te));
        } else { //dimension
            int dim = 0;
            try {
                dim = Integer.parseInt(args[1]);
            } catch (Throwable e) {
                throw new WrongUsageException("Invalid Usage! Type /mde help "+getCommandName()+" for usage");
            }

            World world = MinecraftServer.getServer().worldServerForDimension(dim);
            if (world == null) {
                throw new CommandException("Specified world does not exist!");
            }

            double tickms = getTickMs(world);
            double tps = getTps(world);

            sender.addChatMessage(new ChatComponentText("World " + world.provider.getDimensionId() + ": " + world.provider.getDimensionName() + " - Loaded chunks: "
                    + world.getChunkProvider().getLoadedChunkCount()));
            sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + "/" + floatfmt.format(20L) + " TPS (" + (int) (tps / 20.0D * 100.0D)
                    + "%) - Tick: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
            sender.addChatMessage(new ChatComponentText("Entities: " + world.loadedEntityList.size() + " - Tile entities: " + world.loadedTileEntityList.size()));
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
