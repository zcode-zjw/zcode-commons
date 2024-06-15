package com.zcode.zjw.common.file.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesEntity {
    @ExcelProperty(index = 0)
    private Integer no;  //工号
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private Double fund;
    @ExcelProperty(index = 3)
    private Double postSalary;
    @ExcelProperty(index = 4)
    private Double performanceOf;
    @ExcelProperty(index = 5)
    private Double allWork;
    @ExcelProperty(index = 6)
    private Double violations;
    @ExcelProperty(index = 7)
    private Double traffic;
    @ExcelProperty(index = 8)
    private Double communication;
}
