package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;

public class ServerMain {
    public static String myIp;
    public static int clientListeningPort;
    public static int myPort;
    public static void main(String args[]) {
        out.print("Enter server ip: ");
        Scanner sc = new Scanner(System.in);
        myIp= sc.nextLine();
        out.print("\nEnter port for listening to clients: ");
        clientListeningPort = sc.nextInt();
        out.print("\nEnter port for listening to servers: ");
        myPort = sc.nextInt();
        Thread clientHandler = null;
        try {
            clientHandler = new Thread(new ClientReqRecv(clientListeningPort));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(myPort == ServersManager.getLeaderPort() ) {
            Thread leaderUpdateListener = null;
            try {
                leaderUpdateListener = new Thread(new LeaderRecv(myPort));
            } catch (Exception e) {
                e.printStackTrace();
            }
            leaderUpdateListener.start();
        } else {
            Thread bcastListener = null;
            try {
                bcastListener = new Thread(new ServerBcastRecv(myPort));
            } catch (Exception e) {
                e.printStackTrace();
            }
            bcastListener.start();
        }

        clientHandler.start();
        while(true);
    }
}
