package com.mattdahepic.mdecore.network;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.sync.PacketConfigSync;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper net;
    public static void initPackets () {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(MDECore.MODID.toUpperCase());
        registerPacket(PacketConfigSync.Handler.class,PacketConfigSync.class,Side.CLIENT);
    }
    private static int packetId = 0;
    private static void registerPacket (Class packet,Class message,Side recieving) {
        net.registerMessage(packet,message,packetId,recieving);
        packetId++;
    }
}
