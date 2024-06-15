package com.zcode.zjw.log.trace.interfaces.facade;

import com.zcode.zjw.common.utils.web.validate.ValidatorUtils;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.log.trace.domain.core.service.TraceMethodManager;
import com.zcode.zjw.log.trace.domain.core.subject.monitor.ApiRecord;
import com.zcode.zjw.log.trace.interfaces.assembler.TraceMethodRequestAssembler;
import com.zcode.zjw.log.trace.interfaces.dto.TraceRequestDTO;
import com.zcode.zjw.log.trace.test.TestService;
import com.zcode.zjw.web.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 追踪方法日志控制器
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@ApiLog
@RestController
@RequestMapping(value = "/log/trace/")
@ApiRecord
public class LogTraceMethodApi {

    private final TraceMethodManager traceMethodManager;

    public LogTraceMethodApi(TraceMethodManager traceMethodManager) {
        this.traceMethodManager = traceMethodManager;
    }

    @Autowired
    private TestService testService;

    /**
     * 追踪开始
     *
     * @param traceRequestDTO 请求DTO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "start")
    @ResponseBody
    public Result<Object> startTrace(@RequestBody @Validated TraceRequestDTO traceRequestDTO) throws Exception {
        TraceMethodRequestAssembler traceMethodRequestAssembler = new TraceMethodRequestAssembler();
        return Result.success(traceMethodManager.startTrace(traceMethodRequestAssembler.toDo(traceRequestDTO)));
    }

    /**
     * 回退操作
     *
     * @param params 请求参数
     * @return
     */
    @PostMapping(value = "backup")
    @ResponseBody
    public Result<?> backup(@RequestBody Map<String, Object> params) {
        String flowId = String.valueOf(params.get("flowId"));
        ValidatorUtils.checkNull(flowId, "flowId");
        return traceMethodManager.backup(flowId) ? Result.success() : Result.error("回退失败");
    }

    /**
     * 重载方法
     *
     * @param params 请求参数
     * @return
     * @throws Exception
     */
    @PostMapping(value = "reload")
    @ResponseBody
    public Result<Object> reload(@RequestBody Map<String, Object> params) throws Exception {
        Object classpath = params.get("classpath");
        ValidatorUtils.checkNull(classpath, "classpath");
        traceMethodManager.hotLoad(String.valueOf(classpath));
        return Result.success();
    }

    /**
     * 获取调用的全部方法
     *
     * @param traceRequestDTO 请求参数
     * @return
     */
    @PostMapping(value = "findCallMethod")
    @ResponseBody
    public Result<Object> findCallMethod(@RequestBody @Validated TraceRequestDTO traceRequestDTO) {
        TraceMethodRequestAssembler traceMethodRequestAssembler = new TraceMethodRequestAssembler();
        return traceMethodManager.getCallMethod(traceMethodRequestAssembler.toDo(traceRequestDTO));
    }

    /**
     * 获取被调用的全部方法
     *
     * @param traceRequestDTO 请求参数
     * @return
     */
    @PostMapping(value = "findCalledMethod")
    @ResponseBody
    public Result<Object> findCalledMethod(@RequestBody @Validated TraceRequestDTO traceRequestDTO) {
        TraceMethodRequestAssembler traceMethodRequestAssembler = new TraceMethodRequestAssembler();
        return traceMethodManager.getCalledMethod(traceMethodRequestAssembler.toDo(traceRequestDTO));
    }

    @GetMapping("testMethod")
    public Result<?> testMethod() throws Exception {
        testService.test();
        return Result.success();
    }

}
