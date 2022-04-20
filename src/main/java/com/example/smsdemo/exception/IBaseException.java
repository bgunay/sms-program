package com.example.smsdemo.exception;

import com.example.smsdemo.exception.badrequest.BadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties({"cause", "localizedMessage", "stackTrace", "suppressed", "message"})

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BadRequestException.class, name = BadRequestException.TYPE_NAME),
    @JsonSubTypes.Type(value = InternalServerException.class, name = InternalServerException.TYPE_NAME)
})
public interface IBaseException {

}
