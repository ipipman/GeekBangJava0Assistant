package classloader;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Created by ipipman on 2021/3/30.
 *
 * @version V1.0
 * @Package classloader
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/30 8:48 下午
 */


// 自定义类加载器
public class CustomiseClassLoader extends ClassLoader {

    private static final String THIS_DIR = "./Trainning/src/main/java/";

    public static void main(String[] args) throws Throwable {

        // 获取自定义加载类Hello
        Class<?> helloClazz = new CustomiseClassLoader().findClass(THIS_DIR + "classloader/Hello.xlass");
        Object helloInstance = helloClazz.newInstance();
        // 反射获取Hello方法
        Method helloMethod = helloClazz.getMethod("hello");
        // 执行hello方法输出：Hello，ClassLoader
        helloMethod.invoke(helloInstance);

    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // 读取Hello.xclass文件到Byte数组
            byte[] helloClassBytes = getContext(THIS_DIR + "/classloader/Hello.xlass");

            // 将读取到的字符串（x=255 -x）
            for (int i = 0; i < Objects.requireNonNull(helloClassBytes).length; i++) {
                helloClassBytes[i] = (byte) (255 - helloClassBytes[i]);
            }

            // 加载自定义Hello类
            return defineClass(name, helloClassBytes, 0, helloClassBytes.length);

        } catch (IOException e) {
            e.printStackTrace();
            return super.findClass(name);
        }
    }


    public static byte[] getContext(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset != buffer.length) {
            throw new IOException("error to read file " + file.getName());
        }
        fi.close();
        return buffer;
    }
}
