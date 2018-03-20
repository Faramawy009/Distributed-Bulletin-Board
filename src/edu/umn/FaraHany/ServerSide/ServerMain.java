package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;

public class ServerMain {
    public static String myIp = "localhost";
    public static int clientListeningPort;
    public static int myPort;
    public static int myLeaderPort;
    public static void main(String args[]) {
        out.print("Enter server ID between 1 and "+ServersManager.numberOfReplicas);
        out.print("  DO NOT ENTER AN ALREADY IN USE ID: ");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        clientListeningPort = ServersManager.clientPortBase+id-1;
        myPort = ServersManager.serverPortBase+id-1;
        myLeaderPort = ServersManager.leaderPortBase+id-1;

        if(ServersManager.isLeader(myIp, myLeaderPort)) {
            ServersManager.isLeader = true; }


        Thread leaderDBHandler = null;
        try {
            leaderDBHandler = new Thread(new LeaderGetDBReq(myLeaderPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        leaderDBHandler.start();

        Thread bcastListener = null;
        try {
            bcastListener = new Thread(new ServerBcastRecv(myPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bcastListener.start();

        Thread clientListener = null;
        try {
            clientListener = new Thread(new ClientReqRecv(clientListeningPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        clientListener.start();


        while(true);
    }
}