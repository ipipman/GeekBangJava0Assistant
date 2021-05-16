package org.geektimes.management.demo;

import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pListRequest;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.jolokia.client.request.J4pResponse;
import org.jolokia.client.request.J4pWriteRequest;

import java.util.Map;
import java.util.Optional;

public class JolokiaDemo {

    private static J4pClient j4pClient = J4pClient.url("http://localhost:8080/jolokia")
            .connectionTimeout(3000)
            .build();

    private final static String USER_MBEAN_NAME = "org.geektimes.projects.user.management:type=User";

    public static void main(String[] args) throws Exception {

        // 打印内存占用信息
        heapMemoryUsage();

        // 查询所有MBean
        Map<String, Object> rootMBeans = listMBean(null);
        System.out.println("Root MBeans: " + rootMBeans);

        // 查询 user.management下的MBean
        Map<String, Object> userMBeans = listMBean("org.geektimes.projects.user.management");
        System.out.println("User MBeans: " + userMBeans);

        // 写入 UserMBean 属性
        writeMBean(USER_MBEAN_NAME, "Id", 1L);
        writeMBean(USER_MBEAN_NAME, "Password", "******");
        writeMBean(USER_MBEAN_NAME, "Name", "zhangsan");
        writeMBean(USER_MBEAN_NAME, "Email", "xxx@xx.com");
        writeMBean(USER_MBEAN_NAME, "PhoneNumber", "13688888888");

        // 读取 UserMBean 属性
        Object userMBean = readMBean(USER_MBEAN_NAME, "User");
        System.out.println("Read UserMBean: " + userMBean);

        // 执行 UserMBean toString()方法
        Object result = execMbean(USER_MBEAN_NAME, "toString");
        System.out.println("Exec UserMBean: " + result);
    }

    public static void writeMBean(String mBeanName, String attribute, Object value) throws Exception {
        J4pWriteRequest writeRequest = new J4pWriteRequest(mBeanName, attribute, value);
        J4pResponse<J4pWriteRequest> response = j4pClient.execute(writeRequest);
        System.out.println(String.format("Write MBean: attribute=%s, value=%s", attribute,  response.getValue()));
    }

    public static Object readMBean(String mBeanName, String... attributes) throws Exception {
        J4pReadRequest readRequest = new J4pReadRequest(mBeanName, attributes);
        return j4pClient.execute(readRequest).getValue();
    }

    public static Object execMbean(String mBeanName, String methodName, String... args) throws Exception {
        J4pExecRequest execRequest = new J4pExecRequest(mBeanName, methodName, args);
        return j4pClient.execute(execRequest).getValue();
    }

    public static Map<String, Object> listMBean(String path) throws Exception {
        String root = "/";
        path = Optional.ofNullable(path).orElse(root);
        J4pListRequest j4pListRequest = new J4pListRequest(path);
        return j4pClient.execute(j4pListRequest).getValue();
    }

    public static void heapMemoryUsage() throws Exception {
        J4pReadRequest req = new J4pReadRequest("java.lang:type=Memory",
                "HeapMemoryUsage");
        J4pReadResponse resp = j4pClient.execute(req);
        Map<String, Long> vals = resp.getValue();
        long used = vals.get("used");
        long max = vals.get("max");
        int usage = (int) (used * 100 / max);
        System.out.println("Memory usage: used: " + used +
                " / max: " + max + " = " + usage + "%");
    }

}