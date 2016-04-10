package com.mattdahepic.mdecore;

import com.mattdahepic.mdecore.command.CommandMDE;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.debug.DebugItem;
import com.mattdahepic.mdecore.helpers.EnvironmentHelper;
import com.mattdahepic.mdecore.helpers.TickrateHelper;
import com.mattdahepic.mdecore.network.PacketHandler;
import com.mattdahepic.mdecore.network.StatReporter;
import com.mattdahepic.mdecore.proxy.CommonProxy;
import com.mattdahepic.mdecore.tweaks.*;
import com.mattdahepic.mdecore.tweaks.redstone.MaterialWaterproofCircuits;
import com.mattdahepic.mdecore.tweaks.redstone.WaterproofRedstone;
import com.mattdahepic.mdecore.world.TickHandlerWorld;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(modid = MDECore.MODID,version = MDECore.VERSION,name = MDECore.NAME,dependencies = MDECore.DEPENDENCIES,updateJSON = MDECore.UPDATE_JSON)
public class MDECore extends DummyModContainer {
    public static final String MODID = "mdecore";
    static final String VERSION = "@VERSION@";
    static final String NAME = "MattDahEpic Core";
    static final String UPDATE_JSON = "https://raw.githubusercontent.com/MattDahEpic/Version/master/"+MODID+".json";
    static final String DEPENDENCIES = "required-after:Forge@[12.16.0.1846,);";

    public static final Logger logger = LogManager.getLogger(MODID);
    private static final UUID MATT_UUID = UUID.fromString("c715991d-e69c-48f9-a92d-8fc60c0829fb");

    public static Item debugItem = new DebugItem();
    public static Material waterproof_circuits = new MaterialWaterproofCircuits(MapColor.airColor);

    @SidedProxy(clientSide = "com.mattdahepic.mdecore.proxy.ClientProxy",serverSide = "com.mattdahepic.mdecore.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void prePreInit (FMLConstructionEvent e) {
        EnvironmentHelper.isServer = e.getSide().isServer();
    }

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        OreDictionaryExtras.preInit();
        MDEConfig.instance(MODID).initialize(e);
        WaterproofRedstone.setup();
        proxy.setupItems();
        proxy.setupTextures();
        if (e.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new com.mattdahepic.mdecore.client.MattRenderTweaks());
        }
    }
    @Mod.EventHandler
    public void init (FMLInitializationEvent e) {
        PacketHandler.initPackets();
        MinecraftForge.EVENT_BUS.register(new WaterBottleCauldron());
        MinecraftForge.EVENT_BUS.register(new DaySleepToNight());
        MinecraftForge.EVENT_BUS.register(new EnderdragonElytra());
        MinecraftForge.EVENT_BUS.register(TickHandlerWorld.instance);
    }
    @Mod.EventHandler
    public void finishedLoading (FMLLoadCompleteEvent e) {
        StatReporter.gatherAndReport();
    }
    @Mod.EventHandler
    public void serverStarting (FMLServerStartingEvent e) {
        CommandMDE.instance.init(e);
    }
    @SubscribeEvent
    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent e) {
        TickrateHelper.setClientToCurrentServerTickrate(e.player);
        if (!MDEConfig.loginMessage.isEmpty()) e.player.addChatMessage(new TextComponentString(MDEConfig.loginMessage));
        if (MDEConfig.holidayRewards) HolidayTweaks.doooooo(e.player);
    }
    @SubscribeEvent
    public void clientLeftServer (FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
        TickrateHelper.resetClientTickrate();
    }
}
