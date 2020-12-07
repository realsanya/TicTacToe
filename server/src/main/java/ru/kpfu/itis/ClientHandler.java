package ru.kpfu.itis;

import java.io.*;

public class ClientHandler extends Thread {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(InputStream inputStream, OutputStream outputStream) {
        try {
            this.inputStream = new ObjectInputStream(inputStream);
            this.outputStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        String data;
        while (true) {
            try {
                data = (String) inputStream.readObject();
                System.out.println(data);
                outputStream.writeObject(data);
                outputStream.flush();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
