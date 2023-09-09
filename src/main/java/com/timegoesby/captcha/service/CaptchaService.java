package com.timegoesby.captcha.service;

import com.timegoesby.captcha.common.R;
import com.timegoesby.captcha.vo.CaptchaVo;

public interface CaptchaService {

    String randomCaptcha();

    void saveCaptcha(String email,String captcha);

    R<String> verify(CaptchaVo captchaVo);

}
