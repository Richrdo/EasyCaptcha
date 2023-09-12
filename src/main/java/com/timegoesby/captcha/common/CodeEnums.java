package com.timegoesby.captcha.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeEnums {

    SUCCESS(200,"success"),
    FAIL(500,"failed");

    private final int code;
    private final String message;

}
