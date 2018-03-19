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
        switch(elements[0]) {
            case "read":
//                reply = BulletinBoard.read();
                readReplicas = UniqueRandReplicasGenerator.generateReadReplicas(ServersManager.numReadReplicas);
                for(ServerAddress s: readReplicas){
                    String currentDb = TcpCommunicate.sendRecv("NONE",s.getIp(),s.getServerReadPort());
                    int  currentVersion = Integer.parseInt(currentDb.split("\\(\\)")[0]);
                    if(currentVersion > highestVersion) {
                        latestVersionDB = currentDb;
                    }
                }
                BulletinBoard.updateDB(latestVersionDB);
                reply = BulletinBoard.read();
                break;
            case "choose":
                if(elements.length == 2) {
                    readReplicas = UniqueRandReplicasGenerator.generateReadReplicas(ServersManager.numReadReplicas);
                    for(ServerAddress s: readReplicas){
                        String currentDb = TcpCommunicate.sendRecv("NONE",s.getIp(),s.getServerReadPort());
                        int  currentVersion = Integer.parseInt(currentDb.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = currentDb;
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
                    //TODO: parallelism?
                    for(ServerAddress s: writeReplicas){
                        String currentDb = TcpCommunicate.sendRecv("NONE",s.getIp(),s.getServerReadPort());
                        int  currentVersion = Integer.parseInt(currentDb.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = currentDb;
                        }
                    }
                    if(highestVersion > BulletinBoard.getSize())
                        BulletinBoard.updateDB(latestVersionDB);
                    reply = BulletinBoard.post(elements[1], elements[2]);
                    TcpCommunicate.broadCast(BulletinBoard.buildDB(),writeReplicas);
                    break;
                }
            case "reply":
                if(elements.length == 4) {
                    writeReplicas = UniqueRandReplicasGenerator.generateWriteReplicas(ServersManager.numWriteReplicas-1);
                    //TODO: parallelism?
                    for(ServerAddress s: writeReplicas){
                        String currentDb = TcpCommunicate.sendRecv("NONE",s.getIp(),s.getServerReadPort());
                        int  currentVersion = Integer.parseInt(currentDb.split("\\(\\)")[0]);
                        if(currentVersion > highestVersion) {
                            latestVersionDB = currentDb;
                        }
                    }
                    if(highestVersion > BulletinBoard.getSize())
                        BulletinBoard.updateDB(latestVersionDB);
                    int parentId = Integer.parseInt(elements[1]);
                    reply = BulletinBoard.reply(parentId,elements[2],elements[3]);
                    TcpCommunicate.broadCast(BulletinBoard.buildDB(),writeReplicas);
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
