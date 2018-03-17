package edu.umn.FaraHany.ServerSide;

public class ServerAddress implements Comparable<ServerAddress>{
    private int port;
    private String ip;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public ServerAddress(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    @Override
    public int compareTo(ServerAddress serverAddress) {
        return port-serverAddress.port;
    }
}
