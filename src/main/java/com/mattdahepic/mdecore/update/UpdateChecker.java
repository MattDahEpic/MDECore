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
    private static String remoteVersion = null;
    private static String changelog = null;
    private UpdateChecker () {}
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion,boolean inChat,EntityPlayer player) {
        remoteVersion = null;
        changelog = null;
        if (updateCheckEnabled) {
            if (!isUpToDate(modid,remoteUrl,currentVersion)) {
                if (inChat) {
                    if (player != null) {
                        player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "Update for " + modName + " available!"));
                    }
                } else {
                    LogHelper.info(modid, "---~===~---");
                    LogHelper.info(modid, "Update for " + modName + " available!");
                    LogHelper.info(modid, "Version " + remoteVersion + " available! You are currently running version " + currentVersion + ". Changes include:");
                    LogHelper.info(modid, changelog);
                    LogHelper.info(modid, "---~===~---");
                }
            }
        }
    }
    private static boolean getRemoteVersion(String modid, String remoteUrl) {
        try {
            URL updateUrl = new URL(remoteUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(updateUrl.openStream()));
            remoteVersion = reader.readLine();
            //TODO: critical updates
            changelog = reader.readLine();
            reader.close();
            return true;
        } catch (Exception e) {
            LogHelper.error(modid,"Error during attempted update check!");
            LogHelper.trace(modid,e);
            remoteVersion = null;
            return false;
        }
    }
    private static boolean isUpToDate (String modid, String remoteUrl, String currentVersion) {
        if (remoteVersion == null) {
            if (!getRemoteVersion(modid, remoteUrl)) return true;
        }
        return remoteVersion.equalsIgnoreCase(currentVersion);
    }
}