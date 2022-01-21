package com.wenwo.evaluate.model.property;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Data;

@Data
@ColumnWidth(value = 15)
@ContentRowHeight(value = 14)
public class EvaluateProperty {


    @ExcelProperty(value = "序号",index = 0)
    private Integer num;

    @ExcelProperty(value = "部门",index = 1)
    private String depart;

    @ExcelProperty(value = "评价人",index = 2)
    private String evaluator;

    @ExcelProperty(value = {"1-能力","标准"},index = 3)
    private String powerStandard;

    @ExcelProperty(value = {"1-能力","事例说明"},index = 4)
    private String powerExample;

    @ExcelProperty(value = {"1-能力","改进建议"},index = 5)
    private String powerAdvice;


    @ExcelProperty(value = {"2-态度","标准"},index = 6)
    private String attitudeStandard;

    @ExcelProperty(value = {"2-态度","事例说明"},index = 7)
    private String attitudeExample;

    @ExcelProperty(value = {"2-态度","改进建议"},index = 8)
    private String attitudeAdvice;
}
