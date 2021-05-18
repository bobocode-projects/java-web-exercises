package com.bobocode.net.client;

import com.bobocode.net.server.MessageBoardServer;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.net.Socket;

import static com.bobocode.net.client.ClientUtil.*;

/**
 * {@link MessageBoardClient} is a client app that allows connecting to the {@link MessageBoardServer} in order to send
 * a message. This app reads a message from the console, connects to the server socket and sends a message writing
 * data to the output stream. The message then will be printed on the {@link MessageBoardServer} side. The app will
 * ask user to type message infinitely, until the user enters `q`.
 * <p>
 * PLEASE MAKE SURE THAT {@link MessageBoardServer} is running before using the client app.
 * <p>
 * If you have another machine, you can try to run {@link MessageBoardServer} on that machine. In order to do that, you
 * need to make sure that you can find that machine by IP address, and the port you want to use is exposed properly.
 * If so, you will need to specify corresponding HOST and POST values below.
 */
public class MessageBoardClient {
    private static final String SERVER_ADDRESS = MessageBoardServer.HOST;
    private static final int SERVER_PORT = MessageBoardServer.PORT;

    @SneakyThrows
    public static void main(String[] args) {
        try (BufferedReader reader = openConsoleReader()) {
            String message = readMessage(reader);

            while (!message.equals("q")) {
                try (Socket socket = openSocket(SERVER_ADDRESS, SERVER_PORT)) {
                    writeToSocket(message, socket);
                }
                message = readMessage(reader);
            }
        }
    }
}
