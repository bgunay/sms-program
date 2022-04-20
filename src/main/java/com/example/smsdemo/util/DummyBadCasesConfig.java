package com.example.smsdemo.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * This class is used for demostration purpose only,
 * You can change the values in badcases.properties
 * service/down: boolean / it demonstrates 3rd party application service down state for sending sms
 * badrequest: boolean / it demonstrates if there are any other validations ( maybe from db or other service)
 * or any unchecked validations for SmsRequestDto
 */
@Configuration
@PropertySource("classpath:badcases.properties")
@ConfigurationProperties(prefix = "case")
@Getter
@Setter
public class DummyBadCasesConfig {

    private boolean serviceDown;
    private boolean badRequest;
}
