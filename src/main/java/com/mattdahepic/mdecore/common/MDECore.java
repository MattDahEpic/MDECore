package com.mattdahepic.mdecore.common;

import com.mattdahepic.mdecore.common.command.CommandMDE;
import com.mattdahepic.mdecore.common.config.MDEConfig;
import com.mattdahepic.mdecore.common.registries.CommandRegistry;
import com.mattdahepic.mdecore.common.registries.ConfigRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mdecore")
public class MDECore {
    public static final String MODID = "mdecore";

    public static final Logger logger = LogManager.getLogger();

    public MDECore () {
        ConfigRegistry.registerConfig(null, MDEConfig.COMMON_SPEC);

        //mod bus events
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::commonSetup);

        //forge bus events
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(CommandRegistry::register);

        forgeBus.addListener(this::playerJoinedServer);
    }

    public void commonSetup (final FMLCommonSetupEvent event) {
        CommandRegistry.registerCommand(CommandMDE::register);
    }

    public void playerJoinedServer (PlayerEvent.PlayerLoggedInEvent e) {
        if (!MDEConfig.COMMON.loginMessage.get().isEmpty()) e.getPlayer().sendStatusMessage(new net.minecraft.util.text.StringTextComponent(MDEConfig.COMMON.loginMessage.get()),false);
    }
}
