package juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 9:52 下午
 */
public class CopyOnWriteArrayListDemo {

    static CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        List<Future<Integer>> futureList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            //写
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    list.add(1);
                }
            });

            //读
            Future<Integer> task = executorService.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return list.get(list.size() - 1);
                }
            });
            futureList.add(task);
        }

        futureList.forEach(task -> {
            try {
                System.out.println(task.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("执行完毕 " + list.size());
    }
}
