package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.world.WorldEventHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayDeque;
import java.util.List;

public class PregenLogic implements ICommandLogic {
    public static PregenLogic instance = new PregenLogic();

    @Override
    public String getCommandName() {
        return "pregen";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde pregen help";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();
        if (world.isRemote) return;
        
        if (args.length == 1) AbstractCommand.throwUsages(instance); //only /mde pregen
        if (args[1].equals("help")) { //mde pregen help
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA+"/mde pregen start <player> <x-radius> <z-radius>"+TextFormatting.GOLD+": Generates a "+TextFormatting.LIGHT_PURPLE+"x-radius"+TextFormatting.GOLD+" by "+TextFormatting.LIGHT_PURPLE+"z-radius"+TextFormatting.GOLD+" chunk square centered on "+TextFormatting.LIGHT_PURPLE+"player"+TextFormatting.GOLD+"."));
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA+"/mde pregen start <dimension> <x-start> <z-start> <x-end> <z-end>"+TextFormatting.GOLD+": Generates a square from the chunk coordinates ("+TextFormatting.LIGHT_PURPLE+"x-start"+TextFormatting.GOLD+","+TextFormatting.LIGHT_PURPLE+"z-start"+TextFormatting.GOLD+") to ("+TextFormatting.LIGHT_PURPLE+"x-end"+TextFormatting.GOLD+","+TextFormatting.LIGHT_PURPLE+"z-end"+TextFormatting.GOLD+") in "+TextFormatting.LIGHT_PURPLE+"dimension"+TextFormatting.GOLD+"."));
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA+"/mde pregen pause"+TextFormatting.GOLD+": Pauses all currently running pregenerations. "+TextFormatting.RED+"Paused pregens are NOT saved across server restarts!"));
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA+"/mde pregen resume"+TextFormatting.GOLD+": Resumes any paused pregenerations."));
            sender.sendMessage(new TextComponentString(TextFormatting.AQUA+"/mde pregen cancel"+TextFormatting.GOLD+": Stops and removes all running and paused pregenerations for all dimensions."));
        } else if (args[1].equals("pause")) {
            WorldEventHandler.pregenPause = true;
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"Pregeneration paused!"));
            MDECore.logger.info("Pregeneration paused by command.");
        } else if (args[1].equals("resume")) {
            WorldEventHandler.pregenPause = false;
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"Pregeneration resumed!"));
            MDECore.logger.info("Pregeneration resumed by command.");
        } else if (args[1].equals("cancel")) {
            WorldEventHandler.pregenPause = true;
            WorldEventHandler.chunksToGenerate.clear();
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"All pending pregenerations canceled."));
            MDECore.logger.info("Pregeneration canceled by command.");
            WorldEventHandler.pregenPause = false;
        } else if (args[1].equals("start")) {
            if (args.length < 3) AbstractCommand.throwUsages(instance);
            int dim, xFirst, xLast, zFirst, zLast;
            if (NumberUtils.isNumber(args[2])) {
                if (args.length < 7) AbstractCommand.throwUsages(instance);
                dim = Integer.parseInt(args[2]);
                xFirst = Integer.parseInt(args[3]);
                zFirst = Integer.parseInt(args[4]);
                xLast = Integer.parseInt(args[5]);
                zLast = Integer.parseInt(args[6]);
            } else {
                if (args.length < 5) AbstractCommand.throwUsages(instance);
                EntityPlayerMP p = server.getPlayerList().getPlayerByUsername(args[2]);
                if (p == null) AbstractCommand.throwNoPlayer(args[2]);
                dim = p.dimension;
                ChunkPos center = new ChunkPos(p.chunkCoordX,p.chunkCoordZ);
                int xR = Integer.parseInt(args[3]);
                int zR = Integer.parseInt(args[4]);
                xFirst = center.chunkXPos - xR;
                zFirst = center.chunkZPos - zR;
                xLast = center.chunkXPos + xR;
                zLast = center.chunkZPos + zR;
            }
            int t;
            if (xLast < xFirst) { //sanity check
                t = xFirst;
                xFirst = xLast;
                xLast = t;
            }
            if (zLast < zFirst) { //sanity check
                t = zFirst;
                zFirst = zLast;
                zLast = t;
            }
            synchronized (WorldEventHandler.chunksToGenerate) {
                ArrayDeque<ChunkPos> chunks = WorldEventHandler.chunksToGenerate.get(dim);
                if (chunks == null) {
                    chunks = new ArrayDeque<ChunkPos>();
                }
        
                for (int x = xFirst; x <= xLast; ++x) {
                    for (int z = zFirst; z <= zLast; ++z) {
                        chunks.addLast(new ChunkPos(x, z));
                    }
                }
                WorldEventHandler.chunksToGenerate.put(dim, chunks);
            }
            sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"Pregeneration started!"));
        } else {
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
