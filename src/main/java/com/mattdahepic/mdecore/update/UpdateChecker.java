package com.mattdahepic.mdecore.update;

import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.helpers.LogHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {
    public static boolean updateCheckEnabled = Config.updateCheckEnabled;
    public static String remoteVersion = null;
    public static String changelog = null;
    private UpdateChecker () {}
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion) {
        if (updateCheckEnabled) {
            if (checkUpdateAvaliable(modid, remoteUrl, currentVersion)) {
                LogHelper.info(modid,"---~===~---");
                LogHelper.info(modid,"Update for "+modName+" avaliable!");
                LogHelper.info(modid,"Version "+remoteVersion+" avaliable! You are currently running version "+currentVersion+". Changes include:");
                LogHelper.info(modid,changelog);
                LogHelper.info(modid,"---~===~---");
                //TODO: in game chat
                //TODO: fancy colors n' shit
            }
        } else {
            LogHelper.info(modid,"Checking of updates is disabled, ignoring update check for mod "+modName+".");
        }
    }
    private static boolean checkUpdateAvaliable (String modid,String remoteUrl,String currentVersion) {
        try {
            URL updateUrl = new URL(remoteUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(updateUrl.openStream()));
            remoteVersion = reader.readLine();
            //TODO: critical updates
            changelog = reader.readLine();
            reader.close();
        } catch (Exception e) {
            LogHelper.error(modid,"Error during attempted update check!");
            LogHelper.trace(modid,e);
            remoteVersion = null;
            return false;
        }
        return !remoteVersion.equalsIgnoreCase(currentVersion);
    }
}
