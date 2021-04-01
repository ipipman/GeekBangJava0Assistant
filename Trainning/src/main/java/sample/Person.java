package sample;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 2:30 下午
 */
public class Person {

    private String name;

    private Integer day;

    private AdminAction adminAction;

    public Person(String name, Integer day, AdminAction adminAction){
        this.name = name;
        this.day = day;
        this.adminAction = adminAction;
    }

    public AdminAction getAdminAction() {
        return adminAction;
    }

    public String getName() {
        return name;
    }

    public Integer getDay() {
        return day;
    }

}
