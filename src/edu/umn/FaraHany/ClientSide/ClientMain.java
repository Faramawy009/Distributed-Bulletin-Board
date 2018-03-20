package edu.umn.FaraHany.ClientSide;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.out;

public class ClientMain {

    public static String contactServer(String msg, String ip, int port) throws Exception{
        Socket clientSocket = new Socket(ip, port);
        OutputStream outToServer = clientSocket.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);

        out.writeUTF(msg);

        InputStream inFromServer = clientSocket.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
        String ans = in.readUTF();
        clientSocket.close();
        return ans;
    }

    public static void main (String atgs[]) {
        String serverip = "";
        int serverPort = -1;
        Scanner sc = new Scanner(System.in);
        String selection = "";
        while (true) {
            out.println("Please enter one of the following");
            out.println("connect;server_ip;server_port");
            out.println("end");
            selection = sc.nextLine();

            String[] elements = selection.split(";");
            if (elements[0].equals("connect")) {
                if (elements.length != 3) {
                    out.println("Invalid input");
                    continue;
                }
                serverip = elements[1];
                serverPort = Integer.parseInt(elements[2]);
            } else if (elements[0].equals("end")) {
                out.println("Terminating process...");
                exit(1);
            } else {
                out.println("Invalid input");
                continue;
            }

            String request = "";
            while (!request.equals("disconnect")) {
                out.println("please enter your option from the following");
                out.println("disconnect");
                out.println("choose;id");
                out.println("read");
                out.println("post;title;content");
                out.println("reply;id;title;content");
                request = sc.nextLine();
                long start = System.currentTimeMillis();
                String[] reqElements = request.split(";");
                if (reqElements[0].equals("choose") || reqElements[0].equals("read") || reqElements[0].equals("post")
                        || reqElements[0].equals("reply")) {
                    try {
                        System.out.print(contactServer(request, serverip, serverPort));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("Time consumed: "+(end-start)+ " MilliSeconds\n\n");
                } else if (reqElements[0].equals("disconnect")) {
                    continue;
                } else {
                    out.println("Invalid input");
                }
            }
        }
    }
}