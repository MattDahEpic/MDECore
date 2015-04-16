package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.helpers.LoadStateHelper;
import com.mattdahepic.mdecore.update.UpdateChecker;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = MDECore.MODID,version = MDECore.VERSION,name = MDECore.NAME,acceptedMinecraftVersions = "1.7.10")
public class MDECore {
    public static final String MODID = "mdecore";
    public static final String VERSION = "v1.0-mc1.7.10";
    public static final String NAME = "MattDahEpic Core";

    @Mod.Instance(MDECore.MODID)
    public static MDECore instance;

    //sided proxy

    public static Configuration configFile;

    @Mod.EventHandler
    public static void load (FMLPreInitializationEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
        FMLCommonHandler.instance().bus().register(instance);
        Config.load(event);
    }
    @Mod.EventHandler
    public static void init (FMLInitializationEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void postInit (FMLPostInitializationEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void loadComplete (FMLLoadCompleteEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
        UpdateChecker.updateCheck(MODID,NAME,"https://raw.githubusercontent.com/MattDahEpic/MDECore1.8/master/version.txt",VERSION);
    }
    @Mod.EventHandler
    public static void serverAboutToStart (FMLServerAboutToStartEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void serverStarted (FMLServerStartedEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void serverStarting (FMLServerStartingEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void serverStopped (FMLServerStoppedEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
    @Mod.EventHandler
    public static void serverStopping (FMLServerStoppingEvent event) {
        LoadStateHelper.logLoadState(MODID,event);
    }
}
