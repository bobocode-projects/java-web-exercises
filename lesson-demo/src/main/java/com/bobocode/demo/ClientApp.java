package com.bobocode.demo;

import lombok.SneakyThrows;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    private static Scanner in = new Scanner(System.in);

    @SneakyThrows
    public static void main(String[] args) {
        try (var socket = new Socket("4.tcp.eu.ngrok.io", 17441)) {
            var message = "";
            var writer = new PrintWriter(socket.getOutputStream());
            while (!message.equals("q")) {
                System.out.print(">");
                message = in.nextLine();
                writer.println(message);
                writer.flush();
            }

        }

    }
}
