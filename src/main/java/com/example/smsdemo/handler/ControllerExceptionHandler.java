package com.example.smsdemo.handler;

import com.example.smsdemo.exception.badrequest.BadRequestErrorDto;
import com.example.smsdemo.exception.badrequest.BadRequestException;
import com.example.smsdemo.exception.badrequest.MessageType;
import com.example.smsdemo.exception.custom.SomeSmsBadRequestException;
import com.example.smsdemo.exception.internalserver.InternalServerErrorDto;
import com.example.smsdemo.exception.internalserver.InternalServerException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private static final String ENTITY_NAME = "Sms";

    @Autowired
    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public InternalServerException processException(Exception ex) {
        log.error("error ", ex);
        String message = getMessage("error.general");
        InternalServerErrorDto errorDto = new InternalServerErrorDto(message);
        return new InternalServerException("Internal server error", Collections.singletonList(errorDto));
    }

    @ExceptionHandler(SomeSmsBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BadRequestException processSmsException(SomeSmsBadRequestException ex) {
        log.error("error ", ex);
        String message = getMessage(ex.getMessage());
        BadRequestErrorDto errorDto = new BadRequestErrorDto(MessageType.VALIDATION, ENTITY_NAME, message);
        return new BadRequestException("Request validator error", errorDto);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
        BindException bindException,
        HttpHeaders httpHeaders,
        HttpStatus httpStatus,
        WebRequest webRequest
    ) {
        List<ObjectError> errors = bindException.getAllErrors();

        List<BadRequestErrorDto> errorDtoList = errors
            .stream()
            .map(this::getDtoFromError)
            .sorted(Comparator.comparing(BadRequestErrorDto::getReason))
            .collect(Collectors.toList());

        return new ResponseEntity<>(errorDtoList, httpHeaders, httpStatus);
    }

    private BadRequestErrorDto getDtoFromError(ObjectError error) {
        String msg = error.getDefaultMessage();

        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            if (field.isEmpty()) {
                return new BadRequestErrorDto(MessageType.VALIDATION, ENTITY_NAME, msg);
            } else {
                return new BadRequestErrorDto(MessageType.VALIDATION, ENTITY_NAME + "." + field, msg);
            }
        } else {
            return new BadRequestErrorDto(MessageType.VALIDATION, ENTITY_NAME, msg);
        }
    }

    private String getMessage(String message) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(message, null, currentLocale);
        } catch (NoSuchMessageException e) {
            return message;
        }
    }

}