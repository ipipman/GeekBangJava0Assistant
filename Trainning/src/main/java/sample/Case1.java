package sample;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 2:29 下午
 */
public class Case1 {

    //



    public static void main(String[] args) {
        Person person = new Person("小明", 1, new AdminAction(new Admin2Impl()));
        person.getAdminAction().action(person);

    }
}
