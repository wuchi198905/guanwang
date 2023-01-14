package com.example.guanwang.controller;


import com.example.guanwang.Utils.RedisUtils;
import com.example.guanwang.base.json.ApiUtil;
import com.example.guanwang.base.json.RC;
import com.example.guanwang.base.json.Result;
import com.example.guanwang.bean.LoginUser;
import com.example.guanwang.bean.UserInfo;
import com.example.guanwang.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 陈志浩123
 * @since 2023-01-14
 */

@Slf4j
@Api(description = "后台用户管理接口")
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisUtils redisUtils;
    @ApiOperation(value = "登录接口", notes = "登录系统")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", paramType = "query", required = true, dataType = "string" ),
            @ApiImplicitParam(name = "username", value = "username", paramType = "query", required = true, dataType = "string" ),
            @ApiImplicitParam(name = "password", value = "username", paramType = "query", required = true, dataType = "string" ),
            @ApiImplicitParam(name = "code", value = "验证码", paramType = "query", required = true, dataType = "string" ),
    })
    @ResponseBody
    @RequestMapping(path = "/login", method = {RequestMethod.POST})
    public String login(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getParameter("token");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String  code= request.getParameter("code");

        if(!redisUtils.exists(token)){
            return Result.Result("30004","验证码已失效请重新获取",
                    ApiUtil.generateToken());
        }
        String  oldcode= (String) redisUtils.get(token);
        log.info(oldcode);
        log.info(token);
        if(StringUtils.isBlank(code)){
            return Result.Result("30002","验证码不能为空", ApiUtil.generateToken());
        }

        if(!StringUtils.equalsIgnoreCase(code, oldcode)){
            return Result.Result("30001","验证码不正确请重新获取", ApiUtil.generateToken());
        }
        UserInfo memberInfo=new UserInfo();
        memberInfo.setPassword(password);
        memberInfo.setUsername(username);
        LoginUser loginUser = userInfoService.login(memberInfo);
        redisUtils.remove(token);
        if(loginUser.isLogin()){
            return Result.Result(RC.SUCCESS,loginUser.getToken());
        }
        return Result.Result(RC.LOGIN_USER_ISNOT);
    }

}

