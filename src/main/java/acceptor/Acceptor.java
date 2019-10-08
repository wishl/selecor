package acceptor;

import handler.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selector.Reactor;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

    private Reactor reactor;

    private static final Logger logger = LoggerFactory.getLogger(Acceptor.class);

    public Acceptor(Reactor reactor){
        logger.info("开始创建Acceptor对象");
        this.reactor = reactor;
        logger.info("创建Acceptor成功");
    }

    public void run() {
        try {
            System.out.println("accept");
            // 处理链接的请求
            SocketChannel accept = reactor.getServerSocketChannel().accept();
            if(accept!=null){// 每一个链接单独使用一个handler
                new ReadHandler(reactor.getSelector(),accept);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
