package com.module.userinfo.action;

import com.common.utils.ActionResult;
import com.module.userinfo.entity.User;
import com.module.userinfo.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-07-31 18:08
 */
@Controller
@RequestMapping("/")
@Slf4j
public class UserInfoAction {

    @Autowired
    public IUserInfoService userInfoService;

    @RequestMapping("/findById")
    @ResponseBody
    public ActionResult findById(Integer id) {
        ActionResult result = new ActionResult(true);
        User user = userInfoService.getUserInfoById(id);
        if (user != null) {
            result.setData(user);
        } else {
            result.setMessage("error");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping("/error")
    @ResponseBody
    public ActionResult saveUser(@Valid User user, Errors errors) {
        ActionResult result = new ActionResult(true);
        if (errors.hasErrors()) {
            List<FieldError> list = errors.getFieldErrors();
            for(FieldError error :list){
                log.info(error.getField());
                log.info(error.getCode());
                log.info(error.getDefaultMessage());
            }
            result.setData(errors.getFieldErrors());
        }
        return result;
    }

}
