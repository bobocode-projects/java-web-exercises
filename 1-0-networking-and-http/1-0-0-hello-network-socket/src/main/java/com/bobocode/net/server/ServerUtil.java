package com.bobocode.net.server;

import com.bobocode.util.ExerciseNotCompletedException;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Util class for a {@link MessageBoardServer}. It provides all necessary method for network communication. Using these
 * utils you can create server socket, accept connection from client, read String message from client socket and print
 * it using special provided format.
 */
public class ServerUtil {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private ServerUtil() {
    }

    /**
     * A simple method that returns localhost
     *
     * @return localhost address
     */
    @SneakyThrows
    public static String getLocalHost() {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * Creates an instance of a {@link ServerSocket} based on the given port.
     *
     * @param port
     * @return a new bound {@link ServerSocket} instance
     */
    @SneakyThrows
    public static ServerSocket createServerSocket(int port) {
        throw new ExerciseNotCompletedException(); // todo: implement according to javadoc and verify by ServerUtilTest
    }

    /**
     * This method accepts a client on a given serverSocket and returns a accepted socket instance.
     *
     * @param serverSocket an open server socket
     * @return an instance of a accepted client socket
     */
    @SneakyThrows
    public static Socket acceptClientSocket(ServerSocket serverSocket) {
        throw new ExerciseNotCompletedException(); // todo: implement according to javadoc and verify by ServerUtilTest
    }

    /**
     * This is the most important method of this class. It allows reading all messages sent by client form the sockets.
     * In order to read from a socket, it uses its {@link InputStream}. But since we want to read {@link String}
     * messages instead of bytes, it creates a {@link BufferedReader} based on the socket {@link InputStream}.
     * Using the reader, it reads a line and returns a {@link String} message.
     *
     * @param socket an accepted socket
     * @return message that was received from the client
     */
    @SneakyThrows
    public static String readMessageFromSocket(Socket socket) {
        throw new ExerciseNotCompletedException(); // todo: implement according to javadoc and verify by ServerUtilTest
    }

    /**
     * Simple message that allows to print message using a nice and informative format. Apart from the text message
     * itself, it also prints the time and client address.
     *
     * @param socket  accepted socket
     * @param message a text message to print
     */
    public static void printMessage(Socket socket, String message) {
        InetAddress clientAddress = socket.getInetAddress();
        System.out.print(LocalDateTime.now().format(TIME_FORMATTER) + " ");
        System.out.printf("[%s]", clientAddress.getHostAddress());
        System.out.println(" -- " + message);
    }

}
