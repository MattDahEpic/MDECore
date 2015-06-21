package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.*;

public class LoginMessage {
    public static File loginMessageFile;
    public static String message = null;
    public static void init (File configDir) {
        loginMessageFile = new File(configDir,"loginmessage.txt");
        if (!loginMessageFile.exists()) return;
        try {
            BufferedReader in = new BufferedReader(new FileReader(loginMessageFile));
            message = in.readLine();
            in.close();
        } catch (IOException e) {
            LogHelper.error(MDECore.MODID,"Can not access file "+loginMessageFile.getAbsolutePath());
        }
    }
    public static void tell (EntityPlayer player) {
        player.addChatComponentMessage(new ChatComponentText(message));
    }
}
