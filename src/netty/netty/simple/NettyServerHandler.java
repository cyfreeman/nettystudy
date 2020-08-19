package netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;


/**
 * 自定义一个handler需要继承netty规定好的某个handleradapter（规范）
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取客户端发送的消息
    //ChannelHandlerContext ctx:上下文对象，含有管道[业务逻辑]pipeline；通道[数据]channel地址
    //msg：客户端发送的数据，默认是object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read client thread msg: " + Thread.currentThread().getName());
        System.out.println("server ctx = : " + ctx);
        System.out.println("channel & pipeline");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表，出栈入栈
        //将msg转成一个bytebuffer
        //BYTEBUFFER是netty提供的，不是NIO的
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送的消息是： " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client addr is : " + ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲区，并刷新
        //先将发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hellow client ", CharsetUtil.UTF_8));

    }

    //处理异常，关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
