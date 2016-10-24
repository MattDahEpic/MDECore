package com.mattdahepic.mdecore.command.logic;

import com.google.common.base.Throwables;
import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.world.TickHandlerWorld;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.List;

public class PregenLogic implements ICommandLogic {
    public static PregenLogic instance = new PregenLogic();

    @Override
    public String getCommandName () {
        return "pregen";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde pregen {<user> <x chunk radius> <z chunk radius> | <x chunk start> <z chunk start> <x chunk end> <z chunk end>}";
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 4) AbstractCommand.throwUsages(instance);
        
        World world = sender.getEntityWorld();
        if (world.isRemote) return;

        ChunkPos center = null;
        int argNum = 1;
        int xFirst, xLast;
        if ("@".equals(args[argNum])) {
            center = new ChunkPos(sender.getPosition().getX(),sender.getPosition().getZ());
            argNum++;
            xFirst = CommandBase.parseInt(args[argNum++]);
        } else {
            try {
                xFirst = CommandBase.parseInt(args[argNum++]);
            } catch (Throwable t) {
                ICommandSender senderTemp = CommandBase.getPlayer(server, sender, args[argNum - 1]);
                center = new ChunkPos(senderTemp.getPosition().getX(),senderTemp.getPosition().getZ());
                xFirst = CommandBase.parseInt(args[argNum++]);
            }
        }
        int zFirst = CommandBase.parseInt(args[argNum++]), zLast;
        int t = argNum + 1;

        try {
            xLast = CommandBase.parseInt(args[argNum++]);
            zLast = CommandBase.parseInt(args[argNum++]);
        } catch (Throwable e) {
            if (argNum > t || center == null) {
                throw Throwables.propagate(e);
            }
            --argNum;
            xLast = xFirst;
            zLast = zFirst;
        }

        if (center != null) { //determine corner locations if centered on player
            xFirst = (center.chunkXPos / 16) - xFirst;
            zFirst = (center.chunkZPos / 16) - zFirst;

            xLast = (center.chunkXPos / 16) + xLast;
            zLast = (center.chunkZPos / 16) + zLast;
        }

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

        synchronized (TickHandlerWorld.chunksToPreGen) {
            ArrayDeque<ChunkPos> chunks = TickHandlerWorld.chunksToPreGen.get(world.provider.getDimension());
            if (chunks == null) {
                chunks = new ArrayDeque<ChunkPos>();
            }

            for (int x = xFirst; x <= xLast; ++x) {
                for (int z = zFirst; z <= zLast; ++z) {
                    chunks.addLast(new ChunkPos(x, z));
                }
            }
            TickHandlerWorld.chunksToPreGen.put(world.provider.getDimension(), chunks);
        }
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 2) {
            return AbstractCommand.getPlayerNamesStartingWithLastArg(server,args);
        }
        return null;
    }
}
