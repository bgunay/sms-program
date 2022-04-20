package com.example.smsdemo.handler;

import static com.example.smsdemo.constants.SmsConstants.ERR_STH_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import com.example.smsdemo.exception.badrequest.BadRequestException;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith({MockitoExtension.class})
public class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @Test
    public void shouldProcessPSmsBadRequestException() {
        SomeSmsBadRequestException invalidDateException = new SomeSmsBadRequestException(ERR_STH_NOT_FOUND);

        when(messageSource.getMessage(eq(ERR_STH_NOT_FOUND), eq(null), any())).thenReturn("error-custom");
        BadRequestException validationException = controllerExceptionHandler.processSmsException(invalidDateException);

        assertThat(validationException.getDescription()).isEqualTo("Request validator error");
        assertThat(validationException.getErrors().get(0).getSource()).isEqualTo("Sms");
        assertThat(validationException.getErrors().get(0).getReason()).isEqualTo("error-custom");
    }

}