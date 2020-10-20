package com.mattdahepic.mdecore.common.command;

import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.server.command.ChunkGenWorker;

import java.util.ArrayDeque;
import java.util.Queue;

class UpgradedChunkGenWorker extends ChunkGenWorker {
    private final ServerWorld dimension;

    public UpgradedChunkGenWorker(CommandSource listener, BlockPos chunkcenter, int chunksize, ServerWorld dimension) {
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
    public TextComponent getStartMessage(CommandSource sender) {
        return new TranslationTextComponent("mdecore.command.mde.generate.start",Math.sqrt(total),start.getX(),start.getZ(),dimension.dimension.location);
    }
}
