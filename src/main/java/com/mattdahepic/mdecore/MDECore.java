package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.command.CommandMDE;
import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.config.LoginMessage;
import com.mattdahepic.mdecore.network.PacketHandler;
import com.mattdahepic.mdecore.tweaks.WaterBottleCauldron;
import com.mattdahepic.mdecore.tweaks.redstone.MaterialWaterproofCircuits;
import com.mattdahepic.mdecore.tweaks.redstone.WaterproofRedstone;
import com.mattdahepic.mdecore.update.UpdateChecker;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
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
        FMLCommonHandler.instance().bus().register(new WaterBottleCauldron());
    }
    @Mod.EventHandler
    public static void postInit (FMLPostInitializationEvent event) {}
    @Mod.EventHandler
    public static void loadComplete (FMLLoadCompleteEvent event) {
        UpdateChecker.updateCheck(MODID, NAME, UPDATE_URL, VERSION,null);
    }
    @Mod.EventHandler
    public static void serverStarting (FMLServerStartingEvent event) {
        server = event.getServer();
        event.registerServerCommand(new CommandMDE());
    }
    @SubscribeEvent
    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent event) {
        UpdateChecker.updateCheck(MODID,NAME,UPDATE_URL,VERSION,event.player);
        LoginMessage.tell(event.player);
    }
}
