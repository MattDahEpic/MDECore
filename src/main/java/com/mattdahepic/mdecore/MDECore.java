package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.config.LoginMessage;
import com.mattdahepic.mdecore.network.PacketHandler;
import com.mattdahepic.mdecore.tweaks.DaySleepToNight;
import com.mattdahepic.mdecore.tweaks.WaterBottleCauldron;
import com.mattdahepic.mdecore.tweaks.redstone.MaterialWaterproofCircuits;
import com.mattdahepic.mdecore.tweaks.redstone.WaterproofRedstone;
import com.mattdahepic.mdecore.update.UpdateCheckerNew;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MDECore.MODID,version = MDECore.VERSION,name = MDECore.NAME,acceptedMinecraftVersions = "1.8")
public class MDECore {
    public static final String MODID = "mdecore";
    public static final String VERSION = "@VERSION@";
    public static final String NAME = "MattDahEpic Core";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/MattDahEpic/Version/master/"+ MinecraftForge.MC_VERSION+"/"+MODID+".txt";

    public static Logger logger;

    @Mod.Instance(MDECore.MODID)
    public static MDECore instance;

    public static MinecraftServer server;

    public static Material waterproof_circuits = new MaterialWaterproofCircuits(MapColor.airColor);

    public static Configuration configFile;

    @Mod.EventHandler
    public static void load (FMLPreInitializationEvent event) {
        logger = event.getModLog();
        FMLCommonHandler.instance().bus().register(instance);
        Config.load(event);
        LoginMessage.init(event.getModConfigurationDirectory());
        WaterproofRedstone.setup();
    }
    @Mod.EventHandler
    public static void init (FMLInitializationEvent event) {
        PacketHandler.initPackets();
        if (Config.waterBottlesFillCauldrons) MinecraftForge.EVENT_BUS.register(new WaterBottleCauldron());
        if (Config.sleepDuringDayChangesToNight) MinecraftForge.EVENT_BUS.register(new DaySleepToNight());
        UpdateCheckerNew.checkRemote(MODID,UPDATE_URL);
    }
    @Mod.EventHandler
    public static void postInit (FMLPostInitializationEvent event) {}
    @Mod.EventHandler
    public static void serverStarting (FMLServerStartingEvent event) {
        server = event.getServer();
    }
    @SubscribeEvent
    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent event) {
        UpdateCheckerNew.printMessageToPlayer(MODID,event.player);
        LoginMessage.tell(event.player);
    }
}
