package edu.umn.FaraHany.ServerSide;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientReqReply implements Runnable{
    private Socket clientSocket;
    public ClientReqReply(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public int serverToLeaderHandler(String clientRequest) {
        int id = -1;
        String coordinatorMsg =
                ServerMain.myIp +";"+ ServerMain.myPort +"#"+ clientRequest;
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(
                    ServersManager.getLeaderIp(),
                    ServersManager.getLeaderPort());
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(coordinatorMsg);

            InputStream inFromServer = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            String ans = in.readUTF();
            clientSocket.close();
            id = Integer.parseInt(ans);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }


    public void leaderToServersHandler(String clientRequest) {
        String serversMsg =
                BulletinBoard.getSize() +"#"+ clientRequest;
        ArrayList<Thread> arrThreads = new ArrayList<>();
        // broad cast to all ohter servers
        for (int i = 1; i < ServersManager.numberOfReplicas; i++)
        {
            Thread T1 = new Thread(new TCPSenderThread(serversMsg,
                        ServersManager.addresses.get(i).getIp(),
                    ServersManager.addresses.get(i).getServerListenPort()));
            T1.start();
            arrThreads.add(T1);
        }

        for (int i = 0; i < arrThreads.size(); i++)
        {
            try {
                arrThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int articleId = -1;
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
        String reply;
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
                    //If I am not the leader....
                    if(!ServersManager.isLeader(
                            ServerMain.myIp,
                            ServerMain.myPort)) {
                        articleId = serverToLeaderHandler(request);
                        if(articleId==-1) {
                            reply = (BulletinBoard.class.getName() + ": invalid parent id input");
                        } else {
                            reply = BulletinBoard.postWithId(articleId,elements[1],elements[2]);
                        }
                    } else{
                        ServersManager.leaderLock.lock();
                        reply = BulletinBoard.post(elements[1], elements[2]);
                        leaderToServersHandler(request);
                        ServersManager.leaderLock.unlock();
                    }
                    break;
                }
            case "reply":
                if(elements.length == 4) {
                    int parentId = Integer.parseInt(elements[1]);
                    if(!ServersManager.isLeader(
                            ServerMain.myIp,
                            ServerMain.myPort)) {
                        articleId = serverToLeaderHandler(request);
                        if(articleId==-1) {
                            reply = (BulletinBoard.class.getName() + ": invalid parent id input");
                        } else {
                            reply = BulletinBoard.replyWithId(articleId,parentId,elements[1],elements[2]);
                        }
                    } else {
                        ServersManager.leaderLock.lock();
                        reply = BulletinBoard.reply(parentId, elements[2], elements[3]);
                        leaderToServersHandler(request);
                        ServersManager.leaderLock.unlock();
                    }
                    break;
                }
            default:
                System.out.println(this.getClass().getName() + ": invalid input");
                reply = (this.getClass().getName() + ": invalid input");
        }
        try {
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("\n\nServer reply:\n"+
                                "-------------\n" +reply+"-------------\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
