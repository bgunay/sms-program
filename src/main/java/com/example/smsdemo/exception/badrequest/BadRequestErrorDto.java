package com.example.smsdemo.exception.badrequest;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BadRequestErrorDto implements Serializable {

    private static final long serialVersionUID = 7094155124684806606L;

    private MessageType type;

    private String source;

    private String reason;
}
