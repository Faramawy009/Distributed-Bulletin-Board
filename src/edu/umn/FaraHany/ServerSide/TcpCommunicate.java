package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TcpCommunicate {
    static void broadCast(String msg, ArrayList<ServerAddress> servers) {
        for(int i=0; i<servers.size(); i++) {
            Thread T1 = new Thread(new TcpSenderThread(msg,
                    servers.get(i).getIp(),
                    servers.get(i).getServerWritePort()));
            T1.start();
        }
    }

    static String sendRecv(String msg, String ip, int port){
        String response = "";
        try {
            Socket clientSocket = new Socket(ip, port);
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(msg);
            InputStream inFromServer = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            response = in.readUTF();
            clientSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}