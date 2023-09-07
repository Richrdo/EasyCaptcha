package com.timegoesby.captcha.service;

import org.springframework.stereotype.Service;


public interface EmailService   {

    void sendCaptcha(String to);

}
