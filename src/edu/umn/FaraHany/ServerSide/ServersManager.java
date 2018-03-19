package edu.umn.FaraHany.ServerSide;

import sun.awt.Mutex;

import java.util.ArrayList;
import java.util.Random;

public class ServersManager {
    static ArrayList<ServerAddress> addresses;
    static int clientPortBase = 12340;
    static int serverPortBase = 23450;
    static int leaderPortBase = 34560;
    public static Mutex leaderLock;
    public static String leaderIp;
    public static int leaderPort;
    public final static int numberOfReplicas = 4;
    public static boolean isLeader;
    static {
        isLeader = false;
        leaderIp="localhost";
        leaderPort=leaderPortBase;
        leaderLock = new Mutex();
        addresses = new ArrayList<>();
        for(int i=0; i<numberOfReplicas; i++) {
            addresses.add(new ServerAddress("localhost",
                    clientPortBase+i,
                    serverPortBase+i,
                    leaderPortBase+i));
        }
    }
    static public String getLeaderIp() {return leaderIp;}
    static public int getLeaderPort() {return leaderPort;}
    static public boolean isLeader(String ip, int port) {
        return ip.equals(leaderIp) && port==leaderPort;
    }

    public static void setLeaderIp(String leaderIp) {
        ServersManager.leaderIp = leaderIp;
    }

    public static void setLeaderPort(int leaderPort) {
        ServersManager.leaderPort = leaderPort;
    }
}
