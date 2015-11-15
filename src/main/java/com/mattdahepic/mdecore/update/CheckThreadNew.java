package com.mattdahepic.mdecore.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CheckThreadNew extends Thread {
    private String modid;
    private String remoteUrl;
    public CheckThreadNew (String modid, String remoteUrl) {
        setName("MattDahEpic Version Checker Thread - " + modid);
        setDaemon(true);
        this.modid = modid;
        this.remoteUrl = remoteUrl;
        start();
    }
    public void run () {
        String remoteVersion;
        try {
            URL updateUrl = new URL(remoteUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(updateUrl.openStream()));
            remoteVersion = reader.readLine();
            reader.close();
        } catch (Exception e) {
            System.err.print("Error during attempted update check: "+e.toString());
            remoteVersion = null;
        }
        UpdateCheckerNew.remoteVersions.put(modid, remoteVersion);
    }
}
