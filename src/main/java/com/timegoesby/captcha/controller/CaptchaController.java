package com.timegoesby.captcha.controller;

import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.service.EmailService;
import com.timegoesby.captcha.vo.EmailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/verification")
public class CaptchaController {

    @Autowired
    EmailService emailService;

    @RequestMapping("/test")
    public R<String> test(){
        return R.success();
    }

    @RequestMapping("/send")
    public R<String> sendCaptcha(EmailVo emailVo){

        emailService.sendCaptcha(emailVo);

        return R.success();
    }

}
