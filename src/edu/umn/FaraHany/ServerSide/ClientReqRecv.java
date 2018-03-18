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
                new Thread(new ClientReqReply(server)).start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
