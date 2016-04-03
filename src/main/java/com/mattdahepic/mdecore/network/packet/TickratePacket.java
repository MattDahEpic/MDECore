package com.mattdahepic.mdecore.network.packet;

import com.mattdahepic.mdecore.helpers.TickrateHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TickratePacket implements IMessageHandler<TickratePacket.TickrateMessage,IMessage> {
    @Override
    public IMessage onMessage (TickrateMessage msg, MessageContext ctx) {
        TickrateHelper.setClientTickrate(null,msg.tickrate);
        return null;
    }
    public static class TickrateMessage implements IMessage {
        private float tickrate;
        public TickrateMessage () {}
        public TickrateMessage (float tickrate) {
            this.tickrate = tickrate;
        }
        @Override
        public void toBytes (ByteBuf buf) {
            buf.writeFloat(tickrate);
        }
        @Override
        public void fromBytes (ByteBuf buf) {
            tickrate = buf.readFloat();
        }
    }
}
