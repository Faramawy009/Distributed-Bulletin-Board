package edu.umn.FaraHany.ServerSide;

import java.util.Scanner;

import static java.lang.System.out;

public class ServerMain {
    public static String myIp;
    public static int myPort;
    public static void main(String args[]) {
        out.print("Enter your ip: ");
        Scanner sc = new Scanner(System.in);
        myIp= sc.nextLine();
        out.print("\nEnter your port: ");
        myPort = sc.nextInt();
        Thread clientHandler = null;
        {
            try {
                clientHandler = new Thread(new ClientReqRecv(12345));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientHandler.start();
        while(true);
    }
}
