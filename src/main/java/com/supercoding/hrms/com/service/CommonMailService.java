package com.supercoding.hrms.com.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonMailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromAddress;

    @Value("${spring.mail.frontend-base-url}")
    private String frontendBaseUrl;

    public void sendMailMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromAddress);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public String getInitialPasswordUrl(String email) {
        return frontendBaseUrl + "/set-password?email=" + email;
    }
}
