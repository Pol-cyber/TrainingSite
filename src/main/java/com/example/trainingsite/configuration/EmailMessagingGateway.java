package com.example.trainingsite.configuration;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mail.MailHeaders;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "addToNewsletterChannel")
public interface EmailMessagingGateway {

    void addToNewsletter(SimpleMailMessage message);

}
