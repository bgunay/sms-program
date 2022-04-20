package com.example.smsdemo.service.impl;

import static com.example.smsdemo.constants.SmsConstants.DESCRIPTION;
import static com.example.smsdemo.constants.SmsConstants.ERR_SERVICE_IS_DOWN;
import static com.example.smsdemo.constants.SmsConstants.ERR_STH_NOT_FOUND;
import static com.example.smsdemo.constants.SmsConstants.SMS_TEXT_LENGTH;
import static com.example.smsdemo.constants.SmsConstants.STATUS_OK;

import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.example.smsdemo.service.SmsSendService;
import com.example.smsdemo.util.DummyBadCasesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {

    private final DummyBadCasesConfig dummyBadCasesConfig;

    @Override
    public SmsResponseDto sendSmsMessage(final String text, final String to, final String from) throws SomeSmsBadRequestException, InternalServerException {
        if (dummyBadCasesConfig.isServiceDown()) {
            log.error("Service is Down, throwing exception");
            throw new InternalServerException(ERR_SERVICE_IS_DOWN);
        }

        if (dummyBadCasesConfig.isBadRequest()) {
            throw new SomeSmsBadRequestException(ERR_STH_NOT_FOUND);
        }

        log.debug("to:{}, from:{}", to, from);

        String sms;
        StringBuilder smsBuffer = new StringBuilder(text);
        int size = text.length() / SMS_TEXT_LENGTH + 1 ;
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                String partXofYText = " - Part X of Y".replace("X", String.valueOf((i + 1))).replace("Y", String.valueOf(size));
                if (i + 1 < size) {
                    smsBuffer.insert((i + 1) * SMS_TEXT_LENGTH - partXofYText.length(), partXofYText);
                    sms = smsBuffer.substring(SMS_TEXT_LENGTH * i, SMS_TEXT_LENGTH * i + SMS_TEXT_LENGTH);
                } else {
                    smsBuffer.insert(smsBuffer.length(), partXofYText);
                    sms = smsBuffer.substring(SMS_TEXT_LENGTH * i, smsBuffer.length());
                }
                deliverMessageViaCarrier(sms, to, from);
            }
        } else {
            this.deliverMessageViaCarrier(text, to, from);
        }

        return SmsResponseDto.builder().sent(true).description(DESCRIPTION).status(STATUS_OK).build();
    }

    // note: Here, I dont need "to" and "from" parameters, I've just used same syntax with given documentation
    public void deliverMessageViaCarrier(final String text, final String to, final String from) {
        log.info("{}", text);
    }
}
