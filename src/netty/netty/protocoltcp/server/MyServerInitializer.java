package netty.netty.protocoltcp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.netty.protocoltcp.client.MyMesageEncoder;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        System.out.println("==========================");
        pipeline.addLast(new MyMessageDecoder());//解码器
        pipeline.addLast(new MyMesageEncoder());//编码器
        pipeline.addLast(new MyServerHandler());
    }
}
