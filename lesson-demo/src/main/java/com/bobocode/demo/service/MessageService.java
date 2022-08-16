package com.bobocode.demo.service;

import com.bobocode.demo.annotation.Component;
import com.bobocode.demo.annotation.Trimmed;

@Component
public class MessageService {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(@Trimmed String message) {
        this.message = message;
    }
}
