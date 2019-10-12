package thread;

import com.google.common.util.concurrent.*;
import interfaces.CallBack;
import interfaces.Listener;

import java.util.concurrent.*;

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

    public static <T> void submit(Callable callable, CallBack callback){
        ListenableFuture<byte[]> submit = exctorService.submit(callable);
        Futures.addCallback(submit, new FutureCallback<byte[]>() {
            @Override
            public void onSuccess(byte[] result) {
                callback.callback(result);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.fail();
            }
        });
    }

    public void addListenr(Runnable r, Listener listener){
        ListenableFuture<?> submit = exctorService.submit(r);
        submit.addListener(listener,exctorService);
    }

    public static void excute(Runnable runnable){
        exctorService.execute(runnable);
    }
}
