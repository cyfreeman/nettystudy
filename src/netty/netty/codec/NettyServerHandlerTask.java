package netty.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;


/**
 * 自定义一个handler需要继承netty规定好的某个handleradapter（规范）
 */
public class NettyServerHandlerTask extends ChannelInboundHandlerAdapter {

    //读取客户端发送的消息
    //ChannelHandlerContext ctx:上下文对象，含有管道[业务逻辑]pipeline；通道[数据]channel地址
    //msg：客户端发送的数据，默认是object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //解决方案1
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hellow client miao 222", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("err deduced" +e.getMessage());
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hellow client miao 333", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("err deduced" +e.getMessage());
                }
            }
        });
        System.out.println("go on !~");
        //用户自定义定时任务->该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hellow client miao 444", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    System.out.println("err deduced" +e.getMessage());
                }
            }
        }, 5 , TimeUnit.SECONDS);

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
