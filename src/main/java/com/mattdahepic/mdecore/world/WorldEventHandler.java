package com.mattdahepic.mdecore.world;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.helpers.WorldHelper;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.HashMap;

public class WorldEventHandler {
    public static WorldEventHandler instance = new WorldEventHandler();
    
    public static HashMap<Integer,ArrayDeque<ChunkPos>> chunksToGenerate = new HashMap<Integer,ArrayDeque<ChunkPos>>();
    public static HashMap<Integer,ArrayDeque<ChunkPos>> chunksToDelete = new HashMap<Integer,ArrayDeque<ChunkPos>>();
    
    public static boolean pregenPause = false;
    private static boolean lagPause = false;
    
    @SubscribeEvent
    public void tickEnd(TickEvent.WorldTickEvent event) {
        
        if (event.side != Side.SERVER) return;
        
        World world = event.world;
        int dim = world.provider.getDimension();
        ChunkProviderServer cps = world.getMinecraftServer().worldServerForDimension(dim).getChunkProvider();
        
        ArrayDeque<ChunkPos> chunks = chunksToGenerate.get(dim);
        
        if (chunks != null && chunks.size() > 0) {
            if (WorldHelper.getTps(world.getMinecraftServer(),world) < MDEConfig.pregenMinTPS) { //stop generation if server is lagging
                if (!lagPause) {
                    MDECore.logger.info("TPS below configured minimum, pausing pregeneration.");
                    lagPause = true;
                }
            } else {
                if (lagPause) {
                    MDECore.logger.info("TPS is above configured minimum, resuming pregeneration.");
                    lagPause = false;
                }
            }
            if (!pregenPause && !lagPause) {
                ChunkPos c = chunks.pollFirst();
                if (!cps.chunkExists(c.chunkXPos, c.chunkZPos) && !cps.provideChunk(c.chunkXPos, c.chunkZPos).isPopulated()) {
                    MDECore.logger.info("PreGening " + c.toString() + ".");
                    //generate enough chunks for mc to call generate() by itself
                    cps.provideChunk(c.chunkXPos,c.chunkZPos).needsSaving(true); //(x,z)
                    cps.provideChunk(c.chunkXPos + 1, c.chunkZPos).needsSaving(true); //(x+1,z)
                    cps.provideChunk(c.chunkXPos, c.chunkZPos + 1).needsSaving(true); //(x,z+1)
                    cps.provideChunk(c.chunkXPos+1,c.chunkZPos+1).needsSaving(true); //(x+1,z+1)
                }
            }
        } else if (chunks != null) {
            chunksToGenerate.remove(dim);
            cps.unloadAllChunks();
            MDECore.logger.info("PreGening complete!");
        }
    }
    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event) {
        World world = event.getWorld();
        if (chunksToDelete.containsKey(world.provider.getDimension())) {
            ArrayDeque<ChunkPos> chunks = chunksToDelete.get(world.provider.getDimension());
            MDECore.logger.info("World "+world.provider.getDimension()+" unloaded, deleting "+chunks.size()+" chunks queued for deletion.");
            
            try {
                //reflections
                Field methodModifiers = Method.class.getDeclaredField("modifiers");
                methodModifiers.setAccessible(true);
                
                Method setOffsetMethod = findMethod(RegionFile.class, new String[]{"setOffset", "func_76711_a"}, int.class, int.class, int.class);
                setOffsetMethod.setAccessible(true);
                setOffsetMethod = RegionFile.class.getDeclaredMethod("setOffset", int.class, int.class, int.class);
                methodModifiers.set(setOffsetMethod, Modifier.PUBLIC);
                
                while (chunks != null && chunks.size() > 0) {
                    ChunkPos c = chunks.pollFirst();
                    RegionFile region = RegionFileCache.createOrLoadRegionFile(world.getSaveHandler().getWorldDirectory(), c.chunkXPos, c.chunkZPos);
                    //delete
                    if (region.chunkExists(c.chunkXPos,c.chunkZPos)) {
                        MDECore.logger.info("Deleting " + c.toString() + ".");
                        setOffsetMethod.invoke(region, c.chunkXPos, c.chunkZPos, 0); //basically tell minecraft that the location for the chunk on disk doesn't exist
                    } else {
                        MDECore.logger.info("Chunk "+c.toString()+" already deleted or does not exist, skipping.");
                    }
                }
                chunksToDelete.remove(world.provider.getDimension());
                MDECore.logger.info("Finished deleting queued chunks for world " + world.provider.getDimension() + ".");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static <E> Method findMethod(Class<? super E> clazz, String[] methodNames, Class<?>... methodTypes) {
        Exception failed = null;
        for (String methodName : methodNames) {
            try {
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            } catch (Exception e) {
                failed = e;
            }
        }
        throw new RuntimeException("Method not findable!");
    }
}