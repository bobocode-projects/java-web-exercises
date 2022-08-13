package com.bobocode.demo.service;

import com.bobocode.demo.annotation.Autowired;
import com.bobocode.demo.annotation.Component;

@Component
public class PrinterService {
    @Autowired
    private MessageService messageService;

    public void printMessage() {
        var message = messageService.getMessage();
        System.out.println("*" + message);
    }
}
