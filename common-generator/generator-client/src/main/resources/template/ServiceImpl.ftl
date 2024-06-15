package ${content.serviceImpl.classPackage};
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import ${content.service.classPackage}.${content.service.className};
import ${content.dao.classPackage}.${content.dao.className};
import ${content.assist.classPackage}.${content.assist.className};
import ${content.entity.classPackage}.${content.entity.className};
import com.zcode.zjw.web.common.Result;

/**
 * ${content.entity.className}的服务接口的实现类
 * 
 * @author ${content.author}
 * @since ${.now}
 */
@Service
public class ${content.serviceImpl.className} implements ${content.service.className} {
	private final Logger LOG = LogManager.getLogger(this.getClass());

	@Autowired
	private ${content.dao.className} ${content.dao.className?uncap_first};

	@Override
	public Result<?> ${content.service.item.select.value!}(${content.entity.className} value) {
		//TODO这里可以做通过Assist做添加查询
		List<${content.entity.className}> result = ${content.dao.className?uncap_first}.${content.dao.item.select.value!}(null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行获取${content.entity.className}数据集-->结果:", result);
		}
		return Result.success(result);
	}
	<#if content.entity.primaryKeyAttr??>
	@Override
	public Result<?> ${content.service.item.selectById.value!}(${content.entity.primaryKeyAttr.javaType} id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过${content.entity.className}的id获得${content.entity.className}对象-->失败:id不能为空");
			}
			return Result.error("id不能为空");
		}
		${content.entity.className} result = ${content.dao.className?uncap_first}.${content.dao.item.selectById.value!}(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过${content.entity.className}的id获得${content.entity.className}对象-->结果:", result);
		}
		return Result.success(result);
	}
	</#if>

	@Override
	public Result<?> ${content.service.item.insertNotNull.value!}(${content.entity.className} value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将${content.entity.className}中属性值不为null的数据保存到数据库-->失败:对象不能为空");
			}
			return Result.error("参数对象不能为空");
		}
		<#if content.entity.cantNullAttrs?exists>
		if(<#list content.entity.cantNullAttrs as item>value.${item.fget}() == null <#if item?has_next>||</#if> </#list>){
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行将${content.entity.className}中属性值不为null的数据保存到数据库-->失败:存在不能为空的空值");
			}
			return Result.error("参数对象中存在不能为空的空值");
		}
		</#if>
		int result = ${content.dao.className?uncap_first}.${content.dao.item.insertNotNull.value!}(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行将${content.entity.className}中属性值不为null的数据保存到数据库-->结果:", result);
		}
		return Result.success(result);
	}
	<#if content.entity.primaryKeyAttr??>
	@Override
	public Result<?> ${content.service.item.updateNotNull.value!}(${content.entity.className} value) {
		if (value == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过${content.entity.className}的id更新${content.entity.className}中属性不为null的数据-->失败:对象为null");
			}
			return Result.error("参数对象为null");
		}
		int result = ${content.dao.className?uncap_first}.${content.dao.item.updateNotNullById.value!}(value);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过${content.entity.className}的id更新${content.entity.className}中属性不为null的数据-->结果:", result);
		}
		return Result.success(result);
	}

	@Override
	public Result<?> ${content.service.item.deleteById.value!}(${content.entity.primaryKeyAttr.javaType} id) {
		if (id == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("执行通过${content.entity.className}的id删除${content.entity.className}-->失败:id不能为空");
			}
			return Result.error("id不能为空");
		}
		int result = ${content.dao.className?uncap_first}.${content.dao.item.deleteById.value!}(id);
		if (LOG.isDebugEnabled()) {
			LOG.debug("执行通过${content.entity.className}的id删除${content.entity.className}-->结果:", result);
		}
		return Result.success(result);
	}
	</#if>


}