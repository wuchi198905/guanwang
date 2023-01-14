package com.example.guanwang.bean;

import lombok.Data;

@Data
public class LoginUser {
    /**
     * 验证码
     */
    private String phoneCode;
    /**
     * 用户
     */
    private UserInfo userInfo;

    /**
     * 用户token验证(header的键名)
     */
    private String token;
    /**
     *
     */
    private boolean login;
    private boolean stade;



}
