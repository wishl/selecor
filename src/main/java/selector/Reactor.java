package selector;

import acceptor.AcceptorImpl;
import org.omg.SendingContext.RunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import subselector.NioSubSelector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用的处理链接,用于分配Acceptor,第一次触发selector之后直接触发Acceptor
 */
public class Reactor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Reactor.class);

    private final Selector selector;
    // 链接使用的channel
    private final ServerSocketChannel serverSocketChannel;

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public Selector getSelector() {
        return selector;
    }

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",port);
        // 绑定ip端口
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 非阻塞io
        serverSocketChannel.configureBlocking(false);
        // serverSocket处理链接事件
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 存储事件唤醒
        selectionKey.attach(new AcceptorImpl(this));

        logger.info("创建Reactor成功");
    }

    public void run() {
        while (!Thread.interrupted()){
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    dispatch(selectionKey);
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 转发请求
    public void dispatch(SelectionKey key){
        Runnable runnable = (Runnable) key.attachment();
        if(runnable!=null){
            runnable.run();
        }
    }
}
