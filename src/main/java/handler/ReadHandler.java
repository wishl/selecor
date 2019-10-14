package handler;

import abstracts.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ReadHandler extends AbstractHandler {

    private SocketChannel socketChannel;

    private static final Logger logger = LoggerFactory.getLogger(ReadHandler.class);

    public ReadHandler(Selector selector, SocketChannel socketChannel){
        try {
            logger.info("开始创建ReadHandler对象");
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            // 在selector.select()方法阻塞的时候，register方法会一直阻塞知道select()方法返回
            selector.wakeup();
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
            selectionKey.attach(this);
            logger.info("创建ReadHandler对象成功");
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doHandler() {
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
                WriteHandler.write(socketChannel,bs);
            }
            if(i==-1){// 读取返回-1时，说明客户端主动关闭链接，否则一直轮询
                socketChannel.close();
            }
        } catch (IOException e) {
            // OP_READ 事件不仅仅只有可读时才触发，
            // 当channel中数据读完远程的另一端被关闭有一个错误的pending都会触发OP_READ事件"!
            e.printStackTrace();
            try {
                socketChannel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public <T> T callback(T t) {
        return null;
    }

    @Override
    public <T> T fail() {
        return null;
    }
}
