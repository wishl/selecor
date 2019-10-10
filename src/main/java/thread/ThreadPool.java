package thread;

import com.google.common.util.concurrent.*;
import interfaces.Handler;
import interfaces.Listener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private static final ListeningExecutorService exctorService;

    private static int coreSize = 16;
    private static int maxSize = 32;
    private static int keepAlive = 0;
    private static TimeUnit timeUnit = TimeUnit.SECONDS;
    private static BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue(1000);

    static {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxSize, keepAlive, timeUnit, blockingQueue, (Runnable r, ThreadPoolExecutor executor1) -> {
            System.out.println("队列超长抛弃");
        });
        exctorService = MoreExecutors.listeningDecorator(executor);
    }

    public static <T> void submit(Handler handler){
        ListenableFuture submit = exctorService.submit(handler);
        Futures.addCallback(submit, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                handler.callback(result);
            }

            @Override
            public void onFailure(Throwable t) {
                handler.fail();
            }
        });
    }

    public void addListenr(Runnable r, Listener listener){
        ListenableFuture<?> submit = exctorService.submit(r);
        submit.addListener(listener,exctorService);
    }
}
