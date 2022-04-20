package com.example.smsdemo.service;

import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerException;

public interface SmsSendService {

    SmsResponseDto sendSmsMessage(final String text, final String to, final String from) throws SomeSmsBadRequestException, InternalServerException;

}
