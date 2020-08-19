package netty.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import org.junit.Test;

public class NettyServer {

    public static void main(String[] args) throws Exception{


        //1、创建两个线程组，B和W
        //2、boss只做连接处理，worker处理真正的业务
        //boss和worker含有的子线程的个数默认是CPU核数*2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            serverBootstrap.group(bossGroup, workerGroup)//设置两个线程组
            .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器的通信通道
            .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数
            .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
            .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象（匿名对象）
                //向pipeline设置处理器
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //可以使用一个集合管理socketchannel，在推送消息时，可以将业务加入到各个channel对应的nioeventLoop的taskqueue或scheduleTaskqueque
                    socketChannel.pipeline().addLast(new NettyServerHandlerTask());
                }
            });//给我们的workergroup的EVENTLOOP设置对应的处理器

            System.out.println("=======server is ready======");
            //绑定一个端口并同步，生成一个cf对象;启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()) {
                        System.out.println("monitor port 6668 success");
                    }else {
                        System.out.println("monitor port 6667 failed");
                    }
                }
            });
            //对关闭通道事件进行侦听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }

    }

    @Test
    public void test(){
        System.out.println(NettyRuntime.availableProcessors());
    }
}
