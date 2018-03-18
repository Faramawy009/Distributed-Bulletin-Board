package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class LeaderRecv implements Runnable{

    private ServerSocket serverSocket;
    public LeaderRecv(int port) throws Exception{
        serverSocket = new ServerSocket(port);
    }

    public void leaderToServersHandler(String query, String excludeAddress) {
        String excludeIp = excludeAddress.split(";")[0];
        int excludePort = Integer.parseInt(excludeAddress.split(";")[1]);
        ArrayList<Thread> arrThreads = new ArrayList<>();
        // broad cast to all ohter servers
        for (int i = 1; i < ServersManager.numberOfReplicas; i++)
        {
            if(excludeIp.equals(ServersManager.addresses.get(i).getIp()) &&
                    excludePort == ServersManager.addresses.get(i).getServerListenPort()) {
                continue;
            }
            Thread T1 = new Thread(new TCPSenderThread(BulletinBoard.getSize()+"$"+query,
                    ServersManager.addresses.get(i).getIp(),
                    ServersManager.addresses.get(i).getServerListenPort()));
            T1.start();
            arrThreads.add(T1);
        }

        for (int i = 0; i < arrThreads.size(); i++)
        {
            if(excludeIp.equals(ServersManager.addresses.get(i).getIp()) &&
                    excludePort == ServersManager.addresses.get(i).getServerListenPort()) {
            continue;
        }
            try {
                arrThreads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            String request = null;
            try {
                Socket server = serverSocket.accept();
                DataInputStream in = null;
                in = new DataInputStream(server.getInputStream());
                request = in.readUTF();
            } catch (Exception e){
                e.printStackTrace();
            }
            String query = request.split("$")[1];
            String[] elements = query.split(";");
            switch(elements[0]){
                case "post":
                    if(elements.length == 3) {
                        BulletinBoard.post(elements[1], elements[2]);
                        break;
                    }
                case "reply":
                    if(elements.length == 4) {
                        int parentId = Integer.parseInt(elements[1]);
                        BulletinBoard.reply(parentId,elements[2],elements[3]);
                        break;
                    }
            }
            ServersManager.leaderLock.lock();

        }
    }
}
