package sample;

/**
 * Created by ipipman on 2021/3/31.
 *
 * @version V1.0
 * @Package sample
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/31 2:36 下午
 */
public class Admin2Impl  extends AdminService {


    @Override
    public void action(Person person) {
        System.out.println("leader审批");
    }

}