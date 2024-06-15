package com.zcode.zjw.configs.controller;


import com.zcode.zjw.configs.common.ConfigPropertiesTypeEnum;
import com.zcode.zjw.configs.common.Result;
import com.zcode.zjw.configs.service.ConfigPropertiesService;
import com.zcode.zjw.log.api.annotation.ApiLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangjiwei
 * @description 配置文件控制器
 * @date 2022/12/2 下午9:50
 */
@RestController
@ApiLog
@RequestMapping("/config/properties/")
@Api(tags = "系统配置文件接口")
public class ZcodeConfigPropertiesController {

    private final ConfigPropertiesService configPropertiesService;

    public ZcodeConfigPropertiesController(ConfigPropertiesService configPropertiesService) {
        this.configPropertiesService = configPropertiesService;
    }

    /**
     * 获取配置文件数据列表
     *
     * @param params 配置文件参数
     * @return 配置文件数据列表
     * @throws IOException
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "查询参数", dataType = "Map<String, Object>", paramType = "query", required = true)
    })
    @GetMapping("findData")
    @ResponseBody
    @ApiOperation("获取配置文件数据列表")
    public Result<?> findData(@RequestParam Map<String, Object> params) throws IOException {
        return Result.success(configPropertiesService.findData(
                ConfigPropertiesTypeEnum.selectType((String) params.get("fileName")), params));
    }

    /**
     * 删除
     *
     * @param data
     * @return
     */
    @PostMapping("delData")
    @ResponseBody
    @ApiOperation("删除配置文件数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "删除参数", dataType = "Map<String, Object>", paramType = "data", required = true)
    })
    public Result<?> delData(@RequestBody Map<String, Object> data) {
        return Result.success(configPropertiesService.delData(ConfigPropertiesTypeEnum.selectType((String) data.get("fileName")), data));
    }

    /**
     * 更新
     *
     * @param data
     * @return
     */
    @PostMapping("updateData")
    @ResponseBody
    @ApiOperation("更新配置文件数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "更新参数", dataType = "Map<String, Object>", paramType = "data", required = true)
    })
    public Result<?> updateData(@RequestBody Map<String, Object> data) {
        int res;
        String fileName = (String) data.get("fileName");
        // 如果没有指定文件名称，说明是批量更新
        if (fileName == null && data.get("data") != null) {
            res = configPropertiesService.batchUpdateData((List<Map<String, Object>>) data.get("data"));
        } else {
            res = configPropertiesService.updateData(ConfigPropertiesTypeEnum.selectType(fileName), data);
        }
        return Result.success(res);
    }

}
