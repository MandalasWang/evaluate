package com.wenwo.evaluate.model.bo;

import com.wenwo.evaluate.model.property.EvaluateProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailContent {


    private String depart;

    private String evaluator;

    private String powerStandard;

    private String powerExample;

    private String powerAdvice;

    private String attitudeStandard;

    private String attitudeExample;


    private String attitudeAdvice;


    private String mailTo;



    public static EvaluateProperty convert(MailContent content){
        if(null == content){
            return null;
        }
        EvaluateProperty property = new EvaluateProperty();
        BeanUtils.copyProperties(content,property);
        return property;
    }


    public static List<EvaluateProperty> convert(List<MailContent> list){
        if(list.isEmpty() ){
            return null;
        }
        List<EvaluateProperty> collect = list.stream().map(MailContent::convert).collect(Collectors.toList());
        int i = 0;
        for (EvaluateProperty x : collect) {
            x.setNum(i);
            i++;
        }
        return collect;
    }
}
