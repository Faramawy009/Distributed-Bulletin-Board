package edu.umn.FaraHany.ServerSide;

import sun.awt.Mutex;

import java.util.ArrayList;
import java.util.Random;

public class ServersManager {
    static ArrayList<ServerAddress> addresses;
    static int clientPortBase = 12340;
    static int severReadPortBase = 23450;
    static int serverWritePortBase = 34560;
    public static Mutex DbWriteLock;
    public final static int numberOfReplicas = 5;
    public final static int numReadReplicas = 2;
    public final static int numWriteReplicas = 4;
    static {
        DbWriteLock = new Mutex();
        addresses = new ArrayList<>();
        for(int i=0; i<numberOfReplicas; i++) {
            addresses.add(new ServerAddress("localhost",
                    clientPortBase+i,
                    severReadPortBase+i,
                    serverWritePortBase+i));
        }
    }
}