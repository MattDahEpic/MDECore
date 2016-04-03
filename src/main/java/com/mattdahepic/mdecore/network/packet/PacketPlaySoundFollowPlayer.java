package com.mattdahepic.mdecore.network.packet;

import com.mattdahepic.mdecore.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketPlaySoundFollowPlayer implements IMessageHandler<PacketPlaySoundFollowPlayer.Message,IMessage> {
    public static void sendToPlayer (String sound, EntityPlayerMP player) {
        PacketHandler.net.sendTo(new Message(sound),player);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage (Message msg, MessageContext ctx) {
        if (ctx.side.isClient()) {
            PsuedoAntiCrasher.doo(msg.sound);
        }
        return null;
    }
    public static class Message implements IMessage {
        private String sound;
        public Message () {}
        public Message (String sound) {
            this.sound = sound;
        }
        @Override
        public void toBytes (ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf,sound);
        }
        @Override
        public void fromBytes (ByteBuf buf) {
            sound = ByteBufUtils.readUTF8String(buf);
        }
    }
    private static class PsuedoAntiCrasher {
        private static void doo (String s) {
            Minecraft.getMinecraft().getSoundHandler().playSound(new com.mattdahepic.mdecore.client.MovingSoundPlayer(s));
        }
    }
}
