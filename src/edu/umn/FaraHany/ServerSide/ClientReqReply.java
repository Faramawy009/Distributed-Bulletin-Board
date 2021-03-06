package edu.umn.FaraHany.ServerSide;

import java.io.*;
import java.net.Socket;

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
                    reply = BulletinBoard.post(elements[1], elements[2]);
                    break;
                }
            case "reply":
                if(elements.length == 4) {
                    int parentId = Integer.parseInt(elements[1]);
                    reply = BulletinBoard.reply(parentId,elements[2],elements[3]);
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
