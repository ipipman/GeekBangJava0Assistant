package juc;

import javafx.util.Pair;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 11:38 上午
 */
public class ReentrantReadWriteLockDemo {

    static AtomicInteger num = new AtomicInteger(1);

    // 可重入读写锁
    public static void main(String[] args) {

        ReadWriteLock lock = new ReentrantReadWriteLock(true);
        for (int i = 0; i < 100; i++) {
            // 写
            new Thread(() -> {
                try {
                    writeLock(lock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();


            // 读1
            new Thread(() -> {
                readLock(lock, "A");
            }).start();

            // 读2
            new Thread(() -> {
                readLock(lock, "B");
            }).start();
        }


    }


    public static void writeLock(ReadWriteLock lock) throws InterruptedException {
        try {
            lock.writeLock().lock();
            num.decrementAndGet();
            num.incrementAndGet();
            Thread.sleep(200);
            System.out.println("写锁写了[" + num.get() + "]");
        } finally {
            System.out.println("写锁释放[" + num.get() + "]");
            lock.writeLock().unlock();
        }
    }

    public static void readLock(ReadWriteLock lock, String name) {
        try {
            lock.readLock().lock();
            System.out.println("[" + name + "]读锁读了[" + num.get() + "]");
        } finally {
            System.out.println("[" + name + "]读锁释放[" + num.get() + "]");
            lock.readLock().unlock();
        }
    }
}


