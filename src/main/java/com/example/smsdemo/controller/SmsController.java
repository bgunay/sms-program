package com.example.smsdemo.controller;

import com.example.smsdemo.config.SwaggerConfiguration;
import com.example.smsdemo.dto.request.SmsRequestDto;
import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.exception.badrequest.BadRequestException;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.example.smsdemo.service.SmsSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = SmsController.ENDPOINT, tags = {SwaggerConfiguration.SMS_TAG})
@RequestMapping(value = SmsController.ENDPOINT)
public class SmsController {

    static final String ENDPOINT = "api/sms";

    private final SmsSendService smsSendService;

    public SmsController(SmsSendService smsSendService) {
        this.smsSendService = smsSendService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
        value = "Send Sms",
        nickname = "Send Sms",
        notes = "Sends Sms with given params.<br>"
            + "Valid format for to, from: +xx xxx xxx xxxx (eg +49 134 125 3456,  +1 212 555-3458)"
    )
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "ok", response = SmsResponseDto.class),
        @ApiResponse(code = 400, message = "Bad request", response = BadRequestException.class),
        @ApiResponse(code = 500, message = "Internal server error", response = InternalServerException.class)
    })
    public ResponseEntity<SmsResponseDto> sendSms(@Validated @ModelAttribute SmsRequestDto smsRequestDto) throws InternalServerException, SomeSmsBadRequestException {
        SmsResponseDto smsResponseDto = smsSendService.sendSmsMessage(smsRequestDto.getText(), smsRequestDto.getTo(), smsRequestDto.getFrom());
        return ResponseEntity.ok(smsResponseDto);
    }
}