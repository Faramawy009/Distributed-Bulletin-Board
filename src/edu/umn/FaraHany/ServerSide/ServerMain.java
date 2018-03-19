package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;

public class ServerMain {
    public static String myIp;
    public static int clientListeningPort;
    public static int serverReadPort;
    public static int serverWriterPort;
    public static void main(String args[]) {
        out.print("Enter server ip: ");
        Scanner sc = new Scanner(System.in);
        myIp= sc.nextLine();
        out.print("\nEnter port for listening to clients: ");
        clientListeningPort = sc.nextInt();
        out.print("\nEnter port for listening to reads: ");
        serverReadPort = sc.nextInt();
        out.print("\nEnter port for listening to writes: ");
        serverWriterPort = sc.nextInt();

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