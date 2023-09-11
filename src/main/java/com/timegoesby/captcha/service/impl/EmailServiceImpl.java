package com.timegoesby.captcha.service.impl;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.service.CaptchaService;
import com.timegoesby.captcha.service.EmailService;
import com.timegoesby.captcha.utils.EmailThreadPool;
import com.timegoesby.captcha.vo.EmailVo;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.bouncycastle.mime.MimeMultipartContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
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
import java.util.concurrent.*;


@Log4j2
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender mailSender;

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


    @Autowired
    CaptchaService captchaService;

    @Override
    public R<String> sendCaptcha(EmailVo emailVo){
        System.out.println("in func sendCaptcha "+ emailVo.getReceiver());
        Future<EmailStatue> emailStatueFuture =  EmailThreadPool.getInstance().submit(new MessageTask(emailVo));
        EmailStatue statue = null;
        try{
            statue = emailStatueFuture.get();
        }catch (InterruptedException | ExecutionException e){
            log.warn(e.getMessage());
        }
        if (null==statue) return R.fail("未知错误");
        if (statue.getCode()!=200){
            return R.fail(statue.getMessage());
        }
        return R.success(statue.getMessage());
    }

    @AllArgsConstructor
    @Data
    private class EmailStatue{
        int code;
        String message;
    }


    private class MessageTask implements Callable<EmailStatue> {

        private EmailVo emailVo=null;

        public MessageTask(EmailVo emailVo){
            this.emailVo = emailVo;
        }

        @Override
        public EmailStatue call() throws Exception {

            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol",protocol);
            properties.setProperty("mail.smtp.host",host);
            properties.setProperty("mail.smtp.auth","true");
            properties.setProperty("mail.smtp.port",port);

            Session session = Session.getInstance(properties);
            MimeMessage mimeMessage = new MimeMessage(session);

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

            String captcha = captchaService.randomCaptcha();
            captchaService.saveCaptcha(emailVo.getReceiver(),captcha);
            System.out.println(emailVo.getReceiver());
            htmlPart.setContent("欢迎注册/登入,您的验证码为"+captcha,"text/html;charset=UTF-8");

            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();
            EmailStatue emailStatue = new EmailStatue(200,"OK");
            try{
                Transport transport = session.getTransport();
                transport.connect(from,password);

                transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
            }catch (SMTPAddressFailedException e){
                System.out.println(e.getReturnCode()+" in "+e.getAddress()+":"+e.getMessage());
                log.warn(e.getReturnCode()+" in "+e.getAddress()+":"+e.getMessage());
                emailStatue = new EmailStatue(e.getReturnCode(),e.getMessage());
            }

            return emailStatue;
        }
    }

}
