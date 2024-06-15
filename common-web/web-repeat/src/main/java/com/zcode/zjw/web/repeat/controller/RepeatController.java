package com.zcode.zjw.web.repeat.controller;

import com.zcode.zjw.common.utils.common.JwtUtil;
import com.zcode.zjw.common.utils.common.ThreadLocalUtil;
import com.zcode.zjw.configs.common.Result;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.security.entity.LoginUser;
import com.zcode.zjw.web.repeat.common.RepeatConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjiwei
 * @description 幂等控制器
 * @date 2023/2/9 上午10:44
 */
@RestController
@ApiLog
@RequestMapping("/repeat/")
@Api("幂等接口")
public class RepeatController {

    /**
     * 获取幂等Token
     *
     * @return Token
     */
    @GetMapping("getToken")
    @ResponseBody
    @ApiOperation("获取保持幂等的Token")
    public Result<?> getToken() {
        LoginUser loginUser = (LoginUser) ThreadLocalUtil.get("currentUser");
        return Result.success(JwtUtil.createJWT(loginUser.getUser().getId() + "", RepeatConstant.tokenSecretText));
    }

}
