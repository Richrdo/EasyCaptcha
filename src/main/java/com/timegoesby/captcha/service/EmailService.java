package com.timegoesby.captcha.service;

import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.vo.EmailVo;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface EmailService   {

    public R<String> sendCaptcha(EmailVo emailVo);

}
