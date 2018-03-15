package guo.ping.mall.controller.back;

import com.google.common.collect.Maps;
import guo.ping.mall.common.Const;
import guo.ping.mall.common.ResonseCode;
import guo.ping.mall.common.ServerResponse;
import guo.ping.mall.pojo.Product;
import guo.ping.mall.pojo.User;
import guo.ping.mall.service.IFileService;
import guo.ping.mall.service.IProductService;
import guo.ping.mall.service.IUserService;
import guo.ping.mall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Kingdompin@163.com
 * @date 2018/3/3 上午11:41
 */
@Controller
@RequestMapping("manager/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse getSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResonseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员账户！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户未登录，请登录管理员账户！");
            return resultMap;
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);

            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败！");
                return resultMap;
            }

            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功！");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作！");
            return resultMap;
        }
    }
}
