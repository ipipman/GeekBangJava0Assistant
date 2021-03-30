package juc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 9:39 下午
 */
public class ConcurrentHashMapDemo {

    static ConcurrentHashMap<Integer, Integer> hashMap = new ConcurrentHashMap<>();

    static CopyOnWriteArrayList<ConcurrentHashMap<Integer, Integer>> listHashMap = new CopyOnWriteArrayList<>();

    static ExecutorService executorService = Executors.newCachedThreadPool(new CustomFactory());

    static AtomicInteger threadNum = new AtomicInteger(0);

    static class CustomFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("this-thread-name-" + threadNum.incrementAndGet());
            return thread;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                hashMap.put(threadNum.get(), threadNum.get());
                listHashMap.add(hashMap);
                latch.countDown();
            });
        }

        latch.await();

        listHashMap.stream().parallel().forEach(map -> {
            map.forEach((key, value) -> {
                System.out.println(key + " --> " + value);
            });
        });
    }
}
