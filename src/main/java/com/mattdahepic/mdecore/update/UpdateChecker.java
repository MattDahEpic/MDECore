package com.mattdahepic.mdecore.update;

import com.mattdahepic.mdecore.config.Config;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class UpdateChecker {
    public static boolean updateCheckEnabled = Config.updateCheckEnabled;
    static Map<String,String> remoteVersions = new HashMap<String, String>(); //modid,remote version
    private UpdateChecker () {}
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion,boolean inChat,EntityPlayer player) {
        if (updateCheckEnabled) {
            if (!isUpToDate(modid,remoteUrl,currentVersion)) {
                if (inChat) {
                    if (player != null) {
                        player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "Update for " + modName + " available!"));
                    }
                } else {
                    LogHelper.info(modid, "---~===~---");
                    LogHelper.info(modid, "Update for " + modName + " available!");
                    LogHelper.info(modid, "Version " + remoteVersions.get(modid) + " available! You are currently running version " + currentVersion + ".");
                    LogHelper.info(modid, "---~===~---");
                }
            }
        }
    }
    private static boolean isUpToDate (String modid, String remoteUrl, String currentVersion) {
        if (!remoteVersions.containsKey(modid)) { //has not checked before
            new CheckThread(modid, remoteUrl);
            if (remoteVersions.get(modid) == null) return true;
        }
        return remoteVersions.get(modid).equalsIgnoreCase(currentVersion);
    }
}