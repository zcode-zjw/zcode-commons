package ${content.controller.classPackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zcode.zjw.web.common.Result;
import com.zcode.zjw.common.web.query.annotation.AggregateQuery;
import com.zcode.zjw.common.web.query.annotation.QueryParser;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.common.utils.web.validate.ValidatorUtils;

import ${content.service.classPackage}.${content.service.className};
import ${content.entity.classPackage}.${content.entity.className};
import ${content.dao.classPackage}.${content.dao.className};

import java.util.Map;

/**
 * ${content.entity.className}的路由接口服务
 * 
 * @author ${content.author}
 * @since ${.now}
 */
@RestController
@ApiLog
@RequestMapping(value = "${content.controller.item.r_find.value}/")
public class ${content.controller.className} {

	/** ${content.entity.className}Service服务 */
	@Autowired
	private ${content.service.className} ${content.service.className?uncap_first};

	/**
	 * 聚合查询数据
	 *
	 * @param params 通过解析的前端参数
	 * @return 聚合查询的数据
	 */
	@PostMapping(value = "aggregate/findInfo")
	@AggregateQuery(vo = ${content.entity.className}.class, mapperClass = ${content.dao.className}.class)
	public Result<?> findInfo(@QueryParser("params") Object params) {
		return Result.success();
	}
	
	/**
	 * ${content.controller.item.f_find.describe!}
	 * @param value
	 * @return
	 */
	@PostMapping(value = "${content.controller.item.f_find.value}")
	@ResponseBody
	public Result<?> ${content.controller.item.f_find.value}(@RequestBody ${content.entity.className} value) {
		return ${content.service.className?uncap_first}.${content.service.item.select.value}(value);
	}
	
	<#if content.entity.primaryKeyAttr??>
	/**
	 * ${content.controller.item.f_getById.describe!}
	 * @param params 前端参数
	 * @return
	 */
	@PostMapping(value = "${content.controller.item.f_getById.value}")
	@ResponseBody
	public Result<?> ${content.controller.item.f_getById.value}(@RequestBody Map<String, Object> params) {
		ValidatorUtils.checkNull(params.get("id"), "ID");
		${content.entity.primaryKeyAttr.javaType} id = (${content.entity.primaryKeyAttr.javaType})params.get("id");
		return ${content.service.className?uncap_first}.${content.service.item.selectById.value!}(id);
	}
	</#if>
	
	/**
	 * ${content.controller.item.f_saveNotNull.describe!}
	 * @param value 前端参数
	 * @return
	 */
	@PostMapping(value = "${content.controller.item.r_saveNotNull.value!}", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public Result<?> ${content.controller.item.f_saveNotNull.value!}(@RequestBody ${content.entity.className} value) {
		return ${content.service.className?uncap_first}.${content.service.item.insertNotNull.value!}(value);
	}
	
	<#if content.entity.primaryKeyAttr??>
	/**
	 * ${content.controller.item.f_updateNotNull.describe!}
	 * @param value 前端参数
	 * @return
	 */
	@PostMapping(value = "${content.controller.item.f_updateNotNull.value!}", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public Result<?> ${content.controller.item.f_updateNotNull.value!}(@RequestBody ${content.entity.className} value) {
		return ${content.service.className?uncap_first}.${content.service.item.updateNotNull.value!}(value);
	}

	/**
	 * ${content.controller.item.f_deleteById.describe!}
	 * @param params 前端参数
	 * @return
	 */
	@PostMapping(value = "${content.controller.item.f_deleteById.value!}", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public Result<?> ${content.controller.item.f_deleteById.value!}(@RequestBody Map<String, Object> params) {
		ValidatorUtils.checkNull(params.get("id"), "ID");
		${content.entity.primaryKeyAttr.javaType} id = (${content.entity.primaryKeyAttr.javaType})params.get("id");
		return ${content.service.className?uncap_first}.${content.service.item.deleteById.value!}(id);
	}
	</#if>
}
