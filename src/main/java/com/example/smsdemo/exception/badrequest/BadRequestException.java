package com.example.smsdemo.exception.badrequest;

import com.example.smsdemo.exception.BaseException;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends BaseException {

    private static final long serialVersionUID = 8039762057718393751L;

    public static final String TYPE_NAME = "BadRequestException";

    private List<BadRequestErrorDto> errors;


    public BadRequestException(String description, List<BadRequestErrorDto> errors) {
        super(TYPE_NAME, description);
        this.errors = errors;
    }

    public BadRequestException(String description, BadRequestErrorDto error) {
        super(TYPE_NAME, description);
        this.errors = Collections.singletonList(error);
    }


    public BadRequestException() {
        super(TYPE_NAME);
    }

    @Override
    public void setName(String name) {
        super.setName(TYPE_NAME);
    }

}
