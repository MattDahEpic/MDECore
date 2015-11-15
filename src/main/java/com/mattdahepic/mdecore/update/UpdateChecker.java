package com.mattdahepic.mdecore.update;

import net.minecraft.entity.player.EntityPlayer;

public class UpdateChecker {
    private UpdateChecker () {}
    @Deprecated
    public static void updateCheck (String modid,String modName,String remoteUrl,String currentVersion,boolean inChat,EntityPlayer player) {
        updateCheck(modid, modName, remoteUrl, currentVersion, player);
    }
    public static void updateCheck (String modid,String modName,String updateUrl,String currentVersion,EntityPlayer player) {
        if (!UpdateCheckerNew.remoteVersions.containsKey(modid)) {
            UpdateCheckerNew.checkRemote(modid,updateUrl);
        } else {
            UpdateCheckerNew.printMessageToPlayer(modid,player);
        }
    }
}