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
//        out.print("Enter your ip: ");
        Scanner sc = new Scanner(System.in);
//        String ip = sc.nextLine();
//        out.print("\nEnter your port: ");
//        int port = sc.nextInt();

        String selection = "";
        while (true) {
            out.println("Please enter one of the following");
            out.println("connect;server_ip;server_port");
            out.println("end");
//            sc.nextLine();
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
                String[] reqElements = request.split(";");
                if (reqElements[0].equals("choose") || reqElements[0].equals("read") || reqElements[0].equals("post")
                        || reqElements[0].equals("reply")) {
                    try {
                        System.out.println(contactServer(request, serverip, serverPort));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (reqElements[0].equals("disconnect")) {
                    continue;
                } else {
                    out.println("Invalid input");
                }
            }
        }
    }


//
//        String request = sc.nextLine();
//        String[] elements = request.split(";");
//        switch (elements[0]) {
//            case("connect"):
//                serverip = elements[1];
//                serverPort = Integer.parseInt(elements[2]);
//                break;
//            case("disconnect"):
//                serverip = "";
//                serverPort = -1;
//                break;
//            case("choose"):
//            case("read"):
//            case("post"):
//            case("reply"):
//                out.println("processing your request...");
//                Socket clientSocket = null;
//
//                try {
//                    clientSocket = new Socket(serverip, serverPort);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                DataOutputStream outToServer = null;
//                try {
//                    outToServer = new DataOutputStream(clientSocket.getOutputStream());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    outToServer.writeBytes(request);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                modifiedSentence = inFromServer.readLine();
//                out.println("FROM SERVER: " + modifiedSentence);
//                clientSocket.close();
//    }
//    }
}
