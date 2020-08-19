package netty.nio;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServerSelector {

    /**
     * 创建selector，并从客户端创建连接
     * @throws Exception
     */
    @Test
    public void Selector() throws Exception{

        //创建服务器的serversocket和selector
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();

        //初始化配置serverSocketChannel，以及注册serverSocketChannel到selector，由selector进行统一管理
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //非阻塞等待事件发生
        while(true){
            if(selector.select(1000) == 0){
                System.out.println("1 second waited, no client connected ");
                continue;
            }
            //传递监听到的事件的集合并创建迭代器
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            //遍历寻找发生的事件
            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //找到事件发生对应的selectkey
                //注册到socketchannel
                if(key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("client connect success"+ socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                //有内容传递过来，读取操作
                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer attachment = (ByteBuffer)key.attachment();
                    channel.read(attachment);
                    System.out.println("from client "+ new String(attachment.array()));
                }

                keyIterator.remove();
            }

        }
    }

    @Test
    public void NIOClient() throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("connect need time , client wont block, could do something else");
            }
        }
        String str = "welcome connect";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
