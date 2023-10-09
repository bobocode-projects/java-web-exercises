package com.bobocode.demo;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class DemoWebApp {
    @SneakyThrows
    public static void main(String[] args) {
        try (var serverSocket = new ServerSocket(8080)) {
            while (true) {
                try (var socket = serverSocket.accept()) {
                    var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    var message = reader.readLine();
                    System.out.println(message);
                }
            }
        }
    }
}
