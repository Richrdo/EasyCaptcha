package com.timegoesby.captcha.vo;

import lombok.Data;

import java.util.Date;

@Data
public class EmailVo {
    private String id;
    private String from;
    private String to;
    private String subject;
    private String content;
    private Date sendDate;
    private String status;
    private String error;


}
