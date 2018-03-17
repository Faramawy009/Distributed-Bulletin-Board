package edu.umn.FaraHany.ServerSide;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientReqRecv implements Runnable{
    private ServerSocket serverSocket;
    public ClientReqRecv(int port) throws Exception{
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
//                DataInputStream in = new DataInputStream(server.getInputStream());

//                System.out.println(in.readUTF());
//                DataOutputStream out = new DataOutputStream(server.getOutputStream());
//                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
//                        + "\nGoodbye!");
//                Thread requestHandler = new Thread(new ClientReqReply(in.readUTF(),
//                        server.getRemoteSocketAddress().toString(), server.getPort()));
//                requestHandler.start();
//                DataOutputStream out = new DataOutputStream(server.getOutputStream());
//                out.writeUTF("Zobry");
                new Thread(new ClientReqReply(server)).start();
//                server.close();
            } catch (Exception e){
                e.printStackTrace();
            }
//
//
//
//
//            Socket connectionSocket = null;
//            try {
//                connectionSocket = sock.accept();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            BufferedReader inFromClient = null;
//            try {
//                inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        Thread requestHandler = new Thread(new ClientReqReply(inFromClient.toString(),
//                connectionSocket.getRemoteSocketAddress().toString(), connectionSocket.getPort()));
//            requestHandler.start();
        }
    }
}
