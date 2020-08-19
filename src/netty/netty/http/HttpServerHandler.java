package netty.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是httprequest请求
        if(msg instanceof HttpRequest) {
            System.out.println("msg 类型： " + msg.getClass());
            System.out.println("client addr: " + ctx.channel().remoteAddress());

            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri，过滤重复请求
            URI uri = new URI(httpRequest.uri());

            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("request is dealed , no response for it~");
                return;
            }
            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hellow i am server", CharsetUtil.UTF_8);

            //打造HTTP响应
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //将构建好的response返回
            ctx.writeAndFlush(response);
        }
    }
}
