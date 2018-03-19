package edu.umn.FaraHany.ServerSide;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPSenderThread implements  Runnable {
    public TCPSenderThread(String msg, String ip, int port) {
        this.msg = msg;
        this.ip = ip;
        this.port = port;
    }

    private String msg;
    private String ip;
    private int port;
    @Override
    public void run() {
        try {
            Socket clientSocket = new Socket(ip, port);
            OutputStream outToServer = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF(msg);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}