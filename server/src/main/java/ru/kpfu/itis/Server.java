package ru.kpfu.itis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket serverSocket;
    private static ClientHandler clientHandler1;
    private static ClientHandler clientHandler2;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(Protocol.PORT);
        Socket client1 = serverSocket.accept();
        InputStream in1 = client1.getInputStream();
        OutputStream out1 = client1.getOutputStream();
        Socket client2 = serverSocket.accept();
        InputStream in2 = client2.getInputStream();
        OutputStream out2 = client2.getOutputStream();
        clientHandler1 = new ClientHandler(in1, out2);
        clientHandler2 = new ClientHandler(in2, out1);
    }
}
