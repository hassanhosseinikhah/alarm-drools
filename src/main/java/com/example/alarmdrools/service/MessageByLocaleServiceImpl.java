package com.example.alarmdrools.service;

import java.util.Locale;

import com.example.alarmdrools.inteface.MessageByLocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageByLocaleServiceImpl implements MessageByLocaleService {
    private final MessageSource messageSource;

    @Autowired
    public MessageByLocaleServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String id) {
        return this.messageSource.getMessage(id, (Object[])null, Locale.US);
    }
}
