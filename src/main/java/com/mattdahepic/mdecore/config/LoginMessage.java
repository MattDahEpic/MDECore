package com.mattdahepic.mdecore.config;

import com.mattdahepic.mdecore.MDECore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginMessage {
    public static File loginMessageFile;
    public static String message = null;
    public static void init (File configDir) {
        loginMessageFile = new File(configDir,"mattdahepic"+File.separator+"loginmessage.txt");
        if (!loginMessageFile.exists()) return;
        try {
            BufferedReader in = new BufferedReader(new FileReader(loginMessageFile));
            message = in.readLine();
            in.close();
        } catch (IOException e) {
            MDECore.logger.error(MDECore.MODID, "Can not access file " + loginMessageFile.getAbsolutePath());
        }
    }
    public static void tell (EntityPlayer player) {
        if (message != null) {
            player.addChatComponentMessage(new ChatComponentText(message));
        }
    }
}
