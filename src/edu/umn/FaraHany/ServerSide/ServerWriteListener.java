package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWriteListener implements Runnable {
    private ServerSocket serverSocket;

    public ServerWriteListener(int port) throws Exception {
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
                ServersManager.DbWriteLock.lock();
                BulletinBoard.updateDB(request);
                ServersManager.DbWriteLock.unlock();
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
