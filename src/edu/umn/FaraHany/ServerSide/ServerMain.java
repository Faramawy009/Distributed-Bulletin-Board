package edu.umn.FaraHany.ServerSide;

public class ServerMain {
    public static void main(String args[]) {
        Thread clientHandler = null;
        {
            try {
                clientHandler = new Thread(new ClientReqRecv(12345));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clientHandler.start();
        while(true);
    }
}
