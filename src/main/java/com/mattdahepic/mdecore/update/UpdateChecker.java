package com.mattdahepic.mdecore.update;

import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {
    public static boolean updateCheckEnabled = Config.updateCheckEnabled;
    public static String remoteVersion = null;
    public static String changelog = null;
    private UpdateChecker () {}
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion) {
        updateCheck(modid, modName, remoteUrl, currentVersion, false, null);
    }
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion,boolean inChat,EntityPlayer player) {
        if (updateCheckEnabled) {
            if (checkUpdateAvailable(modid, remoteUrl, currentVersion)) {
                if (inChat) {
                    if (player != null) {
                        player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE+"Update for " + modName + " available!"));
                    }
                } else {
                    LogHelper.info(modid, "---~===~---");
                    LogHelper.info(modid, "Update for " + modName + " available!");
                    LogHelper.info(modid, "Version " + remoteVersion + " available! You are currently running version " + currentVersion + ". Changes include:");
                    LogHelper.info(modid, changelog);
                    LogHelper.info(modid, "---~===~---");
                    //TODO: fancy colors n' shit
                }
            }
        }
    }
    private static boolean checkUpdateAvailable(String modid, String remoteUrl, String currentVersion) {
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