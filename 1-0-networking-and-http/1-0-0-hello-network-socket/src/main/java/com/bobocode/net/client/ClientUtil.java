package com.bobocode.net.client;

import com.bobocode.util.ExerciseNotCompletedException;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

/**
 * A util class that implements all the logic required for building {@link MessageBoardClient}.
 */
public class ClientUtil {
    private ClientUtil() {
    }

    /**
     * Using provided host and port it creates an instance of a new {@link Socket} using two-argument constructor.
     * This means that returned socket will be already connected to the server, or will throw an error if the connection
     * failed.
     *
     * @param host server host
     * @param port server port
     * @return an instance of a connected socket
     */
    @SneakyThrows
    public static Socket openSocket(String host, int port) {
        throw new ExerciseNotCompletedException(); // todo: implement according to javadoc and verify by ClientUtilTest
    }

    /**
     * Creates a simple {@link BufferedReader} that allows to read messages from the console.
     *
     * @return console-based {@link BufferedReader}
     */
    @SneakyThrows
    public static BufferedReader openConsoleReader() {
        InputStreamReader consoleInputStream = new InputStreamReader(System.in);
        return new BufferedReader(consoleInputStream);
    }

    /**
     * Prints a prompt and reads a line using provided reader.
     *
     * @param reader
     * @return the message read by reader
     */
    @SneakyThrows
    public static String readMessage(BufferedReader reader) {
        System.out.print("Enter message (q to quit): ");
        return reader.readLine();
    }

    /**
     * This is the most important method of this class. It allows writing a string message to the given socket.
     * In order to write to the connected socket, it uses its {@link OutputStream}. But since we need to write text
     * messages, it creates a {@link BufferedWriter} based on the {@link OutputStream}. A writer allows to
     * write {@link String} messages instead of bytes. In order to force sending data to the remote socket,
     * it flushed the buffer of the writer using a corresponding method.
     *
     * @param message a message that should be sent to the socket
     * @param socket  a socket instance connected to the server
     */
    @SneakyThrows
    public static void writeToSocket(String message, Socket socket) {
        throw new ExerciseNotCompletedException(); // todo: implement according to javadoc and verify by ClientUtilTest
    }
}
