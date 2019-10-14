package acceptor;

import handler.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import selector.Reactor;
import subselector.NioSubSelector;
import thread.ThreadPool;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class AcceptorImpl implements Runnable {

    private Reactor reactor;

    private static final Logger logger = LoggerFactory.getLogger(AcceptorImpl.class);

    private NioSubSelector[] subSelector;

    // cpu 核心线程数
    int coreNum = Runtime.getRuntime().availableProcessors();

    public AcceptorImpl(Reactor reactor){
        logger.info("开始创建Acceptor对象");
        this.reactor = reactor;

        // 对每一个线程创建一个selector
        subSelector = new NioSubSelector[coreNum];
        for (int i = 0; i < coreNum; i++) {
            subSelector[i] = new NioSubSelector();
            // 在每个线程中运行subSelector
            ThreadPool.excute(subSelector[i]);
        }
        logger.info("创建Acceptor成功");
    }

    public void run() {
        try {
            System.out.println("accept");
            // 处理链接的请求
            SocketChannel accept = reactor.getServerSocketChannel().accept();
            // 把SocketChannel分发给不同selector
            dispatch(accept);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SocketChannel socketChannel){
        if(socketChannel != null) {
            NioSubSelector subSelector = getSubSelector();
            subSelector.start(socketChannel);
            subSelector.wakeup();
        }
    }

    private NioSubSelector getSubSelector(){
        int num = (int) (1 + Math.random() * (coreNum - 1));
        return subSelector[num];
    }

}
