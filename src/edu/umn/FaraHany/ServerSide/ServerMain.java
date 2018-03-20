package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class ServerMain {
    public static String myIp = "localhost";
    public static int clientListeningPort;
    public static int serverReadPort;
    public static int serverWriterPort;
    public static void main(String args[]) {
        out.print("Enter server ID between 1 and "+ServersManager.numberOfReplicas);
        out.print("  DO NOT ENTER AN ALREADY IN USE ID: ");
        Scanner sc = new Scanner(System.in);
        int id = sc.nextInt();
        clientListeningPort = ServersManager.clientPortBase+id-1;
        serverReadPort = ServersManager.severReadPortBase+id-1;
        serverWriterPort = ServersManager.serverWritePortBase+id-1;

        Thread readListener = null;
        try {
            readListener = new Thread(new ServerReadListener(serverReadPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        readListener.start();

        Thread writeListener = null;
        try {
            writeListener = new Thread(new ServerWriteListener(serverWriterPort));
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeListener.start();

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