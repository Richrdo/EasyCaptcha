package com.timegoesby.captcha.controller;

import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.service.CaptchaService;
import com.timegoesby.captcha.service.EmailService;
import com.timegoesby.captcha.vo.CaptchaVo;
import com.timegoesby.captcha.vo.EmailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/verification")
public class CaptchaController {

    @Autowired
    EmailService emailService;

    @Autowired
    CaptchaService captchaService;

    @RequestMapping("/test")
    public R<String> test(){
        return R.success();
    }

    @RequestMapping("/send")
    public R<String> sendCaptcha(@RequestBody EmailVo emailVo){

        emailService.sendCaptcha(emailVo);
        System.out.println(emailVo.toString());
        return R.success();
    }

    @RequestMapping("/verify")
    public R verify(@RequestBody CaptchaVo captchaVo){
        return captchaService.verify(captchaVo);
    }

}
