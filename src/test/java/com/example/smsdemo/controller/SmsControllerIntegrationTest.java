package com.example.smsdemo.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.example.smsdemo.dto.response.SmsResponseDto;
import com.example.smsdemo.extension.LogCapture;
import com.example.smsdemo.extension.LogCaptureExtension;
import com.example.smsdemo.service.impl.SmsSendServiceImpl;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = SmsController.class)
@ExtendWith({LogCaptureExtension.class})
class SmsControllerIntegrationTest {

    @MockBean
    private SmsSendServiceImpl smsSendService;

    @Autowired
    private MockMvc mockMvc;

    String[] params;

    @BeforeEach
    void init() {
        params = new String[]{"smsText", "+905548473863", "+12345324567"};
    }

    @Test
    @DisplayName("Should Send Sms When making GET request to endpoint - /api/sms/")
    public void shouldCreateGet(LogCapture logCapture) throws Exception {
        SmsResponseDto smsResponseDto = SmsResponseDto
            .builder()
            .status("ok")
            .description("desc")
            .sent(true)
            .build();

        // When
        when(smsSendService.sendSmsMessage(anyString(), anyString(), anyString())).thenReturn(smsResponseDto);

        //then
        mockMvc.perform(
                get("/api/sms?to=+1 212 555-3458&from=+1 212 345-3458&text=Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam"))
            .andExpect(status().is(200))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.status", Matchers.is("ok")))
            .andExpect(jsonPath("$.description", Matchers.is("desc")))
            .andExpect(jsonPath("$.sent", Matchers.is(true)));

        //then

        List<LoggingEvent> loggingEvents = logCapture.getLoggingEvents();

    }

}