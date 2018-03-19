package edu.umn.FaraHany.ServerSide;

import java.io.*;
import java.net.Socket;

import static java.lang.System.out;

public class ClientReqReply implements Runnable{
    private Socket clientSocket;
    public ClientReqReply(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String buildDB() {
        StringBuilder DB = new StringBuilder();
        DB.append(ServerMain.myIp+";"+ServerMain.myLeaderPort+"()");
        for(int i=0; i<BulletinBoard.getSize(); i++) {
            Article a = BulletinBoard.getArticles().get(i);
            DB.append(a.getId()+";");
            DB.append(a.getIndentLevel()+";");
            DB.append(a.getParent()+";");
            DB.append(a.getTitle()+";");
            DB.append(a.getContent()+";");
            int k=0;
            for(k=0; k<a.getReplies().size(); k++) {
                DB.append(a.getReplies().get(k)+",");
            }
            DB.append("#");
        }
        return DB.toString();
    }

    @Override
    public void run() {
        boolean needToBroadcast = false;
        DataInputStream in = null;
        try {
            in = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String request = null;
        try {
            request = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String [] elements = request.split(";");
        String reply = "";
        switch(elements[0]) {
            case "read":
                reply = BulletinBoard.read();
                break;
            case "choose":
                if(elements.length == 2) {
                    int id = Integer.parseInt(elements[1]);
                    reply = BulletinBoard.choose(id);
                    break;
                }
            case "post":
                if(elements.length == 3) {
                    // if I am the leader
                    if (ServersManager.isLeader(ServerMain.myIp,ServerMain.myLeaderPort)) {
                        ServersManager.leaderLock.lock();
                        reply = BulletinBoard.post(elements[1], elements[2]);
                        ServersManager.leaderLock.unlock();

                    } else{
                        String req = "GET;"+BulletinBoard.getSize();
                        String response;
                        while(true) {
                            response = TcpCommunicate.sendRecv(req,
                                    ServersManager.leaderIp,
                                    ServersManager.leaderPort);
                            if (!response.equals("NEGATIVE"))
                                break;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        ServersManager.isLeader = true;
                        out.println("I am the leader "+ServerMain.myIp+":"+ServerMain.myLeaderPort);
                        ServersManager.setLeaderPort(ServerMain.myLeaderPort);
                        ServersManager.setLeaderIp(ServerMain.myIp);
                        String[] deltaArticles = response.split("#");
                        for(String s: deltaArticles) {
                            if(!s.equals(""))
                                BulletinBoard.insert(s);
                        }
                        reply = BulletinBoard.post(elements[1], elements[2]);
                        needToBroadcast = true;
                    }
                    break;
                }
            case "reply":
                if(elements.length == 4) {
                    // if I am the leader
                    if (ServersManager.isLeader(ServerMain.myIp,ServerMain.myLeaderPort)) {
                        ServersManager.leaderLock.lock();
                        int parentId = Integer.parseInt(elements[1]);
                        reply = BulletinBoard.reply(parentId,elements[2],elements[3]);
                        ServersManager.leaderLock.unlock();

                    } else {
                        String req = "GET;"+BulletinBoard.getSize();
                        String response;
                        while(true) {
                            response = TcpCommunicate.sendRecv(req,
                                    ServersManager.leaderIp,
                                    ServersManager.leaderPort);
                            if (!response.equals("NEGATIVE"))
                                break;
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        ServersManager.isLeader = true;
                        ServersManager.setLeaderPort(ServerMain.myLeaderPort);
                        ServersManager.setLeaderIp(ServerMain.myIp);
                        String[] deltaArticles = response.split("#");
                        for(String s: deltaArticles) {
                            BulletinBoard.insert(s);
                        }
                        int parentId = Integer.parseInt(elements[1]);
                        reply = BulletinBoard.reply(parentId,elements[2],elements[3]);
                        needToBroadcast = true;
                    }

                    break;
                }
            default:
                out.println(this.getClass().getName() + ": invalid input");
                reply = (this.getClass().getName() + ": invalid input");
        }
//        String msg = reply.split(": ")[1] + "#" + request;
//        TcpCommunicate.broadcast(ServerMain.myIp, ServerMain.myPort, msg);
        try {
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("\n\nServer reply:\n"+
                    "-------------\n" +reply+"\n-------------\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (needToBroadcast) {
            String broadcastMsg = buildDB();
            out.println("DB builder string is "+broadcastMsg);
            TcpCommunicate.broadcast(ServerMain.myIp, ServerMain.myPort, broadcastMsg);
        }

    }
}
