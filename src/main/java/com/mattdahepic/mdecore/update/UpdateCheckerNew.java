package com.mattdahepic.mdecore.update;

import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

public class UpdateCheckerNew {
    public static boolean updateCheckEnabled = MDEConfig.updateCheckEnabled;
    static Map<String,String> remoteVersions = new HashMap<String,String>(); //modid,remote version
    public static void checkRemote (String modid,String remoteUrl) {
        if (updateCheckEnabled) {
            if (!remoteVersions.containsKey(modid)) { //has not checked before
                new CheckThreadNew(modid,remoteUrl);
            }
        }
    }
    public static void printMessageToPlayer (String modid, EntityPlayer player) {
        if (updateCheckEnabled) {
            String modName = Loader.instance().getIndexedModList().get(modid).getName();
            String currentVersion = Loader.instance().getIndexedModList().get(modid).getVersion();
            if (remoteVersions.containsKey(modid) ? remoteVersions.get(modid) != null : false) { //has finished check
                if (!remoteVersions.get(modid).equals(currentVersion)) {
                    player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD+"["+modName+"] "+EnumChatFormatting.LIGHT_PURPLE+" Version "+remoteVersions.get(modid)+" available!"));
                }
            }
        }
    }
}
