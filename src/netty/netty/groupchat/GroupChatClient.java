package netty.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;


public class GroupChatClient {

    //属性
    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //得到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //加入相关的handler
                            pipeline.addLast("decoder", new StringDecoder());
                            //加入相关解码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自定期的handler
                            pipeline.addLast(new GroupChatClientHandler());//打桩
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("---------" + channel.localAddress() + "-----------");
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String s = scanner.nextLine();
                channel.writeAndFlush(s + "\r\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
