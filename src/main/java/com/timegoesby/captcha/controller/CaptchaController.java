package com.timegoesby.captcha.controller;

import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verification")
public class CaptchaController {

    @Autowired
    EmailService emailService;

    @RequestMapping("/test")
    public R<String> test(){
        return R.success();
    }

    @RequestMapping("/send/{email}")
    public R<String> sendCaptcha(@PathVariable(value = "email")String email){

        emailService.sendCaptcha(email);

        return R.success();
    }

}
