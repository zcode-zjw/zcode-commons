package com.zcode.zjw.common.web.query.controller;

import com.zcode.zjw.common.web.query.annotation.AggregateQuery;
import com.zcode.zjw.common.web.query.annotation.QueryParser;
import com.zcode.zjw.common.web.query.common.AggregateMapperArgsType;
import com.zcode.zjw.common.web.query.test.TestUser;
import com.zcode.zjw.web.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author zhangjiwei
 * @since 2023/8/11
 */
@RestController
@RequestMapping(value = "/test/")
public class TestController {

    @PostMapping(value = "testMethod")
    @AggregateQuery(vo = TestUser.class, mapper = "testUserMapper", mapperMethod = "updateById",
            mapperArgsKey = "updateParams", mapperArgsType = Object.class)
    public Result<Object> testMethod(@QueryParser("params") Object params) {
        return Result.success();
    }

}
