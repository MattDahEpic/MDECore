package com.mattdahepic.mdecore.world;

import com.mattdahepic.mdecore.MDECore;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Random;

public class TickHandlerWorld {
    public static TickHandlerWorld instance = new TickHandlerWorld();

    public static HashMap<Integer,ArrayDeque<ChunkCoordIntPair>> chunksToPreGen = new HashMap<Integer,ArrayDeque<ChunkCoordIntPair>>();
    public static HashMap<Integer,ArrayDeque<ChunkCoordIntPair>> chunksToGen = new HashMap<Integer,ArrayDeque<ChunkCoordIntPair>>();

    private static byte pregenC, retroC;
    @SubscribeEvent
    public void tickEnd(TickEvent.WorldTickEvent event) {

        if (event.side != Side.SERVER) return;

        World world = event.world;
        int dim = world.provider.getDimensionId();

        if (event.phase == TickEvent.Phase.END) {
            ArrayDeque<ChunkCoordIntPair> chunks = chunksToGen.get(dim);

            if (chunks != null && chunks.size() > 0) {
                ChunkCoordIntPair c = chunks.pollFirst();
                if (retroC++ == 0 || chunks.size() < 3) {
                    MDECore.logger.info("RetroGening " + c.toString() + ".");
                } else {
                    MDECore.logger.debug("RetroGening " + c.toString() + ".");
                }
                retroC &= 31;
                long worldSeed = world.getSeed();
                Random rand = new Random(worldSeed);
                long xSeed = rand.nextLong() >> 2 + 1L;
                long zSeed = rand.nextLong() >> 2 + 1L;
                rand.setSeed(xSeed * c.chunkXPos + zSeed * c.chunkZPos ^ worldSeed);
                generateWorld(rand, c, world, false);
                chunksToGen.put(dim, chunks);
            } else if (chunks != null) {
                chunksToGen.remove(dim);
            }
        } else {
            ArrayDeque<ChunkCoordIntPair> chunks = chunksToPreGen.get(dim);

            if (chunks != null && chunks.size() > 0) {
                ChunkCoordIntPair c = chunks.pollFirst();
                if (pregenC++ == 0 || chunks.size() < 5) {
                    MDECore.logger.info("PreGening " + c.toString() + ".");
                } else {
                    MDECore.logger.debug("PreGening " + c.toString() + ".");
                }
                pregenC &= 31;
                world.getChunkFromChunkCoords(c.chunkXPos, c.chunkZPos);
            } else if (chunks != null) {
                chunksToPreGen.remove(dim);
            }
        }
    }

    public void generateWorld(Random random, ChunkCoordIntPair chunk, World world, boolean newGen) {
        if (!newGen) {
            world.getChunkFromChunkCoords(chunk.chunkXPos,chunk.chunkZPos).setChunkModified();
        }
    }
}
