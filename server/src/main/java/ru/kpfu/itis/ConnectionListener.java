package ru.kpfu.itis;

public interface ConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveObject(TCPConnection tcpConnection, Object object);

    void onDisconnect(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, Exception e);

}
