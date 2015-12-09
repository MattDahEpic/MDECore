package com.mattdahepic.mdecore.network;

import com.dmurph.tracking.AnalyticsConfigData;
import com.dmurph.tracking.JGoogleAnalyticsTracker;
import com.dmurph.tracking.system.AWTSystemPopulator;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.helpers.EnvironmentHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class StatReporter {
    //By using the mod you agree to the EULA: http://mattdahepic.com/code/mods/eula
    public static void gatherAndReport () {
        //setup google analytics tracker
        JGoogleAnalyticsTracker.setProxy(System.getenv("http_proxy"));
        AnalyticsConfigData config = new AnalyticsConfigData("UA-46943413-4");
        AWTSystemPopulator.populateConfigData(config);
        JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(config, JGoogleAnalyticsTracker.GoogleAnalyticsVersion.V_4_7_2);
        tracker.setDispatchMode(JGoogleAnalyticsTracker.DispatchMode.MULTI_THREAD);
        //do reporting
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            if (mod.getMetadata().authorList.contains("MattDahEpic") || mod.getMetadata().authorList.contains("mattdahepic")) {
                String modPage = mod.getModId()+"-"+mod.getVersion()+(EnvironmentHelper.isServer?"-server":"-client")+(EnvironmentHelper.isDeobf?"-deobf":"");
                if (MDEConfig.debugLogging) MDECore.logger.info("Logging load for mod \""+modPage+"\".");
                tracker.trackPageView(modPage, null, null); //modid-version-(server|client)-deobf
            }
        }
        //cleanup
        tracker.completeBackgroundTasks(1000);
    }
}
