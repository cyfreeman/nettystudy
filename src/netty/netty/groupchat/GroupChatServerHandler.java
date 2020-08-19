package netty.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有的channel
    //GlobalEventExecutor.INSTANCE是一个全局事件执行器，单例模式
    //channelgroup帮助管理所有，不用一一遍历
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");

    //表示连接建立，一旦连接建立，第一个被执行
    //将当前的channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //该方法会将channelgroup中素有的channel遍历，并发送"[client]" + channel.remoteAddress()+ " added to chat\n"
        //不需要自己遍历，一句话搞定！channelGroup.writeAndFlush
        channelGroup.writeAndFlush("[client]" + channel.remoteAddress()+ " added to chat" + date.format(new java.util.Date()) + "\n");
        channelGroup.add(channel);
    }

    //某某客户离开信息推送给当前在线客户
    //channelGroup自动移除，维护大小
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush("[client]" + channel.remoteAddress()+ " is leavet\n");
        System.out.println("channelGroup 's size is " + channelGroup.size());
    }

    //表示channel属于一个活动的状态，提示xx已上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " is online");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " is offline");
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        Channel channel = channelHandlerContext.channel();

        channelGroup.forEach(ch -> {
            if(channel != ch){
                ch.writeAndFlush("[customer]" + channel.remoteAddress() + " is sended msg: " + msg +"\n");
            } else {
                ch.writeAndFlush("[theirself] have send the msg:" + msg +"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("close the channel");
        ctx.close();
    }
}
