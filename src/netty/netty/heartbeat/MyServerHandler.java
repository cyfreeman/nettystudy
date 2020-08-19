package netty.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            //将evt向下转型IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventT = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventT = "read idle";
                    break;
                case ALL_IDLE:
                    eventT = "read & write idle";
                    break;
                case WRITER_IDLE:
                    eventT = "write idle";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--time out--" +eventT);
            System.out.println("server is dealing..");
        }
    }
}
