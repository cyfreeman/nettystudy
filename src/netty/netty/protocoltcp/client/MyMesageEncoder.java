package netty.netty.protocoltcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.netty.protocoltcp.MessageProtocol;

public class MyMesageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol, ByteBuf out) throws Exception {
        System.out.println("MyMesageEncoder method is be used");
        out.writeInt(messageProtocol.getLen());
        out.writeBytes(messageProtocol.getContent());
    }
}
