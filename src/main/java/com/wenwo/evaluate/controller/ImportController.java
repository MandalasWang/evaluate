package com.wenwo.evaluate.controller;

import com.wenwo.evaluate.enums.FunctionEnum;
import com.wenwo.evaluate.model.bo.EvaluateRuleRequest;
import com.wenwo.evaluate.service.ImportService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class ImportController {

    @Resource
    private ImportService importService;


    @ApiOperation(value = "导入环评")
    @PostMapping(value = "importExcel")
    public String importExcel(@RequestParam(value = "file") MultipartFile file){
        try {
            importService.importExcel(file);
        }catch (Exception e){
            return "导入失败,"+e.getMessage();
        }
        return "import done";
    }


    @ApiOperation(value = "环评配置规则")
    @PostMapping(value = "configRuleSet")
    public String configRule(@RequestBody EvaluateRuleRequest request){
        try {
            importService.configRule(request);
        }catch (Exception e){
            return "规则配置失败";
        }
        return "done";
    }

    @ApiOperation(value = "邮件发送")
    @PostMapping(value = "sendMail")
    public String sendMail(@RequestParam(value = "function",defaultValue = "evaluate") FunctionEnum functionEnum){
        try {
            importService.sendMail(functionEnum);
        }catch (Exception e){
            return "规则配置失败";
        }
        return "done";
    }

    @ApiOperation(value = "清空环评")
    @PostMapping(value = "clearEvaluate")
    public String clear(@RequestParam(value = "function",defaultValue = "evaluate") FunctionEnum functionEnum){


        switch (functionEnum){
            case evaluate:
                importService.clear();
                break;
            default:
        }
        return "done";
    }


}
