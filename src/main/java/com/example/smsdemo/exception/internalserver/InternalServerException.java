package com.example.smsdemo.exception.internalserver;

import com.example.smsdemo.exception.BaseException;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalServerException extends BaseException {

    private static final long serialVersionUID = -9068937177739038116L;

    public static final String TYPE_NAME = "InternalServerException";

    private List<InternalServerErrorDto> errors;

    public InternalServerException(String description, List<InternalServerErrorDto> errors) {
        super(TYPE_NAME, description);
        this.errors = errors;
    }

    public InternalServerException(String description, InternalServerErrorDto error) {
        super(TYPE_NAME, description);
        this.errors = Collections.singletonList(error);
    }

    public InternalServerException(String description) {
        super(TYPE_NAME, description);
        this.errors = Collections.emptyList();
    }

    public InternalServerException() {
        super(TYPE_NAME);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public void setName(String name) {
        super.setName(TYPE_NAME);
    }

}
