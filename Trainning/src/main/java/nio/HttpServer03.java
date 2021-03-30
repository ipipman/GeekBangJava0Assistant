package nio;

import sun.print.resources.serviceui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package nio
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 9:24 下午
 */
public class HttpServer03 {

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final ServerSocket serverSocket = new ServerSocket(8083);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                TaskItem task = new TaskItem();
                task.setSocket(socket);
                executorService.execute(task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class TaskItem implements Runnable {
        private Socket socket;

        public void setSocket(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                String str = "hello";
                printWriter.println("HTTP/1.1 200 OK");
                printWriter.println("Content-Type:text/html;charset=utf-8");
                printWriter.println("Context-Length:" + printWriter.toString().length());
                printWriter.println();
                printWriter.write(str);
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


