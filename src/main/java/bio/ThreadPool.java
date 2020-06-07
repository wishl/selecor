package bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private static int coreSize = 5;
    private static int maxSize = 10;
    private static int keepAlive = 0;
    private static BlockingQueue queue = new ArrayBlockingQueue(1024);

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize, maxSize,
            keepAlive, TimeUnit.SECONDS, queue, (r, e)-> {
    });

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

}
