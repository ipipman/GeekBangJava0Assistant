package demo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by ipipman on 2021/3/23.
 *
 * @version V1.0
 * @Package demo
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/23 8:56 下午
 */
public class Demo {

    public synchronized void setA() {

    }

    public static synchronized void setB(){

    }

    public static void main(String[] args) {
        byte a = (byte) 127;
        System.out.println(a);
        a = (byte) ((byte) a + 1);
        System.out.println(a);
        a = (byte) ((byte) a + 1);
        System.out.println(a);


        TreeMap map = new TreeMap();
        ArrayList<Integer> b = new ArrayList<Integer>();
        b.size();

        LinkedList<Integer> l1  = new LinkedList<Integer>();
        l1.size();
    }
}
