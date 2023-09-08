package com.timegoesby.captcha.service.impl;

import com.timegoesby.captcha.service.CaptchaService;
import com.timegoesby.captcha.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Autowired
    RedisUtil redisUtil;

    @Value("${captcha.livetime}")
    private int liveTime;

    @Override
    public String randomCaptcha() {
        Random random = new Random();
        int num = random.nextInt(999999);
        String captcha = String.format("%06d",num);
        return captcha;
    }

    @Override
    public void saveCaptcha(String email, String captcha) {
        redisUtil.set(email, captcha,liveTime );
    }
}
