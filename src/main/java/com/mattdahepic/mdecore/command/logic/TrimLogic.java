package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.world.WorldEventHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;

public class TrimLogic implements ICommandLogic {
    public static TrimLogic instance = new TrimLogic();
    private static HashMap<Integer,ArrayDeque<ChunkPos>> tempChunks = new HashMap<Integer,ArrayDeque<ChunkPos>>();
    private static boolean hasConfirmed = false;
    
    @Override
    public String getCommandName() {
        return "trim";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde trim <keep|remove> <corner 1 chunk x> <corner 1 chunk z> <cornet 2 chunk x> <corner 2 chunk z>\n\'keep\' keeps only the specified chunks, \'remove\' removes only the specified chunks";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender.getEntityWorld().isRemote) return;
        if (args.length < 6) {
            if (args.length == 2 && args[1].equals("confirm")) {
                hasConfirmed = true;
                int dim = sender.getEntityWorld().provider.getDimension();
                sender.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "" + tempChunks.get(dim).size() + " chunks will be deleted on world unload.\n"+TextFormatting.LIGHT_PURPLE+"I hope you made a backup!"));
                
                ArrayDeque<ChunkPos> chunksToDelete = WorldEventHandler.chunksToDelete.get(dim);
                if (chunksToDelete == null) chunksToDelete = new ArrayDeque<ChunkPos>();
    
                for (ChunkPos c : tempChunks.get(dim)) {
                    chunksToDelete.add(c);
                }
                WorldEventHandler.chunksToDelete.put(dim, chunksToDelete);
                hasConfirmed = false;
                return;
            }
            AbstractCommand.throwUsages(instance);
        } else {
            if (args[1].equals("keep") || args[1].equals("remove")) {
                //dimension
                int dimension = sender.getEntityWorld().provider.getDimension();;
                WorldServer world = DimensionManager.getWorld(dimension);
                //determine the tempChunks to delete
                ArrayDeque<ChunkPos> chunksToDeleteIfConfirmed = tempChunks.get(dimension);
                if (chunksToDeleteIfConfirmed == null) chunksToDeleteIfConfirmed = new ArrayDeque<ChunkPos>();
                
                int deleteCount = 0;
                if (args[2].equals("keep")) {
                    //TODO this
                } else { //delete
                    Chunk corner1 = world.getChunkFromChunkCoords(Integer.parseInt(args[2]),Integer.parseInt(args[3]));
                    Chunk corner2 = world.getChunkFromChunkCoords(Integer.parseInt(args[4]),Integer.parseInt(args[5]));
                    for (int x = corner1.x; x < corner2.x; x++) {
                        for (int z = corner1.z; z < corner2.z; z++) {
                            chunksToDeleteIfConfirmed.add(new ChunkPos(x,z));
                            deleteCount++;
                        }
                    }
                }
                tempChunks.put(dimension,chunksToDeleteIfConfirmed);
                new java.util.Timer().schedule(new java.util.TimerTask() { //set up 10 second timer
                    @Override
                    public void run () {
                        if (!hasConfirmed) tempChunks.clear();
                    }
                },10000);
                sender.sendMessage(new TextComponentString(TextFormatting.YELLOW+"If you run "+TextFormatting.AQUA+"/mde trim confirm"+TextFormatting.YELLOW+" in the next 10 seconds, "+TextFormatting.LIGHT_PURPLE+deleteCount+TextFormatting.YELLOW+" chunks will be deleted from this world on next unload.\n"+TextFormatting.YELLOW+"MAKE A BACKUP BEFORE RUNNING THIS COMMAND."));
            } else {
                AbstractCommand.throwUsages(instance);
            }
        }
    }
    @Override
    public List<String> getTabCompletionList(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) return CommandBase.getListOfStringsMatchingLastWord(args,"keep","remove");
        return null;
    }
}
