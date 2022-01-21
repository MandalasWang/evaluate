package com.wenwo.evaluate.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wenwo.cloud.sp.domain.user.interfaces.feign.base.AuthUserFeign;
import com.wenwo.cloud.sp.domain.user.interfaces.frm.auth.FindAbdUserFrm;
import com.wenwo.cloud.sp.domain.user.interfaces.vo.AbdUserVo;
import com.wenwo.common.domain.vo.ResultVO;
import com.wenwo.evaluate.dao.ConfigRuleMapper;
import com.wenwo.evaluate.dao.ImportMapper;
import com.wenwo.evaluate.enums.*;
import com.wenwo.evaluate.model.bo.EvaluateRuleRequest;
import com.wenwo.evaluate.model.bo.MailBean;
import com.wenwo.evaluate.model.bo.MailContent;
import com.wenwo.evaluate.model.entity.EvaluateConfig;
import com.wenwo.evaluate.model.entity.EvaluateDetail;
import com.wenwo.evaluate.model.property.EvaluateProperty;
import com.wenwo.evaluate.util.EasyExcelReadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImportService {


    @Resource
    private ImportMapper importMapper;

    @Resource
    private ConfigRuleMapper mapper;


    @Autowired
    private MailService mailService;

    @Autowired
    private AuthUserFeign userInfoFeign;


    public void importExcel(MultipartFile file) {
        if(file == null){
            return;
        }
        EvaluateConfig config = mapper.selectOne(new QueryWrapper<EvaluateConfig>().eq("function", "evaluate"));
        if(ActiveImportEnum.close.name().equalsIgnoreCase(config.getActiveImport())){
            throw new NullPointerException("目前该功能还未开启，请先开启导入功能");
        }

        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        try {
            String evaluator = originalFilename.substring(originalFilename.substring(0, originalFilename.indexOf("-")).length() + 1, originalFilename.indexOf("."));
            Set<String> evaluators = importMapper.selectList(new QueryWrapper<EvaluateDetail>().eq("deleted",0)).stream().map(EvaluateDetail::getEvaluator).collect(Collectors.toSet());
            if(!evaluators.stream().filter(evaluator::equalsIgnoreCase).collect(Collectors.toList()).isEmpty()){
                throw new RuntimeException("当前评价人的环评已经导入，请勿重复操作");
            }
            List<Map<String, Object>> maps = EasyExcelReadUtil.noModelRead(file.getInputStream());
            List<EvaluateDetail> evaluateDetails = maps.stream().map(x -> EvaluateDetail.builder().evaluator(evaluator).depart(x.get(1).toString()).name(x.get(2).toString())
                    .powerExample(x.get(4).toString()).powerAdvice(x.get(5).toString()).attitudeExample(x.get(7).toString()).attitudeAdvice(x.get(8).toString()).createTime(LocalDate.now().toString()).build()).collect(Collectors.toList());
            evaluateDetails.forEach(x-> {
                if(ActiveEnum.now.name().equalsIgnoreCase(config.getActive())){
                    x.setActiveTime(LocalDate.now().toString());
                }else{
                    x.setActiveTime((config.getActiveTime() == null || config.getActiveTime().length() < 1) ? LocalDate.now().toString() : config.getActiveTime());
                }
                importMapper.insert(x);
            });
        } catch (IOException e) {
            log.error("导入数据失败,请保证Excel命名正确并没有合并单元格",e);
        }
    }


    /**
     * 配置环评规则
     * @param request
     */
    public void configRule(EvaluateRuleRequest request) {
        //先删除所有功能相关配置
        QueryWrapper<EvaluateConfig> queryWrapper = new QueryWrapper();
        queryWrapper.eq("function",request.getFunction());
        mapper.delete(queryWrapper);

        EvaluateConfig config = EvaluateConfig.builder().activeImport(request.getActiveImport())
                .active(request.getActive())
                .activeTime(request.getActiveTime())
                .createTime(new Date())
                .showDepart(request.getShowDepart())
                .showEvaluator(request.getShowEvaluator())
                .function(request.getFunction())
                .creator("superAdmin")
                .build();
        mapper.insert(config);
    }




    /**
     * 邮件发送
     */
    public void sendMail( FunctionEnum functionEnum) {

         switch (functionEnum){
             case evaluate:
                 //获取环评规则
                 QueryWrapper<EvaluateConfig> queryWrapper = new QueryWrapper<>();
                 EvaluateConfig config = mapper.selectOne(queryWrapper.eq("function", "evaluate").eq("deleted",0));
                 QueryWrapper<EvaluateDetail> detailQueryWrapper = new QueryWrapper<>();
                 List<EvaluateDetail> evaluateDetails = importMapper.selectList(detailQueryWrapper.eq("deleted",0));
                 MailBean mailBean = new MailBean();
                 List<MailContent> collect = evaluateDetails.stream().map(x -> MailContent.builder().evaluator(x.getEvaluator()).attitudeAdvice(x.getAttitudeAdvice())
                         .attitudeExample(x.getAttitudeExample()).powerExample(x.getPowerExample()).powerAdvice(x.getPowerAdvice())
                         .depart(x.getDepart()).mailTo(x.getName()).build()).collect(Collectors.toList());
                 Set<String> mailTo = collect.stream().map(MailContent::getMailTo).collect(Collectors.toSet());
                 Map<String,String> userMail = new HashMap<>(16);
                 mailTo.forEach(x->{
                     ResultVO<List<AbdUserVo>> adminAbdUsers = userInfoFeign.findAdminAbdUsers(FindAbdUserFrm.builder().realName(x).build());
                     AbdUserVo abdUserVo = adminAbdUsers.getData().get(0);
                     userMail.put(x,abdUserVo.getEmail());
                 });
                 collect.forEach(x->{
                     x.setMailTo(userMail.get(x.getMailTo()));
                 });
                 if(DepartEnum.no_show.name().equalsIgnoreCase(config.getShowDepart())){
                     collect.forEach(x->
                             x.setDepart("xxx部门"));
                 }
                 if(EvaluatorEnum.no_show.name().equalsIgnoreCase(config.getShowEvaluator())){
                     collect.forEach(x->
                             x.setEvaluator("评价人xxx"));
                 }
                 mailBean.setContentList(collect);
                 if(ActiveEnum.now.name().equalsIgnoreCase(config.getActive())){
                     mailService.sendExcelMail(mailBean);
                 }
                 break;
             default:
         }
    }


    public void clear() {
        List<EvaluateDetail> evaluateDetails = importMapper.selectList(new QueryWrapper<>());
        evaluateDetails.forEach(x->{
            x.setDeleted("1");
            importMapper.updateById(x);
        });
    }
}
