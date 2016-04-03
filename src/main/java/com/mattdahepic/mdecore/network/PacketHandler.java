package com.mattdahepic.mdecore.network;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.sync.PacketConfigSync;
import com.mattdahepic.mdecore.network.packet.PacketPlaySoundFollowPlayer;
import com.mattdahepic.mdecore.network.packet.TickratePacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper net;
    public static void initPackets () {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(MDECore.MODID.toUpperCase());
        registerPacket(PacketConfigSync.Handler.class,PacketConfigSync.class,Side.CLIENT);
        registerPacket(TickratePacket.class,TickratePacket.TickrateMessage.class,Side.CLIENT);
        registerPacket(PacketPlaySoundFollowPlayer.class,PacketPlaySoundFollowPlayer.Message.class,Side.CLIENT);
    }
    private static int packetId = 0;
    @SuppressWarnings("unchecked")
    private static void registerPacket (Class packet,Class message,Side recieving) {
        net.registerMessage(packet,message,packetId,recieving);
        packetId++;
    }
}
