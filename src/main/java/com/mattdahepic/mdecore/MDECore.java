package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.command.CommandMDE;
import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.config.LoginMessage;
import com.mattdahepic.mdecore.update.UpdateChecker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod(modid = MDECore.MODID,version = MDECore.VERSION,name = MDECore.NAME,acceptedMinecraftVersions = "1.8")
public class MDECore {
    public static final String MODID = "mdecore";
    public static final String VERSION = "@VERSION@";
    public static final String NAME = "MattDahEpic Core";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/MattDahEpic/MDECore1.8/master/version.txt";

    @Mod.Instance(MDECore.MODID)
    public static MDECore instance;

    public static MinecraftServer server;

    //sided proxy

    public static Configuration configFile;

    @Mod.EventHandler
    public static void load (FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(instance);
        Config.load(event);
        LoginMessage.init(event.getModConfigurationDirectory());
    }
    @Mod.EventHandler
    public static void init (FMLInitializationEvent event) {}
    @Mod.EventHandler
    public static void postInit (FMLPostInitializationEvent event) {}
    @Mod.EventHandler
    public static void loadComplete (FMLLoadCompleteEvent event) {
        UpdateChecker.updateCheck(MODID, NAME, UPDATE_URL, VERSION,false,null);
    }
    @Mod.EventHandler
    public static void serverStarting (FMLServerStartingEvent event) {
        server = event.getServer();
        event.registerServerCommand(new CommandMDE());
    }
    @SubscribeEvent
    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        UpdateChecker.updateCheck(MODID,NAME,UPDATE_URL,VERSION,true,player);
        LoginMessage.tell(player);
    }
}
