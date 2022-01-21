package com.wenwo.evaluate.service;

import com.wenwo.evaluate.config.MailConfig;
import com.wenwo.evaluate.model.bo.MailBean;
import com.wenwo.evaluate.model.bo.MailContent;
import com.wenwo.evaluate.model.property.EvaluateProperty;
import com.wenwo.evaluate.util.EasyExcelWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailService {


    @Autowired
    private MailConfig mailMsg;

    @Autowired
    private JavaMailSender mailSender;




    private void reSendMail(SimpleMailMessage message, MimeMessage mailMessage, MailSendException se) {
        Exception[] messageExceptions = se.getMessageExceptions();
        SendFailedException sendFail = new SendFailedException();
        try {
            sendFail = (SendFailedException) messageExceptions[0];
        } catch (ClassCastException e) {
            log.error("class cast exception:[{}]", e.getMessage());
        }
        log.error("send mail eerror,the invalid mail address:{}", (Object) sendFail.getInvalidAddresses());
        Address[] address = sendFail.getValidUnsentAddresses();
        if (null == address) {
            log.error("address is null ");
        }
        assert address != null;
        String[] validMails = new String[address.length];
        for (int i = 0; i < address.length; i++) {
            validMails[i] = String.valueOf(address[i]);
        }
        if (message != null) {
            message.setTo(validMails);
            mailSender.send(message);
        }
        if (mailMessage != null) {
            try {
                for (int i = 0; i < validMails.length - 1; i++) {
                    mailMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(validMails[i])));
                }
            } catch (AddressException e) {
                log.error("邮件地址获取失败", e);
            } catch (MessagingException e) {
                log.error("邮件重发失败", e);
            }
            mailSender.send(mailMessage);
        }
    }


    /**
     * 邮件发送Excel
     *
     * @param mailBean
     * @return
     */
    public String sendExcelMail(MailBean mailBean) {
        try  {
            Map<String, List<MailContent>> mailMap = mailBean.getContentList().stream().collect(Collectors.groupingBy(MailContent::getMailTo));
            Set<String> mailTo = mailBean.getContentList().stream().collect(Collectors.groupingBy(MailContent::getMailTo)).keySet();
            MimeMessage mimeMessage  = mailSender.createMimeMessage();
            mailTo.forEach(x -> {
                ByteArrayInputStream swapStream = null;
                try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()){
                    EasyExcelWriteUtil.writeExcel(arrayOutputStream, MailContent.convert(mailMap.get(x)), "环评Excel", "1", EvaluateProperty.class);
                    swapStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
                    mimeMessage.setFrom(new InternetAddress(mailMsg.getFrom()));
                    mimeMessage.addRecipients(Message.RecipientType.TO, x);
                    mimeMessage.setSubject(mailMsg.getSubject());
                    mimeMessage.setText("以下是公司成员对您的评价，请及时关注");
                    //创建合同附件
                    MimeBodyPart contentPart = (MimeBodyPart) createContent("以下是公司成员对您的评价，请及时关注！",swapStream,"部门评价附件.xlsx");
                    MimeMultipart mime = new MimeMultipart("mixed");
                    mime.addBodyPart(contentPart);
                    mimeMessage.setContent(mime);
                    mailSender.send(mimeMessage);

                } catch (MailSendException se) {
                    reSendMail(null, mimeMessage, se);
                } catch (Exception e) {
                    log.error("create MimeMessage exception.", e);
                }finally {
                    try {
                        swapStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Excel转换失败 ");
        }
        return "发送成功！";
    }



    /**
     * 合同附件的内容
     * @param content 内容文本
     * @param byteArrayInputStream 附件流
     * @param affixName 附件标题
     * @return
     */
    private Part createContent(String content, ByteArrayInputStream byteArrayInputStream, String affixName){

        MimeBodyPart contentPart = null;
        try{
            contentPart = new MimeBodyPart();
            MimeMultipart multipart = new MimeMultipart("related");
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(content,"text/html;charset=gbk");
            multipart.addBodyPart(bodyPart);

            //附件部分
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            DataSource dataSource = new ByteArrayDataSource(byteArrayInputStream,"application/excel");
            DataHandler dataHandler = new DataHandler(dataSource);
            mimeBodyPart.setDataHandler(dataHandler);
            mimeBodyPart.setFileName(MimeUtility.encodeText(affixName));
            multipart.addBodyPart(mimeBodyPart);
            contentPart.setContent(multipart);

        }catch (Exception e){
            log.error("邮件发送失败！",e);
        }
        return contentPart;
    }

}
