package edu.umn.FaraHany.ServerSide;

import java.util.ArrayList;
import java.util.Random;

public class ServersManager {
    static ArrayList<ServerAddress> addresses;
    static {
        final int numberOfReplicas = 5;
        int portBase = 12340;
        addresses = new ArrayList<>();
        for(int i=0; i<numberOfReplicas; i++) {
            addresses.add(new ServerAddress(portBase++,"localhost"));
        }
    }
    static public String getLeaderIp() {return "localhost";}
    static public int getLeaderPort() {return 12340;}
}
