package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package pool
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 11:55 上午
 */
public class CreateThreadPoolExecutor {

    static LongAdder taskIndex = new LongAdder();

    public static void main(String[] args) {
        ThreadPoolExecutor executor = initThreadPoolExecutor();
        List<Future<?>> futureList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            try{
                Future<?> future = executor.submit(new TaskCallable());
                futureList.add(future);
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
            }
        }
        futureList.forEach(future -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    static class TaskCallable implements Callable<Object> {
        @Override
        public Object call() throws Exception {
            System.out.println(Thread.currentThread().getName());
            taskIndex.increment();
            return taskIndex.intValue();
        }
    }


    // 创建线程池
    public static ThreadPoolExecutor initThreadPoolExecutor() {
        int coreSize = 1;
        int maxSize = 1;
        BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<Runnable>(20);
        CustomThreadFactory childThreadFactory = new CustomThreadFactory();
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                1,
                TimeUnit.MINUTES,
                taskQueue,
                childThreadFactory,
                rejectedExecutionHandler
        );
    }


    static class CustomThreadFactory implements ThreadFactory {
        private AtomicInteger serial = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("CustomThreadFactory[" + serial + "]");
            return thread;
        }
    }
}
