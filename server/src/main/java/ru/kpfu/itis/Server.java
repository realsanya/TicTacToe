package ru.kpfu.itis;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server implements ConnectionListener {

    //    private static char crossSymbol = 'X';
//    private static char zeroSymbol = 'O';
//

    private int[][] wins = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private int[] positions = new int[9];

    private int turn;

    HashMap<Integer, Character> maps = new HashMap<>();

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
//        c.add(tcpConnection);
    }

    private boolean yourTurn = true;

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

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}

class Turn {
    Integer position;
    Character symbol;

    public Turn(Integer position, Character symbol) {
        this.position = position;
        this.symbol = symbol;
    }
}
