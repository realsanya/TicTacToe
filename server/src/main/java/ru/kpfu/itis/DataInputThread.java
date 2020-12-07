package ru.kpfu.itis;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class DataInputThread extends Thread {

    private ObjectInputStream objectInputStream;
    private Client client;

    public DataInputThread(InputStream inputStream, Client client) {
        try {
            this.objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client = client;
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                client.setPosition(Integer.parseInt((String) objectInputStream.readObject()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
