package com.example.trainingsite.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;

@Configuration
public class ConfForIntegrationMail {


    @Bean
    public IntegrationFlow emailSendingFlow(JavaMailSender mailSender) {
        return IntegrationFlow
                .from("addToNewsletterChannel").channel("sendNewsletterChannelToEmail")
                .handle(Mail.outboundAdapter(mailSender))
                .get();
    }
}
