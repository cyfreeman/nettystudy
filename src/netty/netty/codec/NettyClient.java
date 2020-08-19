package netty.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyClient {

    public static void main(String[] args) {
        //客户端的事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        //创建客户端的启动对象
        //客户端使用的不是serverbootstrap，时bootstrap
        Bootstrap bootstrap = new Bootstrap();

        //设置相关参数
        bootstrap.group(eventExecutors)//设置线程组
                .channel(NioSocketChannel.class)//设置客户端通道的实现类
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());//加入自己的处理器
                    }
                });
        System.out.println("client is ok!");
        //启动客户端去连接服务端
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
