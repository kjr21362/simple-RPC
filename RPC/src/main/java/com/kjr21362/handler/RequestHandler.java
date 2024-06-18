package com.kjr21362.handler;

import com.kjr21362.common.InvocationParams;
import com.kjr21362.register.LocalRegister;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Need to create ObjectOutputStream before ObjectInputStream
        // ObjectInputStream constructor will block until the corresponding ObjectOutputStream has written and flushed the header.
        try (ObjectOutputStream objectOutputStream =
                 new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream =
                 new ObjectInputStream(socket.getInputStream())) {

            Object result;
            InvocationParams invocationParams =
                (InvocationParams) objectInputStream.readObject();
            System.out.println("Request from client " + socket.getLocalAddress());
            System.out.println(invocationParams);

            String interfaceName = invocationParams.getInterfaceName();

            Class<?> implClass = LocalRegister.get(interfaceName);
            if (implClass == null){
                result = "Error: " + interfaceName;
                objectOutputStream.writeObject(result);
                objectOutputStream.flush();
                return;
            }

            Method method;
            try {
                method = implClass.getMethod(invocationParams.getMethodName(),
                    invocationParams.getParameterTypes());
            } catch (NoSuchMethodException | SecurityException e) {
                result = "Error: " + invocationParams.getMethodName();
                objectOutputStream.writeObject(result);
                objectOutputStream.flush();
                return;
            }

            result = method.invoke(implClass.getDeclaredConstructor().newInstance(),
                    invocationParams.getParameters());

            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

            System.out.println(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
