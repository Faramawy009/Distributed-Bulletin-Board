package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBcastRecv implements Runnable {

    private ServerSocket serverSocket;

    public ServerBcastRecv(int port) throws Exception {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            int id = Integer.parseInt(request.split("#")[0]);
            String query = request.split("#")[1];
            String[] elements = query.split(";");
            switch(elements[0]){
                case "post":
                    if(elements.length == 3) {
                        BulletinBoard.postWithId(id,elements[1], elements[2]);
                        break;
                    }
                case "reply":
                    if(elements.length == 4) {
                        int parentId = Integer.parseInt(elements[1]);
                        BulletinBoard.replyWithId(id,parentId,elements[2],elements[3]);
                        break;
                    }
                default:
                    System.out.println(this.getClass().getName() + ": invalid input");
            }
        }
    }
}
