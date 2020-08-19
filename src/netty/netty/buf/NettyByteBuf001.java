package netty.netty.buf;


import com.sun.xml.internal.ws.addressing.WsaActionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;

public class NettyByteBuf001 {

    @Test
    public void test001(){
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
    }

    @Test
    public void test002() {
        ByteBuf hellow_world = Unpooled.copiedBuffer("hellow world", Charset.forName("utf-8"));


        if(hellow_world.hasArray()) {
            byte[] array = hellow_world.array();

            System.out.println(new String(array, Charset.forName("utf-8")));
            System.out.println("bytebuf=" + hellow_world);

            System.out.println(hellow_world.arrayOffset());
            System.out.println(hellow_world.readerIndex());
            System.out.println(hellow_world.writerIndex());
            System.out.println(hellow_world.capacity());

            int x = hellow_world.readableBytes();
            System.out.println(x);
        }

    }
}
