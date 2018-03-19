package edu.umn.FaraHany.ServerSide;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class UniqueRandReplicasGenerator {
    private static ArrayList<ServerAddress> serverAddresses;
    private static ArrayList<Integer> indicesList;
    static {
        serverAddresses.addAll(ServersManager.addresses);
        indicesList = new ArrayList<>();
        for(int i=0; i<ServersManager.numberOfReplicas; i++) {
            indicesList.add(i);
        }
    }
    private static boolean isMe(ServerAddress other) {
        return ServerMain.myIp.equals(other.getIp())
                && ServerMain.clientListeningPort == other.getClientPort()
                && ServerMain.serverReadPort == other.getServerReadPort()
                && ServerMain.serverWriterPort == other.getServerWritePort();
    }
    public static ArrayList<ServerAddress> generateReadReplicas(int n) {
        ArrayList<ServerAddress> returnAddresses = new ArrayList<>();
        Collections.shuffle(indicesList);
        for(int i=0; i<n; i++) {
            returnAddresses.add(serverAddresses.get(indicesList.get(i)));
        }
        return returnAddresses;
    }

    public static ArrayList<ServerAddress> generateWriteReplicas(int n) {
        ArrayList<ServerAddress> returnAddresses = new ArrayList<>();
        Collections.shuffle(indicesList);
        int currentIndex = 0;
        while(returnAddresses.size()<n) {
            if(!isMe(serverAddresses.get(indicesList.get(currentIndex)))) {
                returnAddresses.add(serverAddresses.get(indicesList.get(currentIndex)));
            }
            currentIndex++;
            if(currentIndex<serverAddresses.size())
            {
                System.out.println("Out of bounds index in generating write replicas");
            }
        }
        return returnAddresses;
    }

}
