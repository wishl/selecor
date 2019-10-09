package handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ReadHandler implements Runnable {

    private SocketChannel socketChannel;

    private static final Logger logger = LoggerFactory.getLogger(ReadHandler.class);

    public ReadHandler(Selector selector, SocketChannel socketChannel){
        try {
            logger.info("开始创建ReadHandler对象");
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(this);
            selector.wakeup();
            logger.info("创建ReadHandler对象成功");
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("read");
        ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
        inputBuffer.clear();
        try {
            int i;
            while((i = socketChannel.read(inputBuffer))!=0&&i!=-1) {
                byte[] bs = new byte[1024];
                inputBuffer.flip();
                inputBuffer.get(bs, inputBuffer.position(), inputBuffer.limit());
                System.out.println(Arrays.toString(bs));
                // 写入
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                byteBuffer.put(bs);
                // 在写入前必须flip
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            if(i==-1){// 读取返回-1时，说明客户端主动关闭链接，否则一直轮询
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
