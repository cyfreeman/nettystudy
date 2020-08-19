package netty.netty.protocoltcp.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import netty.netty.protocoltcp.MessageProtocol;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private  int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送十条数据
        for (int i = 0; i < 5; i++) {
            String msgTo = "weather cold, eat hotpot";
            byte[] content = msgTo.getBytes(Charset.forName("utf-8"));
            int length = msgTo.getBytes(Charset.forName("utf-8")).length;

            //创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        System.out.println();
        System.out.println("client receive msg as follows： ");
        System.out.println("lenth: " + len);
        System.out.println("contends : " + new String(content, Charset.forName("utf-8")));
        System.out.println("receive in total: " + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exception msg:" +cause);
    }
}
