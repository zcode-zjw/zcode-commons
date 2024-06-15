package ${content.entity.classPackage};

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class ${content.entity.className} {
	<#list content.entity.attrs as item> 
	/**
	 * ${item.remarks!}
	 */
	private ${item.javaType} ${item.field}; 
	</#list>
}
