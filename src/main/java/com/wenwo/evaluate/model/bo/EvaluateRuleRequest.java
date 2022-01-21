package com.wenwo.evaluate.model.bo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wenwo.evaluate.constant.EnumValid;
import com.wenwo.evaluate.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author wyy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRuleRequest {


    @ApiModelProperty(value = "是否开启导入功能 open 开启 close:关闭",example = "open")
    @NotBlank(message = "请确认是否开启导入功能")
    @EnumValid(message = "请输入正确的导入功能 open/close",target = ActiveImportEnum.class)
    private String activeImport;

    @ApiModelProperty(value = "是否展示部门 show 展示 no_show 不展示",example = "no_show")
    @NotBlank(message = "请确认是否展示部门参数")
    @EnumValid(message = "请输入是否展示部门 show/no_show",target = DepartEnum.class)
    private String showDepart;

    @ApiModelProperty(value = "是否展示评价人 show 展示 no_show 不展示",example = "no_show")
    @NotBlank(message = "请输入发送时机")
    @EnumValid(message = "请输入是否展示评价人 show/no_show",target = EvaluatorEnum.class)
    private String showEvaluator;

    @ApiModelProperty(value = "是否立即发送 now:立即发送 delay:延迟发送",example = "now")
    @NotBlank(message = "请输入发送时机")
    @EnumValid(message = "请输入发送时机 now/delay",target = ActiveEnum.class)
    private String active;

    @ApiModelProperty(value = "功能 evaluate:环评")
    @NotBlank(message = "请输入导入数据隶属于那个功能")
    @EnumValid(message = "请输入导入数据隶属于那个功能 evaluate/",target = FunctionEnum.class)
    private String function;

    @ApiModelProperty(value = "延迟发送时 ：邮件激活时间",example = "2022-01-20")
    private String activeTime;
}
