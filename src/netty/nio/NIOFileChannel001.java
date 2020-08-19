package netty.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel001 {

    public static void main(String[] args) throws Exception{
        FileChannelAstablish();
        return;
    }

    private static void FileChannelAstablish() throws IOException {
        String str = "hellow cy";
        FileOutputStream fileOutputStream = new FileOutputStream("d://NIOFileChannel001.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(str.getBytes());

        byteBuffer.flip();

        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }

    /*
    用filebuffer从文件读取数据
     */
    @Test
    public void FileChannelWriteAndRead() throws Exception{
        File file = new File("d://NIOFileChannel001.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //建立通道
        FileChannel channel = fileInputStream.getChannel();
        //建立buffer大小
        ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

        channel.read(allocate);

        System.out.println(new String(allocate.array()));
    }

    @Test
    public void OneBufferReadAndWrite() throws Exception{
        File file1 = new File("1.txt");
        FileInputStream fileInputStream001 = new FileInputStream(file1);
        FileChannel channel001 = fileInputStream001.getChannel();

        FileOutputStream fileOutputStream002 = new FileOutputStream("12.txt");
        FileChannel channel002 = fileOutputStream002.getChannel();

        ByteBuffer allocate = ByteBuffer.allocate(512);

        while(true){
            allocate.clear();

            int read = channel001.read(allocate);
            if(-1 == read){
                break;
            }
            allocate.flip();
            channel002.write(allocate);
        }
    }

    @Test
    public void TransferFiles() throws Exception{
        File file1 = new File("d://20180821160253.png");
        FileInputStream fileInputStream001 = new FileInputStream(file1);
        FileChannel sourceChannel = fileInputStream001.getChannel();

        FileOutputStream fileOutputStream002 = new FileOutputStream("d://F20180821160253.png");
        FileChannel dstChannel = fileOutputStream002.getChannel();

        dstChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        sourceChannel.close();
        dstChannel.close();
        fileInputStream001.close();
        fileOutputStream002.close();
    }
}
