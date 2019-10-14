package subselector;

import abstracts.AbstractHandler;
import handler.ReadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

// 子selector,用于获取数据和写入数据
public class NioSubSelector implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(NioSubSelector.class);

    private Selector subSelector;

    public NioSubSelector(){
        try {
            this.subSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(SocketChannel socketChannel){
        new ReadHandler(subSelector,socketChannel);
    }

    public void wakeup(){
        subSelector.wakeup();
    }

    public void select(){
        try {
            while (true){
                logger.info("开始执行selector,threadName:{}",Thread.currentThread().getName());
                subSelector.select();
                Set<SelectionKey> selectionKeys = subSelector.selectedKeys();
                logger.info("获取到selector,threadName:{}",Thread.currentThread().getName());
                for (SelectionKey selectionKey : selectionKeys) {
                    dispatch(selectionKey);
                }
                selectionKeys.clear();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey selectionKey){
        AbstractHandler attachment = (AbstractHandler)selectionKey.attachment();
        // 单线程操作
        attachment.doHandler();
    }

    @Override
    public void run() {
        select();
    }
}
