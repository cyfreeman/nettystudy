package netty.wechat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class WechatClient {

    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String usrName;

    public WechatClient() {
        try {
            selector = Selector.open();
            socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
            socketChannel.configureBlocking(false);

            socketChannel.register(selector, SelectionKey.OP_READ);
            usrName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("ok for: " + usrName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfoToServer(String info) {
        info = usrName + "says : " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readInfo() {
        try {
            int readChannels = selector.select(200);
            if(readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if(key.isReadable()) {
                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);
                        String s = new String(allocate.array());
                        System.out.println(s.trim());
                        iterator.remove();
                    }
                    else {
//                        System.out.println("no avalable channels!");
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WechatClient wechatClient = new WechatClient();
        new Thread() {
            public void run(){
                while(true) {
                    wechatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            wechatClient.sendInfoToServer(s);
        }

    }
}
