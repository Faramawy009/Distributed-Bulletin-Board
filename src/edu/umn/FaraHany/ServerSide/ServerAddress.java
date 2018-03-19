package edu.umn.FaraHany.ServerSide;

public class ServerAddress{
    private int clientPort;
    private int serverReadPort;
    private int serverWritePort;
    private String ip;

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public int getServerReadPort() {
        return serverReadPort;
    }

    public void setServerReadPort(int serverReadPort) {
        this.serverReadPort = serverReadPort;
    }

    public int getServerWritePort() {
        return serverWritePort;
    }

    public void setServerWritePort(int serverWritePort) {
        this.serverWritePort = serverWritePort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ServerAddress(String ip, int clientPort, int serverReadPort, int serverWritePort) {
        this.clientPort = clientPort;
        this.serverReadPort = serverReadPort;
        this.serverWritePort = serverWritePort;
        this.ip = ip;
    }
}
