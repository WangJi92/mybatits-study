package com.module.userinfo.service;

import com.module.userinfo.entity.User;

import javax.validation.constraints.NotNull;

/**
 * descrption:
 * authohr: wangji
 * date: 2017-07-31 18:13
 */
public interface IUserInfoService {

    @NotNull(message = "不能为空")
    User getUserInfoById(@NotNull(message = "不能为空") Integer id);

    void save(@NotNull(message = "不能为空") User user);

}
