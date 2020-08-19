package netty.netty.protocoltcp.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.netty.protocoltcp.server.MyMessageDecoder;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        System.out.println("------------------------------");
        pipeline.addLast(new MyMesageEncoder());//加入编码器
        pipeline.addLast(new MyMessageDecoder());//加入解码器
        pipeline.addLast(new MyClientHandler());
    }
}
