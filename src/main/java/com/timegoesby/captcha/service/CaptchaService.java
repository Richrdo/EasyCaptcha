package com.timegoesby.captcha.service;

public interface CaptchaService {

    String randomCaptcha();

    void saveCaptcha(String email,String captcha);

}
