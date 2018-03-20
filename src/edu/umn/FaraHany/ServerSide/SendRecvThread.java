package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

public class SendRecvThread implements Runnable {
    String [] DBs;
    int index;
    String ip;
    int port;
    SendRecvThread(String [] DBs, int index,
            String ip, int port){
        this.DBs = DBs;
        this.index = index;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void run() {
        try {
            Socket clientSocket = new Socket(ip, port);
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("GET: DB");
            InputStream inFromServer = clientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            String response = in.readUTF();
            DBs[index] = response;
            clientSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
