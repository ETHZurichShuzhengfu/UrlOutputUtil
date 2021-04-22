package threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:szf
 * desc:线程池
 * date:2021/2/23
 */
public class UrlUtilThreadPool {

    private UrlUtilThreadPool() {
    }

    private volatile static ExecutorService threadPool = null;

    public static ExecutorService getThreadPool() {
        if (threadPool == null) {
            synchronized (UrlUtilThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPoolExecutor(5, 10, 3,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }
        return threadPool;
    }
}
