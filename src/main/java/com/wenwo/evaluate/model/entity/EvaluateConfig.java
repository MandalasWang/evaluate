package com.wenwo.evaluate.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_evaluate_config")
@ApiModel(value="t_evaluate_config对象", description="")
public class EvaluateConfig {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "是否开启导入功能")
    @TableField(value = "active_import")
    private String activeImport;

    @ApiModelProperty(value = "是否展示部门")
    @TableField(value = "show_depart")
    private String showDepart;

    @ApiModelProperty(value = "是否展示评价人")
    @TableField(value = "show_evaluator")
    private String showEvaluator;

    @ApiModelProperty(value = "是否立即发送 now:立即发送 delay:延迟发送")
    @TableField(value = "active")
    private String active;

    @ApiModelProperty(value = "功能 evaluate:环评",example = "evaluate")
    @TableField(value = "function")
    private String function;

    @ApiModelProperty(value = "延迟发送时 ：邮件激活时间")
    @TableField(value = "active_time")
    private String activeTime;

    @ApiModelProperty(value = "创建人")
    @TableField(value = "creator")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除")
    @TableField(value = "deleted")
    private Integer deleted;
}
