package com.mattdahepic.mdecore.command.logic;

import com.google.common.base.Throwables;
import com.mattdahepic.mdecore.MDECore;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.text.DecimalFormat;

public class TPSLogic {
    public static void go (ICommandSender sender, String[] arguments) throws CommandException {
        DecimalFormat floatfmt = new DecimalFormat("##0.00");
        if (arguments.length == 1) { //empty arguments
            double tps = getTps(null);
            double tickms = getTickMs(null);

            sender.addChatMessage(new ChatComponentText("Overall: " + floatfmt.format(tps) + " TPS/" + floatfmt.format(tickms) + "MS ("
                    + (int) (tps / 20.0D * 100.0D) + "%)"));

            for (World world : MDECore.server.worldServers) {
                tps = getTps(world);
                tickms = getTickMs(world);
                sender.addChatMessage(new ChatComponentText(world.provider.getDimensionName() + " [" + world.provider.getDimensionId() + "]: "
                        + floatfmt.format(tps) + " TPS/" + floatfmt.format(tickms) + "MS (" + (int) (tps / 20.0D * 100.0D) + "%)"));
            }
        } else if (arguments[1].toLowerCase().charAt(0) == 'o') { //overall
            double tickms = getTickMs(null);
            double tps = getTps(null);

            sender.addChatMessage(new ChatComponentText("Overall server tick"));
            sender.addChatMessage(new ChatComponentText("TPS: " + floatfmt.format(tps) + " TPS of " + floatfmt.format(20L) + " TPS ("
                    + (int) (tps / 20.0D * 100.0D) + "%)"));
            sender.addChatMessage(new ChatComponentText("Tick time: " + floatfmt.format(tickms) + " ms of " + floatfmt.format(50L) + " ms"));
        } else if (arguments[1].toLowerCase().charAt(0) == 'a') { //all
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

            for (World world : MDECore.server.worldServers) {
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
                dim = Integer.parseInt(arguments[1]);
            } catch (Throwable e) {
                sender.addChatMessage(new ChatComponentText("Invalid Usage! Type /mde help for usage"));
                Throwables.propagate(e);
            }

            World world = MDECore.server.worldServerForDimension(dim);
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
        return getTickTimeSum(world == null ? MDECore.server.tickTimeArray : MDECore.server.worldTickTimes.get(Integer.valueOf(world.provider.getDimensionId()))) * 1.0E-006D;
    }

    private static double getTps(World world) {
        double tps = 1000.0D / getTickMs(world);
        return tps > 20.0D ? 20.0D : tps;
    }
}
