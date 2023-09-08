package com.timegoesby.captcha.service.impl;

import com.timegoesby.captcha.service.CaptchaService;
import com.timegoesby.captcha.service.EmailService;
import com.timegoesby.captcha.vo.EmailVo;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.mime.MimeMultipartContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;


@Log4j2
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    CaptchaService captchaService;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.transport.protocol}")
    private String protocol;

    @Value("${spring.mail.company}")
    private String company;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.subject}")
    private String subject;

    @Override
    public void sendCaptcha(EmailVo emailVo){

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol",protocol);
        properties.setProperty("mail.smtp.host",host);
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.port",port);

        Session session = Session.getInstance(properties);
        MimeMessage mimeMessage = new MimeMessage(session);

        try{
            mimeMessage.setFrom(new InternetAddress(from,company,"UTF-8"));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(emailVo.getReceiver()));
            mimeMessage.setSubject(subject,"UTF-8");

            MimeMultipart msgMultipart = new MimeMultipart("mixed");
            mimeMessage.setContent(msgMultipart);

            MimeBodyPart content = new MimeBodyPart();
            msgMultipart.addBodyPart(content);

            MimeMultipart bodyMultipart = new MimeMultipart("related");
            content.setContent(bodyMultipart);

            MimeBodyPart htmlPart = new MimeBodyPart();
            bodyMultipart.addBodyPart(htmlPart);

            //TODO 这里修改验证码以及邮件正文。
            String captcha = captchaService.randomCaptcha();
            captchaService.saveCaptcha(emailVo.getReceiver(),captcha);
            System.out.println(emailVo.getReceiver());
            htmlPart.setContent("欢迎注册/登入,您的验证码为"+captcha,"text/html;charset=UTF-8");

            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

            Transport transport = session.getTransport();
            transport.connect(from,password);

            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
