package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBcastRecv implements Runnable {

    private ServerSocket serverSocket;

    public ServerBcastRecv(int port) throws Exception {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
//        System.out.println("Hello from Bcast Recv Thread");
        while (true) {
            String request = null;
            try {
                Socket server = serverSocket.accept();
                DataInputStream in = null;
                in = new DataInputStream(server.getInputStream());
                request = in.readUTF();
                System.out.println("Received this broadcast "+ request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String senderInfo = request.split("\\(\\)")[0];
            System.out.println("sender info is "+senderInfo);
            ServersManager.setLeaderIp(senderInfo.split(";")[0]);
            ServersManager.setLeaderPort(Integer.parseInt(senderInfo.split(";")[1]));
            System.out.println("New leader is port "+ senderInfo.split(";")[1]);
            String DB = request.split("\\(\\)")[1];
            String[] articles = DB.split("#");
            BulletinBoard.clear();
            for(String s: articles) {
                BulletinBoard.insert(s);
            }
        }
    }
}