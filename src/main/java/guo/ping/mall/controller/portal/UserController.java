package guo.ping.mall.controller.portal;

import guo.ping.mall.common.Const;
import guo.ping.mall.common.ResonseCode;
import guo.ping.mall.common.ServerResponse;
import guo.ping.mall.pojo.User;
import guo.ping.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Kingdompin@163.com
 * @date 2018/2/6 上午9:36
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱是否有效
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "/check_vaild.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkVaild(String str, String type){
        return iUserService.checkVaild(str, type);
    }

    /**
     * 获取用户信息（不经过数据库直接查询session中保存的信息）
     * @param session
     * @return
     */
    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }


    /**
     * 忘记密码问题查询
     * @param username
     * @return
     */
    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestions(username);
    }


    /**
     * 校验答案是否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }


    /**
     * 忘记密码后重设密码
     * @param username
     * @param passwordNew
     * @param token
     * @return
     */
    @RequestMapping(value = "/forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String token){
        return iUserService.forgetResetPassword(username, passwordNew, token);
    }


    /**
     * 重新修改密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "/reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    /**
     * 修改用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "/update_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(HttpSession session, User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInfo(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }


    /**
     * 查询当前用户信息(根据session中保存的用户id走数据库查询返回)
     * @param session
     * @return
     */
    @RequestMapping(value = "/get_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，需要先登录");
        }
        return iUserService.getInfo(currentUser.getId());
    }
}
