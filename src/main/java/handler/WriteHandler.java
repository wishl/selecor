package handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteHandler {

    public static void write(SocketChannel socketChannel,byte[] bytes) throws IOException {
        // 堆外内存减少拷贝
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
    }


}
