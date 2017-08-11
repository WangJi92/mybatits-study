package com.module.userinfo.action;

import com.common.utils.ActionResult;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-07-31 18:08
 */
@Controller
@RequestMapping("/")
public class UserInfoAction {

    @Autowired
    public IUserInfoService userInfoService;

    @RequestMapping("/findById")
    @ResponseBody
  public ActionResult findById(Integer id){
        ActionResult result = new ActionResult(true);
       User user = userInfoService.getUserInfoById(id);
       if(user !=null){
           result.setData(user);
       }else{
           result.setMessage("error");
           result.setSuccess(false);
       }
       return result;
  }

}
