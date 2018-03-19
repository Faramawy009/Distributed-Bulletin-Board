package edu.umn.FaraHany.ServerSide;

public class ServerAddress{
    private String ip;
    private int clientListenPort;
    private int serverListenPort;
    private int leaderListenPort;


    public ServerAddress(String ip, int clientListenPort,int serverListenPort, int leaderListenPort) {
        this.ip = ip;
        this.clientListenPort = clientListenPort;
        this.serverListenPort = serverListenPort;
        this.leaderListenPort = leaderListenPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getClientListenPort() {
        return clientListenPort;
    }

    public void setClientListenPort(int clientListenPort) {
        this.clientListenPort = clientListenPort;
    }

    public int getServerListenPort() {
        return serverListenPort;
    }

    public void setServerListenPort(int serverListenPort) {
        this.serverListenPort = serverListenPort;
    }

    @Override
    public boolean equals(Object o) {
        ServerAddress other = (ServerAddress) o;
        return this.ip.equals(other.ip) && this.serverListenPort==other.serverListenPort;
    }
}