package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LeaderGetDBReq implements Runnable{
    private ServerSocket serverSocket;

    public LeaderGetDBReq(int port) throws Exception {
        serverSocket = new ServerSocket(port);
    }
    @Override
    public void run() {
        while(true) {
            String request = "";
            Socket server = null;
            DataOutputStream out = null;
            try {
                server = serverSocket.accept();
                DataInputStream in = null;
                in = new DataInputStream(server.getInputStream());
                request = in.readUTF();
                OutputStream outToServer = server.getOutputStream();
                Socket clientSocket;
                out = new DataOutputStream(outToServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // if I am not the leader anymore, reply NEGATIVE
            if (!ServersManager.isLeader(ServerMain.myIp, ServerMain.myLeaderPort)) {
                try {
                    out.writeUTF("NEGATIVE");
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }
            // indicate I am not the leader for future requests
            System.out.println("I am no longer the leader "+ ServerMain.myIp+":"+ServerMain.myLeaderPort);
            ServersManager.isLeader = false;
            String req = request.split(";")[0];
            int deltaStart = Integer.parseInt(request.split(";")[1]);
            if (!req.equals("GET")) {
                try {
                    out.writeUTF("\n\nLeader reply:\n" +
                            "-------------\n" + "Invalid Input!!" + "-------------\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
//                ServersManager.leaderLock.lock();
                StringBuilder DBdelta = new StringBuilder();
                for (int i = deltaStart; i < BulletinBoard.getSize(); i++) {
                    Article a = BulletinBoard.getArticles().get(i);
                    DBdelta.append(a.getId() + ";");
                    DBdelta.append(a.getIndentLevel() + ";");
                    DBdelta.append(a.getParent() + ";");
                    DBdelta.append(a.getTitle() + ";");
                    DBdelta.append(a.getContent() + ";");
                    for (int k = 0; k < a.getReplies().size(); k++) {
                        DBdelta.append(a.getReplies().get(k) + ",");
                    }
                    DBdelta.append("#");
                }
                try {
                    out.writeUTF(DBdelta.toString());
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                ServersManager.leaderLock.unlock();
            }
        }
    }
}
