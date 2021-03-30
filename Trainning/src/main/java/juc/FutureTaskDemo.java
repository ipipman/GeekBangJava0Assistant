package juc;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 10:09 下午
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            // run
            return new Random().nextInt();
        });
        new Thread(task).start();

        System.out.println(task.get());

    }
}
