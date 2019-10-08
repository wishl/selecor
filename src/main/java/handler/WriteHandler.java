package handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 写数据handler，不能直接添加selectionkey.op_write否则将会一直触发写事件，
 * 需要使用interestOps添加op_write并在处理后更改回来
 */
public class WriteHandler implements Runnable {

    private SocketChannel socketChannel;

    private ByteBuffer byteBuffer;

    private SelectionKey selectionKey;

    private Selector selector;

    public WriteHandler(SocketChannel socketChannel,Selector selector){
        try {
            this.socketChannel = socketChannel;
            socketChannel.configureBlocking(false);
            // 为了获取 selectionKey
            this.selectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
            this.selector = selectionKey.selector();
            // attach会覆盖
            selectionKey.attach(this);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println("write");
            if(byteBuffer!=null&&selectionKey.isWritable()){// 有东西写
               byteBuffer.flip();
               socketChannel.write(byteBuffer);
               byteBuffer.clear();
               selectionKey.interestOps(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
