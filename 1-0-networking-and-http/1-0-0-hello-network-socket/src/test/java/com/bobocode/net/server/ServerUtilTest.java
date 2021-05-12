package com.bobocode.net.server;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

import static com.bobocode.net.server.ServerUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerUtilTest {
    @BeforeEach
    public void init() {
    }

    @Order(1)
    @ParameterizedTest
    @ValueSource(ints = {8899, 9988})
    @DisplayName("createServerSocket creates an instance of ServerSocket based on a given port")
    @SneakyThrows
    void createServerSocketCreateSocketBasedOnPort(int port) {
        try (var serverSocket = createServerSocket(port)) {

            assertThat(serverSocket.getClass()).isEqualTo(ServerSocket.class);
            assertThat(serverSocket.getLocalPort()).isEqualTo(port);
        }
    }

    @Order(2)
    @Test
    @DisplayName("createServerSocket returns socket that is bound")
    @SneakyThrows
    void createServerSocketReturnsBoundSocket() {
        try (var serverSocket = createServerSocket(8899)) {

            assertThat(serverSocket.isBound()).isTrue();
        }
    }

    @Order(3)
    @Test
    @DisplayName("createServerSocket returns socket that is not closed")
    @SneakyThrows
    void createServerSocketReturnsNotClosedSocket() {
        try (var serverSocket = createServerSocket(8899)) {

            assertFalse(serverSocket.isClosed());
        }
    }

    @Order(4)
    @Test
    @SneakyThrows
    @DisplayName("acceptClientSocket accepts request and returns an instance of client socket")
    void acceptClientSocketAcceptsRequest() {
        var serverSocketMock = mock(ServerSocket.class);
        var clientSocketMock = mock(Socket.class);
        when(serverSocketMock.accept()).thenReturn(clientSocketMock);

        var acceptedSocket = acceptClientSocket(serverSocketMock);

        assertThat(acceptedSocket).isEqualTo(clientSocketMock);
    }

    @Order(5)
    @Test
    @SneakyThrows
    @DisplayName("acceptClientSocket does not modify client socket")
    void acceptClientSocketDoesNotModifySocket() {
        var serverSocketMock = mock(ServerSocket.class);
        var clientSocketMock = mock(Socket.class);
        when(serverSocketMock.accept()).thenReturn(clientSocketMock);

        acceptClientSocket(serverSocketMock);

        verify(serverSocketMock).accept();
        verifyNoInteractions(clientSocketMock);
    }

    @Order(6)
    @Test
    @SneakyThrows
    @DisplayName("acceptClientSocket does not modify server socket")
    void acceptClientSocketDoesNotModifyServerSocket() {
        var serverSocketMock = mock(ServerSocket.class);
        var clientSocketMock = mock(Socket.class);
        when(serverSocketMock.accept()).thenReturn(clientSocketMock);

        acceptClientSocket(serverSocketMock);

        verify(serverSocketMock).accept();
        verifyNoMoreInteractions(serverSocketMock);
    }

    @Order(7)
    @ParameterizedTest
    @ValueSource(strings = {"hey", "hola", "how are you doing?"})
    @DisplayName("readMessageFromSocket reads line from InputStream using BufferedReader")
    @SneakyThrows
    void readMessageFromSocketViaInputStream(String givenMessage) {
        try (var serverSocket = new ServerSocket(8899)) {
            CompletableFuture.runAsync(() -> createClientWritingMessage(givenMessage));

            var receivedMessage =  readMessageFromSocket(serverSocket.accept());

            assertThat(receivedMessage).isEqualTo(givenMessage);
        }
    }

    @SneakyThrows
    private void createClientWritingMessage(String message) {
        try (var socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 8899)) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message);
            writer.flush();
        }
    }
}