package com.jet.module.UserManage.action;

import com.common.utils.ActionResult;
import com.jet.module.UserManage.service.IUserInfoService;
import com.sun.tools.internal.xjc.reader.RawTypeSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-07-31 18:08
 */
@Controller
@RequestMapping("/")
public class UserInfoAction {

    @Resource
    IUserInfoService userInfoService;

    @RequestMapping("/userInfoById")
    @ResponseBody
    public ActionResult getUserInofById(Integer id){
        ActionResult result = new ActionResult();
        result.setData(userInfoService.getUserInfoById(id));
        return result;
    }
    @RequestMapping("/error")
    //@ResponseBody
    public void getUser()throws Exception{
        ActionResult result = new ActionResult();
       // throw new Exception("eee");



    }
}
