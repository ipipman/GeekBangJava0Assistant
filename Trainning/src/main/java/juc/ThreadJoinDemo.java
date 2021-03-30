package juc;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 10:16 下午
 */

public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {
        MyThread myThread  = new MyThread("AA");
        myThread.start();

        synchronized (myThread){
            for (int i = 0; i < 100; i++) {
                if (i == 20){
                    myThread.join();
                }
                System.out.println(Thread.currentThread().getName() + " --> " + i);
            }
        }
    }
}


class MyThread extends Thread {

    private String name;

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println(name + " --> " + i);
            }
        }
    }
}

