package com.bobocode.demo;

import com.bobocode.demo.context.ApplicationContextImpl;
import com.bobocode.demo.service.MessageService;
import com.bobocode.demo.service.PrinterService;
import lombok.SneakyThrows;

public class DemoWebApp {
    @SneakyThrows
    public static void main(String[] args) {
        var context = new ApplicationContextImpl("com.bobocode.demo");
        MessageService messageService = context.getBean(MessageService.class);
        messageService.setMessage("Hello everyone! It's been a good time");
        PrinterService printerService = context.getBean(PrinterService.class);
        printerService.printMessage();
    }
}
