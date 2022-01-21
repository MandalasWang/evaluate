package com.wenwo.evaluate.model.bo;

import lombok.Data;
import java.util.List;

@Data
public class MailBean {

    /**
     * 邮件接收人
     */
    private String recipient;

    /**
     * 邮件内容
     */
    private String content;


    /**
     * 邮件内容集合
     */
    private List<MailContent> contentList;

}
