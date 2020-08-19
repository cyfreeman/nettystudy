package netty.netty.protocoltcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import netty.netty.protocoltcp.MessageProtocol;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder method is be called");
        //需要将得到的二进制字节码--->MessageProtocol数据包（对象
        int lenth = byteBuf.readInt();
        byte[] content = new byte[lenth];
        byteBuf.readBytes(content);

        //封装成MessageProtocol数据包对象，放入out，传给下一个handler
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(lenth);
        messageProtocol.setContent(content);
        out.add(messageProtocol);

    }
}
