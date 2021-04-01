package sample;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 2:46 下午
 */
public class AdminAction {

    private AdminService admin;

    public AdminAction(AdminService admin) {
        this.admin = admin;
    }

    public void action(Person person) {
        if (admin instanceof Admin1Impl) {
            admin.action(person);

        } else if (admin instanceof Admin2Impl) {

            // leader
            admin.action(person);

            // boss
            Admin1Impl admin1 = (Admin1Impl) admin;
            admin1.action(person);
        }


    }
}
