package edu.umn.FaraHany.ServerSide;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientReqReply implements Runnable{
    private Socket clientSocket;
    public ClientReqReply(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
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
        ArrayList<ServerAddress> readReplicas;
        ArrayList<ServerAddress> writeReplicas;
        String latestVersionDB = "";
        int highestVersion = 0;
        String [] DBs;
        ArrayList<Integer> versions;
        switch(elements[0]) {
            case "read":
                readReplicas = UniqueRandReplicasGenerator.generateReadReplicas(ServersManager.numReadReplicas);
                DBs = TcpCommunicate.concurrentSendRecv(readReplicas);
                for (String DB : DBs) {
                    int  currentVersion = Integer.parseInt(DB.split("\\(\\)")[0]);
                    if(currentVersion > highestVersion) {
                        latestVersionDB = DB;
                        highestVersion = currentVersion;
                    }
                }
                BulletinBoard.updateDB(latestVersionDB);
                reply = BulletinBoard.read();
                break;
            case "choose":
                if(elements.length == 2) {
                    readReplicas = UniqueRandReplicasGenerator.generateReadReplicas(ServersManager.numReadReplicas);
                    DBs = TcpCommunicate.concurrentSendRecv(readReplicas);
                    for (String DB : DBs) {
                        int  currentVersion = Integer.parseInt(DB.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = DB;
                            highestVersion = currentVersion;
                        }
                    }
                    BulletinBoard.updateDB(latestVersionDB);
                    int id = Integer.parseInt(elements[1]);
                    reply = BulletinBoard.choose(id);
                    break;
                }
            case "post":
                if(elements.length == 3) {
                    writeReplicas = UniqueRandReplicasGenerator.generateWriteReplicas(ServersManager.numWriteReplicas-1);
                    versions = new ArrayList<>(writeReplicas.size());
                    DBs = TcpCommunicate.concurrentSendRecv(writeReplicas);
                    for (String DB : DBs) {
                        int  currentVersion = Integer.parseInt(DB.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = DB;
                            highestVersion = currentVersion;
                        }
                        versions.add(currentVersion);
                    }
                    if(highestVersion > BulletinBoard.getSize())
                        BulletinBoard.updateDB(latestVersionDB);
                    reply = BulletinBoard.post(elements[1], elements[2]);
                    for (int i=0; i<writeReplicas.size(); i++) {
                        String msg = BulletinBoard.builDeltadDB(versions.get(i));
                        Thread sender = new Thread(new TcpSenderThread(msg,
                                writeReplicas.get(i).getIp(),
                                writeReplicas.get(i).getServerWritePort()));
                        sender.start();
                    }
                    break;
                }
            case "reply":
                if(elements.length == 4) {
                    writeReplicas = UniqueRandReplicasGenerator.generateWriteReplicas(ServersManager.numWriteReplicas-1);
                    versions = new ArrayList<>(writeReplicas.size());
                    DBs = TcpCommunicate.concurrentSendRecv(writeReplicas);
                    for (String DB : DBs) {
                        int  currentVersion = Integer.parseInt(DB.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = DB;
                            highestVersion = currentVersion;
                        }
                        versions.add(currentVersion);
                    }
                    if(highestVersion > BulletinBoard.getSize())
                        BulletinBoard.updateDB(latestVersionDB);
                    int parentId = Integer.parseInt(elements[1]);
                    reply = BulletinBoard.reply(parentId,elements[2],elements[3]);
                    for (int i=0; i<writeReplicas.size(); i++) {
                        String msg = BulletinBoard.builDeltadDB(versions.get(i));
                        Thread sender = new Thread(new TcpSenderThread(msg,
                                writeReplicas.get(i).getIp(),
                                writeReplicas.get(i).getServerWritePort()));
                        sender.start();
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
                                "-------------\n" +reply+"\n-------------\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
