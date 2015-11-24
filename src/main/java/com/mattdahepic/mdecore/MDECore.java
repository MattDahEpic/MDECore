package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.command.CommandMDE;
import com.mattdahepic.mdecore.config.LoginMessage;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.network.PacketHandler;
import com.mattdahepic.mdecore.network.StatReporter;
import com.mattdahepic.mdecore.tweaks.DaySleepToNight;
import com.mattdahepic.mdecore.tweaks.WaterBottleCauldron;
import com.mattdahepic.mdecore.tweaks.redstone.MaterialWaterproofCircuits;
import com.mattdahepic.mdecore.tweaks.redstone.WaterproofRedstone;
import com.mattdahepic.mdecore.update.UpdateChecker;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MDECore.MODID,version = MDECore.VERSION,name = MDECore.NAME,dependencies = MDECore.DEPENDENCIES,acceptedMinecraftVersions = "1.8.8")
public class MDECore {
    public static final String MODID = "mdecore";
    public static final String VERSION = "@VERSION@";
    public static final String NAME = "MattDahEpic Core";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/MattDahEpic/Version/master/"+ MinecraftForge.MC_VERSION+"/"+MODID+".txt";
    public static final String DEPENDENCIES = "required-after:Forge@[1.8.8-11.14.4.1576,);";

    public static final Logger logger = LogManager.getLogger(MODID);

    @Mod.Instance(MDECore.MODID)
    public static MDECore instance;

    public static Material waterproof_circuits = new MaterialWaterproofCircuits(MapColor.airColor);

    @Mod.EventHandler
    public void load (FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(instance);
        MDEConfig.load(MODID,event,new MDEConfig());
        LoginMessage.init(event.getModConfigurationDirectory());
        WaterproofRedstone.setup();
    }
    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        PacketHandler.initPackets();
        if (MDEConfig.waterBottlesFillCauldrons) MinecraftForge.EVENT_BUS.register(new WaterBottleCauldron());
        if (MDEConfig.sleepDuringDayChangesToNight) MinecraftForge.EVENT_BUS.register(new DaySleepToNight());
        UpdateChecker.checkRemote(MODID, UPDATE_URL);
    }
    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        if (MDEConfig.reportUsageStats) StatReporter.gatherAndReport();
    }
    @Mod.EventHandler
    public void serverStarting (FMLServerStartingEvent e) {
        CommandMDE.init(e);
    }
    @SubscribeEvent
    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent event) {
        UpdateChecker.printMessageToPlayer(MODID, event.player);
        LoginMessage.tell(event.player);
    }
}
