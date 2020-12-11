package ru.kpfu.itis;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements ConnectionListener {

    private int turn;
    private boolean yourTurn = true;
    private static TCPConnection player1;
    private static TCPConnection player2;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(Protocol.PORT)) {
            try {
                player1 = new TCPConnection(this, serverSocket.accept());
                player2 = new TCPConnection(this, serverSocket.accept());
            } catch (IOException e) {
                System.out.println("TCPConnection exception: " + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
    }

    @Override
    public void onReceiveObject(TCPConnection tcpConnection, Object object) {
        Integer a = (Integer) object;
        if (yourTurn) {
            turn = 10 + a;
        } else {
            turn = 20 + a;
        }
        player1.sendObject(turn);
        player2.sendObject(turn);
        yourTurn = !yourTurn;
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        throw new UnsupportedOperationException();
    }
}
