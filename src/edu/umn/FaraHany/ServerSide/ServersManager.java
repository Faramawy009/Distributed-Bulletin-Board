package edu.umn.FaraHany.ServerSide;

import sun.awt.Mutex;

import java.util.ArrayList;
import java.util.Random;

public class ServersManager {
    static ArrayList<ServerAddress> addresses;
    static int clientPortBase = 12340;
    static int serverPortBase = 23450;
    public static Mutex leaderLock;
    public final static int numberOfReplicas = 2;
    static {
        leaderLock = new Mutex();
        addresses = new ArrayList<>();
        for(int i=0; i<numberOfReplicas; i++) {
            addresses.add(new ServerAddress("localhost",clientPortBase+i,serverPortBase+i));
        }
    }
    static public String getLeaderIp() {return "localhost";}
    static public int getLeaderPort() {return serverPortBase;}
    static public boolean isLeader(String ip, int port) {
        return ip.equals("localhost") && port==serverPortBase;
    }
}
