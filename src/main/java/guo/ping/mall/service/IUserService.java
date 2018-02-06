package guo.ping.mall.service;

import guo.ping.mall.common.ServerResponse;
import guo.ping.mall.pojo.User;

/**
 * @author Kingdompin@163.com
 * @date 2018/2/6 上午9:41
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkVaild(String str, String type);

    ServerResponse<String> selectQuestions(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInfo(User user);
}
