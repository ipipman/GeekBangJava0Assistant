package juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 9:43 上午
 */
public class LockConditionDemo {

    static AtomicInteger num = new AtomicInteger(1);

    public static void main(String[] args) {

        // 可重入锁
        ReentrantLock lock = new ReentrantLock(true);
        // 生产者锁
        Condition producerLock = lock.newCondition();
        // 消费者锁
        Condition consumerLock = lock.newCondition();

        for (int i = 0; i < 1000; i++) {
            if (num.get() < 10) {
                // 生产
                new Thread(() -> {
                    try {
                        producer(lock, producerLock, consumerLock);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            if (num.get() >= 10) {
                // 消费
                new Thread(() -> {
                    try {
                        consumer(lock, producerLock, consumerLock);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    // 生产者
    public static void producer(ReentrantLock lock, Condition producerLock, Condition consumerLock)
            throws InterruptedException {

        lock.lock();
        for (int i = 0; i < 10; i++) {
            num.incrementAndGet();
            System.out.println("生产 -> " + num.get());
        }
        Thread.sleep(1000);
        producerLock.await();
        consumerLock.signalAll();
        lock.unlock();
    }

    // 消费者
    public static void consumer(ReentrantLock lock, Condition producerLock, Condition consumerLock)
            throws InterruptedException {

        lock.lock();
        for (int i = num.intValue(); i > 0; i--) {
            num.getAndDecrement();
            System.out.println("消费 -> " + num.get());
        }
        Thread.sleep(1000);
        consumerLock.await();
        producerLock.signalAll();
        lock.unlock();
    }

}
