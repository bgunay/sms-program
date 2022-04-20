package com.example.smsdemo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.smsdemo.dto.request.SmsRequestDto;
import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.example.smsdemo.service.SmsSendService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SmsControllerUnitTest {

    @Mock
    private SmsSendService smsSendService;

    @InjectMocks
    private SmsController smsController;

    @Test
    void whenSendSms_thenOK() throws InternalServerException {
        // Given
        SmsRequestDto smsRequestDto = SmsRequestDto
            .builder()
            .text("some sms content")
            .to("+90 343 534 543 5334")
            .from("+90 123 534 543 5334")
            .build();

        SmsResponseDto smsResponseDto = SmsResponseDto
            .builder()
            .status("ok")
            .description("desc")
            .sent(true)
            .build();

        // When
        when(smsSendService.sendSmsMessage(anyString(), anyString(), anyString())).thenReturn(smsResponseDto);

        ResponseEntity<SmsResponseDto> smsResponseDtoResponseEntity = smsController.sendSms(smsRequestDto);

        // Then
        ResponseEntity<SmsResponseDto> okResponse = ResponseEntity.ok(smsResponseDto);

        assertThat(smsResponseDtoResponseEntity).isEqualTo(okResponse);
        assertThat(smsResponseDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}