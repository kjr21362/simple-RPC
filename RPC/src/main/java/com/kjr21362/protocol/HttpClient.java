package com.kjr21362.protocol;

import com.kjr21362.common.InvocationParams;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HttpClient {

    public Object send(String ip, int port, InvocationParams invocationParams) {
        // Need to create ObjectOutputStream before ObjectInputStream
        // ObjectInputStream constructor will block until the corresponding ObjectOutputStream has written and flushed the header.
        try (Socket clientSocket = new Socket(ip, port);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())) {

            objectOutputStream.writeObject(invocationParams);
            objectOutputStream.flush();

            return objectInputStream.readObject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
