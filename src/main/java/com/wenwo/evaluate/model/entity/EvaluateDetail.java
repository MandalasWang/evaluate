package com.wenwo.evaluate.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wenwo.evaluate.model.property.EvaluateProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.BeanUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("t_evaluate_detail")
@ApiModel(value="evaluate_detail对象", description="")
public class EvaluateDetail {


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "评价人")
    @TableField("evaluator")
    private String evaluator;


    @TableField("depart")
    private String depart;

    @ApiModelProperty(value = "被评价人")
    @TableField("name")
    private String name;

    @TableField("power_standard")
    private String powerStandard;

    @TableField("power_example")
    private String powerExample;

    @TableField("power_advice")
    private String powerAdvice;


    @TableField("attitude_standard")
    private String attitudeStandard;

    @TableField("attitude_example")
    private String attitudeExample;

    @TableField("attitude_advice")
    private String attitudeAdvice;

    @TableField("active_time")
    private String activeTime;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("deleted")
    private String deleted;


    public static EvaluateDetail convert(EvaluateProperty property){
        if(property == null){
            return null;
        }
        EvaluateDetail detail = new EvaluateDetail();
        BeanUtils.copyProperties(property,detail);
        detail.setCreateTime(LocalDate.now().toString());
        return detail;
    }


    public static List<EvaluateDetail> convert(List<EvaluateProperty> list){
        if(list.isEmpty()){
            return null;
        }
       return list.stream().map(EvaluateDetail::convert).collect(Collectors.toList());
    }
}
