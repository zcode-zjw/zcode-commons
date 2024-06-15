package com.zcode.zjw.log.trace.domain.core.subject.asm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 方法简单对象
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AsmMethodPojo implements Serializable {

    private Integer opcode;

    private String owner;

    private String name;

    private String descriptor;

    private Boolean isInterface;

    public AsmMethodPojo(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsmMethodPojo that = (AsmMethodPojo) o;
        return Objects.equals(owner, that.owner) && Objects.equals(name, that.name) && Objects.equals(descriptor, that.descriptor) && Objects.equals(isInterface, that.isInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor, isInterface);
    }
}
