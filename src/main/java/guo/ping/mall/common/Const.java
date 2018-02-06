package guo.ping.mall.common;

/**
 * 常量类
 * @author Kingdompin@163.com
 * @date 2018/2/6 上午10:53
 */
public class Const {
    public static final String CURRENT_USER = "current_user";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role{
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
