package com.example.smsdemo.dto.request;

import static com.example.smsdemo.constants.SmsConstants.PATTERN_PHONE_NUMBER;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRequestDto implements Serializable {

    private static final long serialVersionUID = -1071660964299653120L;

    @NotEmpty(message = "{error.sms.text.empty}")
    @Size(min = 1, max = 10000, message = "{error.sms.size}")
    private String text;

    @NotEmpty(message = "{error.sms.to.empty}")
    @Pattern(message = "{error.sms.phone-number.format}", regexp = PATTERN_PHONE_NUMBER)
    private String to;

    @NotEmpty(message = "{error.sms.from.empty}")
    @Pattern(message = "{error.sms.phone-number.format}", regexp = PATTERN_PHONE_NUMBER)
    private String from;

}
