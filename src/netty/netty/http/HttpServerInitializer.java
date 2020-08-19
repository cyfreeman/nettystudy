package netty.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入一个netty提供的httpservercodec
        //1、httpSERVER是netty提供的处理http的编解码器
        pipeline.addLast("myHttpServer", new HttpServerCodec());
        //2、增加一个自定义的handle
        pipeline.addLast("myHttpServerHandle", new HttpServerHandler());
    }
}
