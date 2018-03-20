package edu.umn.FaraHany.ServerSide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class TcpCommunicate {
    static  String [] concurrentSendRecv (ArrayList<ServerAddress> addresses) {
        int n = addresses.size();
        Thread[] threads = new Thread[n];
        final String [] DBs = new String[n];
        for(int i=0; i<DBs.length; i++)
            DBs[i] = " ";
        for (int i = 0; i< n; i++) {
            threads[i] = new Thread( new SendRecvThread(DBs,i, addresses.get(i).getIp(),
                    addresses.get(i).getServerReadPort()));
            threads[i].start();
        }
        for (int i = 0; i< n; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return DBs;
    }
}