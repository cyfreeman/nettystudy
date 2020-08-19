package netty.netty.protocoltcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.netty.protocoltcp.MessageProtocol;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接受到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println();
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("server part receiveed msg as follows");
        System.out.println("lenth: " + len);
        System.out.println("contents : " + new String(content, Charset.forName("utf-8")));

        System.out.println("server recieve msg intotal :" + (++this.count));

        //回复消息
        String responseContent = UUID.randomUUID().toString();
        int responseLen = responseContent.getBytes("utf-8").length;
        byte[] responseContent2 = responseContent.getBytes("utf-8");
        //构建协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responseLen);
        messageProtocol.setContent(responseContent2);

        ctx.writeAndFlush(messageProtocol);
    }
}
