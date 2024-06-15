package ${content.entity.classPackage};

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * ${content.entity.tableName}实体类
 * 
 * @author ${content.author}
 * @since ${.now}
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "${content.entity.tableName}", autoResultMap = true)
public class ${content.entity.className} implements Serializable {
	<#list content.entity.attrs as item> 
	/**
	 * ${item.remarks!}
	 */
	<#if item.javaType  == "JsonObject">
	@TableField(value = "${item.field}", typeHandler = JacksonTypeHandler.class)
	<#elseif item.field == "id" && item.javaType != "String">
	@TableId(type = IdType.AUTO)
	<#elseif item.field == "id" && item.javaType == "String">
	@TableId(type = IdType.ASSIGN_UUID)
	</#if>
	private ${item.javaType} ${item.field};
	</#list>
}
