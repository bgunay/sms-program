package com.example.smsdemo.service.impl;

import static com.example.smsdemo.constants.SmsConstants.ERR_SERVICE_IS_DOWN;
import static com.example.smsdemo.constants.SmsConstants.ERR_STH_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.example.smsdemo.extension.LogCapture;
import com.example.smsdemo.extension.LogCaptureExtension;
import com.example.smsdemo.service.SmsSendService;
import com.example.smsdemo.util.DummyBadCasesConfig;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class, LogCaptureExtension.class})
public class SmsSendServiceImplUnitTest {

    SmsSendService smsSendService;

    @Mock
    DummyBadCasesConfig dummyBadCasesConfig;

    String[] params;

    @BeforeEach
    void init() {
        params = new String[]{"smsText", "+905548473863", "+12345324567"};
    }

    @Test
    void givenValidInputs_whenSendSms_thenSucceed() throws InternalServerException, SomeSmsBadRequestException {
        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl(dummyBadCasesConfig);

        SmsResponseDto smsSent = smsSendService.sendSmsMessage(params[0], params[1], params[2]);

        assertNotNull(smsSent);
    }

    @Test
    void givenConfigBadRequest_whenSendSms_thenThrowsBadRequestException() {
        when(dummyBadCasesConfig.isBadRequest()).thenReturn(true);

        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl(dummyBadCasesConfig);

        SomeSmsBadRequestException thrown = Assertions.assertThrows(SomeSmsBadRequestException.class, () -> {
            smsSendService.sendSmsMessage(params[0], params[1], params[2]);
        }, "err.sth.not.found was expected");

        assertEquals(ERR_STH_NOT_FOUND, thrown.getMessage());

    }

    @Test
    void givenConfigServiceDown_whenSendSms_thenThrowsInternalServerException(LogCapture logCapture) throws SomeSmsBadRequestException {
        when(dummyBadCasesConfig.isServiceDown()).thenReturn(true);

        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl(dummyBadCasesConfig);

        InternalServerException thrown = Assertions.assertThrows(InternalServerException.class, () -> {
            smsSendService.sendSmsMessage(params[0], params[1], params[2]);
        }, "InternalServerException: err.service.down was expected");

        assertEquals(InternalServerException.TYPE_NAME + ": " + ERR_SERVICE_IS_DOWN, thrown.getMessage());

        List<LoggingEvent> loggingEvents = logCapture.getLoggingEvents();

        assertThat(loggingEvents.size()).isEqualTo(1);
        assertThat(loggingEvents.get(0).getLevel().levelStr).isEqualTo("ERROR");
        assertThat(loggingEvents.get(0).getFormattedMessage()).isEqualTo("Service is Down, throwing exception");
    }

    @Test
    void givenMethodNotCall_whenSendSms_thenLog(LogCapture logCapture) throws Exception {
        assertThat(logCapture.getLoggingEvents()).isEmpty();

    }

    @Test
    void givenValidInputs_whenSendSms_thenLog(LogCapture logCapture) throws Exception {
        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl(dummyBadCasesConfig);

        SmsResponseDto smsSent = smsSendService.sendSmsMessage(params[0], params[1], params[2]);

        List<LoggingEvent> loggingEvents = logCapture.getLoggingEvents();

        assertThat(loggingEvents).isNotEmpty();
        assertThat(loggingEvents).hasSize(1);

        LoggingEvent loggingEvent = loggingEvents.get(0);

        assertThat(loggingEvent.getMessage()).isEqualTo("{}");
        assertThat(loggingEvent.getFormattedMessage()).isEqualTo("smsText");
        assertThat(loggingEvent.getLevel().levelStr).isEqualTo("INFO");
        assertThat(loggingEvent.getArgumentArray()[0]).isEqualTo(params[0]);
        assertThat(loggingEvent.getLoggerName()).isEqualTo("com.example.smsdemo.service.impl.SmsSendServiceImpl");
    }

    @Test
    void givenValidInputs_whenSendSms_thenCalculateAllLogs(LogCapture logCapture) throws Exception {
        SmsSendServiceImpl smsSendService = new SmsSendServiceImpl(dummyBadCasesConfig);

        //given
        int partSize = 6;
        String partXofYText = " - Part X of Y";
        //long sms has 6 parts
        String originalMessage = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur";
        params[0] = originalMessage;
        smsSendService.sendSmsMessage(params[0], params[1], params[2]);

        List<LoggingEvent> loggingEvents = logCapture.getLoggingEvents();

        //then
        assertThat(loggingEvents).isNotEmpty();
        assertThat(loggingEvents).hasSize(partSize);

        //given
        List<String> smsTextsLogged = loggingEvents.stream().map(elm -> elm.getFormattedMessage()).collect(Collectors.toList());
        List<String> smsTextWithoutLast = smsTextsLogged.subList(0, smsTextsLogged.size() - 1);

        //then all sms's has 160 elements
        assertThat(smsTextWithoutLast.stream().map(elm -> elm.length()).distinct().findFirst()).isEqualTo(Optional.of(160));
        assertThat(smsTextWithoutLast.stream().map(elm -> elm.length()).distinct().count()).isEqualTo(1);

        //then check last element more than 1 and  less than 160 char
        assertThat(smsTextsLogged.get(smsTextsLogged.size() - 1).length()).isGreaterThan(0).isLessThan(160);

        //then sms log length  = original sms + (partXofY * smsParts)
        String totalLogJoinedWithPartText = smsTextsLogged.stream().collect(Collectors.joining());
        assertThat(totalLogJoinedWithPartText.length()).isEqualTo(originalMessage.length() + (partXofYText.length() * smsTextsLogged.size()));

        assertThat(loggingEvents.get(0).getLevel().levelStr).isEqualTo("INFO");

        //check first sms if ends with " - Part 1 of 6"
        LoggingEvent loggingEvent = loggingEvents.get(0);
        new StringAssert(loggingEvent.getArgumentArray()[0].toString()).endsWith(" - Part 1 of 6");

        assertThat(loggingEvent.getLoggerName()).isEqualTo("com.example.smsdemo.service.impl.SmsSendServiceImpl");
    }
}