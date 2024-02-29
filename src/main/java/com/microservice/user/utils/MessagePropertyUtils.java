package com.microservice.user.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessagePropertyUtils {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, String replace) {
        return messageSource
            .getMessage(code, null, LocaleContextHolder.getLocale())
            .replace("{value}", replace)
        ;
    }

}
