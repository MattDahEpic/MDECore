package com.mattdahepic.mdecore.common.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.server.command.ChunkGenWorker;

import java.util.ArrayDeque;
import java.util.Queue;

class UpgradedChunkGenWorker extends ChunkGenWorker {
    private final ServerLevel dimension;

    public UpgradedChunkGenWorker(CommandSourceStack listener, BlockPos chunkcenter, int chunksize, ServerLevel dimension) {
        super(listener,chunkcenter,chunksize*chunksize,dimension,-1);
        this.dimension = dimension;
    }

    @Override
    protected Queue<BlockPos> buildQueue() {
        Queue<BlockPos> ret = new ArrayDeque<>();
        ret.add(start);

        int radius = (int) (Math.sqrt(total)/2);
        for (int x = start.getX()-radius; x < start.getX()+radius; x++) {
            for (int z = start.getZ()-radius; z < start.getZ()+radius; z++) {
                ret.add(new BlockPos(x,0,z));
            }
        }
        return ret;
    }

    @Override
    public BaseComponent getStartMessage(CommandSourceStack sender) {
        return new TranslatableComponent("mdecore.command.mde.generate.start",Math.sqrt(total),start.getX(),start.getZ(),dimension.dimension.location);
    }
}
