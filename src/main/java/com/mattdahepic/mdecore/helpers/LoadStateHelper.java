package com.mattdahepic.mdecore.helpers;

import com.mattdahepic.mdecore.config.Config;
import net.minecraftforge.fml.common.event.*;

public class LoadStateHelper {
    public static boolean stateLogEnabled = Config.loadStateLogEnabled;
    public static void logLoadState (String modid,FMLStateEvent event) {
        if (stateLogEnabled) {
            if (event instanceof FMLPreInitializationEvent) {
                LogHelper.info(modid,"Entered Pre-Initialization!");
            } else if (event instanceof FMLInitializationEvent) {
                LogHelper.info(modid,"Entered Initialization!");
            } else if (event instanceof FMLPostInitializationEvent) {
                LogHelper.info(modid,"Entered Post-Initialization!");
            } else if (event instanceof FMLLoadCompleteEvent) {
                LogHelper.info(modid,"Load Complete Event fired.");
            } else if (event instanceof FMLServerAboutToStartEvent) {
                LogHelper.info(modid,"Server About To Start Event fired.");
            } else if (event instanceof FMLServerStartedEvent) {
                LogHelper.info(modid,"Server Started Event fired.");
            } else if (event instanceof FMLServerStartingEvent) {
                LogHelper.info(modid,"Server Starting Event fired.");
            } else if (event instanceof FMLServerStoppedEvent) {
                LogHelper.info(modid,"Server Stopped Event fired.");
            } else if (event instanceof FMLServerStoppingEvent) {
                LogHelper.info(modid,"Server Stopping Event fired.");
            } else {
                LogHelper.warn(modid,"Attempted to identify event that is not a state event!");
            }
        }
    }
}
