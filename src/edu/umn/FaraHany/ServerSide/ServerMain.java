package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;

public class ServerMain {
    public static String myIp;
    public static int clientListeningPort;
    public static int myPort;
    public static int myLeaderPort;
    public static void main(String args[]) {
        out.print("Enter server ip: ");
        Scanner sc = new Scanner(System.in);
        myIp= sc.nextLine();
        out.print("\nEnter port for listening to clients: ");
        clientListeningPort = sc.nextInt();
        out.print("\nEnter port for listening to servers: ");
        myPort = sc.nextInt();
        out.print("\nEnter port for linstening when you are the leader: ");
        myLeaderPort = sc.nextInt();

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