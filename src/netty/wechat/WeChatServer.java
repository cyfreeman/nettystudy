package netty.wechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class WeChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public WeChatServer() {

        try {
            //get selector
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        while(true){
            try {
                int count = selector.select(200);
//                System.out.println("waiting...");
                if(count > 0){//have event need to deal
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+ "is online");
                        }else if(key.isReadable()) {
                            readMSG(key);
                        }
                        //防止重复处理，把key删除
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMSG(SelectionKey selectionKey){
        SocketChannel channel = null;

        try {
            channel = (SocketChannel)selectionKey.channel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int count = channel.read(allocate);
            if(count > 0){
                String msg = new String(allocate.array());
                System.out.println("from client msg is : " + msg);

                //transfer msg to other clients，write a function to deal with
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + "off line.");
                selectionKey.cancel();
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel myChannel){
        System.out.println("server is transfering msg");
        try {
            for (SelectionKey key : selector.keys()) {
                Channel channel = key.channel();
                if(channel instanceof SocketChannel && channel != myChannel) {
                    SocketChannel dest = (SocketChannel) channel;
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        WeChatServer weChatServer = new WeChatServer();
        weChatServer.listen();
    }
}
