package com.example.smsdemo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.example.smsdemo.service.SmsSendService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class SmsApplicationTests {

    @SpyBean
    SmsSendService smsSendService;

    @Test
    void contextLoads() throws InternalServerException {
        assertNotNull(smsSendService);
        verify(smsSendService, times(0)).sendSmsMessage(anyString(), anyString(), anyString());
    }

}
