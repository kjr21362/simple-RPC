package com.kjr21362.protocol;

import com.kjr21362.handler.RequestHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private static final int N_THREADS = 10;

    private final ServerSocket serverSocket;
    private final ExecutorService executorService;

    public HttpServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(N_THREADS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true) {
            try {
                executorService.execute(new RequestHandler(serverSocket.accept()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
