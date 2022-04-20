package com.example.smsdemo.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsResponseDto implements Serializable {

    private static final long serialVersionUID = -3037536123009084373L;

    private boolean sent;
    private String status;
    private String description;
}
