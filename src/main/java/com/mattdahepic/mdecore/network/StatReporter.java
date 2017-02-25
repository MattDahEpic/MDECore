package com.mattdahepic.mdecore.network;

import com.mattdahepic.mdecore.helpers.EnvironmentHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class StatReporter {
    //By using the mod you agree to the EULA: http://mattdahepic.com/mods/eula
    public static void gatherAndReport () {
        //setup
        final HttpClient httpclient = HttpClients.createDefault();
        //report
        for (final ModContainer mod : Loader.instance().getActiveModList()) {
            if (mod.getMetadata().authorList.contains("MattDahEpic") || mod.getMetadata().authorList.contains("mattdahepic")) {
                Thread t = new Thread(() -> {
                    try {
                        HttpPost httppost = new HttpPost("https://telemetry.mattdahepic.com/submit/1/");
                        //prep request
                        List<NameValuePair> params = new ArrayList<>(3);
                        params.add(new BasicNameValuePair("product", mod.getModId()));
                        params.add(new BasicNameValuePair("version", mod.getVersion() + (EnvironmentHelper.isServer ? "-server" : "-client")));
                        params.add(new BasicNameValuePair("action", EnvironmentHelper.isDeobf ? "dev" : "run"));
                        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                        //do request
                        httpclient.execute(httppost);
                    } catch (Exception ignored) {}
                });
                t.setName("MattDahEpic Telemetry Thread");
                t.start();
            }
        }
    }
}
