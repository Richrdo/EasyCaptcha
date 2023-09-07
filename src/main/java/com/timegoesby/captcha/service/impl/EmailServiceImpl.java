package com.timegoesby.captcha.service.impl;

import com.timegoesby.captcha.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Log4j2
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    protected JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendCaptcha(String to) {


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setText("你的验证码为：123545\n");
        mailMessage.setSubject("邮箱验证");

        mailSender.send(mailMessage);
    }
}
