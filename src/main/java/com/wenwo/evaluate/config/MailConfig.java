package com.wenwo.evaluate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class MailConfig {

    @Value("${evaluate.email.subject}")
    private String subject;


    @Value("${spring.mail.username}")
    private String from;


}
