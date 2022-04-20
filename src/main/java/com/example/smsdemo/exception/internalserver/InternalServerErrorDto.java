package com.example.smsdemo.exception.internalserver;

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
public class InternalServerErrorDto implements Serializable {

    private static final long serialVersionUID = 4143152490720150370L;

    private String reason;
}
