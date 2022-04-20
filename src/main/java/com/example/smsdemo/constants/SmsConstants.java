package com.example.smsdemo.constants;

public final class SmsConstants {

    private SmsConstants() {
    }

    public static final String ERR_SERVICE_IS_DOWN = "err.service.down";
    public static final String ERR_STH_NOT_FOUND = "err.sth.not.found";
    public static final String DESCRIPTION = "sms sent to receipt";
    public static final String STATUS_OK = "ok";
    public static final String PATTERN_PHONE_NUMBER = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
    public static final int SMS_TEXT_LENGTH = 160;

}
