package com.huiming.controller.backend;

import com.huiming.common.Const;
import com.huiming.common.ResponseCode;
import com.huiming.common.ServerResponse;
import com.huiming.pojo.User;
import com.huiming.service.ICategoryService;
import com.huiming.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/*
 * @author:15737
 * @createtime:2017/11/09 16:00
 * @desc:
*/
@Controller
@RequestMapping("manage/category")
public class CategoryManageController {

    @Autowired
    IUserService iUserService;

    @Autowired
    ICategoryService iCategoryService;

    @RequestMapping("addCategory.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "user did not login");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("no auth");
        }
    }

    @RequestMapping("setCategoryName.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "user did not login");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //更新categoryname
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("no auth");
        }
    }

    @RequestMapping("getChildrenParallelCategory.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "user did not login");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("no auth");
        }
    }

    @RequestMapping("getCategoryAndDeepChildrenParallelCategory.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenParallelCategory(HttpSession session,
                                                                     @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if (user == null) {
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "user did not login");
//        }
//        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
//
//        } else {
//            return ServerResponse.createByErrorMessage("no auth");
//        }
    }
}
