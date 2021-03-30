package juc;

import java.util.concurrent.CompletableFuture;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package juc
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 10:00 下午
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {

        String result1 = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        }).thenApplyAsync(v -> v + " word").join();
        System.out.println(result1);


        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });
        task1.thenAccept(v -> {
            System.out.println(v + " ====> 消费");
        });

    }
}
