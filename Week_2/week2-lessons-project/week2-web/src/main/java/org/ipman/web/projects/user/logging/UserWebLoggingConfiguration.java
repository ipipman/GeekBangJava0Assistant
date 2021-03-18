package org.ipman.web.projects.user.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.logging
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 3:24 下午
 */
public class UserWebLoggingConfiguration {


    public UserWebLoggingConfiguration() throws Exception {
        // 通过代码（硬编码）的方式调整日志级别
        Logger logger = Logger.getLogger("org.ipman");
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setEncoding("UTF-8");
        consoleHandler.setLevel(Level.INFO);

        // 添加一个 Console Handler
        logger.addHandler(consoleHandler);
    }

    public static void main(String[] args) throws IOException {
        Logger logger = Logger.getLogger("org.ipman");

        // 获取当前线程的 类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("META-INF/logging.properties")) {
            // 获取Logging Manager
            LogManager logManager = LogManager.getLogManager();
            // 引入外部配置，读取 Logging 配置
            logManager.readConfiguration(inputStream);
        }

        logger.info("Hello, World");
        logger.warning("2021");
    }
}
