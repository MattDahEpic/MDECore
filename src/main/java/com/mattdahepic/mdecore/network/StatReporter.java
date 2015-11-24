package com.mattdahepic.mdecore.network;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.system.AWTSystemPopulator;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class StatReporter {
    //By using the mod you agree to the EULA: http://mattdahepic.com/code/mods/eula
    public static void gatherAndReport () {
        //deobf?
        boolean isDeobf = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

        //setup google analytics tracker
        JGoogleAnalyticsTracker.setProxy(System.getenv("http_proxy"));
        AnalyticsConfigData config = new AnalyticsConfigData("UA-46943413-4");
        AWTSystemPopulator.populateConfigData(config);
        JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(config, JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2);
        tracker.setDispatchMode(JGoogleAnalyticsTracker.DispatchMode.MULTI_THREAD);
        //do reporting
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            if (mod.getMetadata().authorList.contains("MattDahEpic") || mod.getMetadata().authorList.contains("mattdahepic")) {
                if (MDEConfig.debugLogging)
                    MDECore.logger.info("Logging load for mod \"" + mod.getModId() + "\" at version \"" + mod.getVersion() + (isDeobf ? "-deobf" : "") + "\".");
                tracker.trackPageView(mod.getModId() + "-" + mod.getVersion() + (isDeobf ? "-deobf" : ""), null, null); //modid-mcVer-modVer-(deobf)
            }
        }
        //cleanup
        tracker.completeBackgroundTasks(1000);
    }
}
