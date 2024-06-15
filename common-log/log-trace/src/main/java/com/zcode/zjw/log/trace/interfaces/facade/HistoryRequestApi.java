package com.zcode.zjw.log.trace.interfaces.facade;

import com.zcode.zjw.common.utils.web.validate.ValidatorUtils;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.log.trace.domain.request.repository.po.ApiRequestRecord;
import com.zcode.zjw.log.trace.domain.request.service.HistoryRequestService;
import com.zcode.zjw.web.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 历史请求Api
 *
 * @author zhangjiwei
 * @since 2023/8/9
 */
@ApiLog
@RestController
@RequestMapping(value = "/api/history/")
public class HistoryRequestApi {

    private final HistoryRequestService historyRequestService;

    public HistoryRequestApi(HistoryRequestService historyRequestService) {
        this.historyRequestService = historyRequestService;
    }

    /**
     * 发送历史请求
     *
     * @param params 请求参数
     * @return
     */
    @PostMapping(value = "send")
    @ResponseBody
    public Result<Object> sendHistoryRequest(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        ValidatorUtils.checkNull(id, "id");
        return historyRequestService.sendHistoryRequest(String.valueOf(id));
    }

    /**
     * 获取历史请求信息
     *
     * @param apiRequestRecord 请求参数
     * @return
     */
    @PostMapping(value = "find")
    @ResponseBody
    public Result<Object> findHistoryRequest(@RequestBody ApiRequestRecord apiRequestRecord) {
        return historyRequestService.findHistoryRecord(apiRequestRecord);
    }

}
