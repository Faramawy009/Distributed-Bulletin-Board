package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerReadListener implements Runnable {
    private ServerSocket serverSocket;

    public ServerReadListener(int port) throws Exception {
        serverSocket = new ServerSocket(port);
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
                System.out.println("Received this database " + request);
                OutputStream outToServer = server.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
                out.writeUTF(BulletinBoard.buildDB());
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
