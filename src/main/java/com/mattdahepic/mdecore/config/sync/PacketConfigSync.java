package com.mattdahepic.mdecore.config.sync;

import io.netty.buffer.ByteBuf;

import com.google.common.base.Throwables;
import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.config.MDEConfig;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PacketConfigSync implements IMessage {
    //thanks to EnderCore
    private Map<String,Object> configValues;
    private String configName;
    public PacketConfigSync () {}
    public PacketConfigSync (ConfigProcessor toSync) {
        this.configValues = toSync.configValues;
        this.configName = toSync.configFileName;
    }
    @Override
    public void toBytes(ByteBuf buf) {
        ByteArrayOutputStream obj = new ByteArrayOutputStream();

        try {
            GZIPOutputStream gzip = new GZIPOutputStream(obj);
            ObjectOutputStream objStream = new ObjectOutputStream(gzip);
            objStream.writeObject(configValues);
            objStream.close();
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        buf.writeShort(obj.size());
        buf.writeBytes(obj.toByteArray());

        ByteBufUtils.writeUTF8String(buf, configName);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void fromBytes(ByteBuf buf) {
        short len = buf.readShort();
        byte[] compressedBody = new byte[len];

        for (short i = 0; i < len; i++)
            compressedBody[i] = buf.readByte();

        try {
            ObjectInputStream obj = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(compressedBody)));
            configValues = (Map<String, Object>) obj.readObject();
            obj.close();
        } catch (Exception e) {
            Throwables.propagate(e);
        }

        configName = ByteBufUtils.readUTF8String(buf);
    }

    public static class Handler implements IMessageHandler<PacketConfigSync, PacketConfigSync> {
        @Override
        public PacketConfigSync onMessage(PacketConfigSync message, MessageContext ctx) {
            ConfigProcessor processor = ConfigProcessor.processorMap.get(message.configName);
            if (processor != null) {
                if (MDEConfig.debugLogging) MDECore.logger.info("Received config synchronization packet from server for config "+processor.configFileName+".cfg. Setting values accordingly...");
                processor.syncTo(message.configValues);
                MinecraftForge.EVENT_BUS.post(new ConfigSyncEvent(message.configName));
            }
            return null;
        }
    }
}
