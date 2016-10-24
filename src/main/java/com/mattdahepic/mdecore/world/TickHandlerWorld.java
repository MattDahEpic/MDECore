package com.mattdahepic.mdecore.world;

import com.mattdahepic.mdecore.MDECore;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayDeque;
import java.util.HashMap;

public class TickHandlerWorld {
    public static TickHandlerWorld instance = new TickHandlerWorld();

    public static HashMap<Integer,ArrayDeque<ChunkPos>> chunksToPreGen = new HashMap<Integer,ArrayDeque<ChunkPos>>();
    public static HashMap<Integer,ArrayDeque<ChunkPos>> chunksToGen = new HashMap<Integer,ArrayDeque<ChunkPos>>();

    private static byte pregenC;
    @SubscribeEvent
    public void tickEnd(TickEvent.WorldTickEvent event) {

        if (event.side != Side.SERVER) return;

        World world = event.world;
        int dim = world.provider.getDimension();
        ChunkProviderServer cps = world.getMinecraftServer().worldServerForDimension(dim).getChunkProvider();
        
        ArrayDeque<ChunkPos> chunks = chunksToPreGen.get(dim);

        if (chunks != null && chunks.size() > 0) {
            ChunkPos c = chunks.pollFirst();
            pregenC &= 31;
            if (!cps.chunkExists(c.chunkXPos,c.chunkZPos) && !cps.provideChunk(c.chunkXPos,c.chunkZPos).isPopulated()) {
                MDECore.logger.info("PreGening " + c.toString() + ".");
                cps.loadChunk(c.chunkXPos,c.chunkZPos);
                cps.loadChunk(c.chunkXPos,c.chunkZPos+1);
                cps.loadChunk(c.chunkXPos+1,c.chunkZPos);
                cps.loadChunk(c.chunkXPos+1,c.chunkZPos+1);
            }
        } else if (chunks != null) {
            chunksToPreGen.remove(dim);
            cps.unloadAllChunks();
            MDECore.logger.info("PreGening complete!");
        }
    }
}
