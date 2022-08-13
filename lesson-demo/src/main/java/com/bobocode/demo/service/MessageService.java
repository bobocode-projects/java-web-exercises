package com.bobocode.demo.service;

import com.bobocode.demo.annotation.Component;

@Component
public class MessageService {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
