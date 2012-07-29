/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mctitan.ChatGetter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Czahrien
 */
public class SimpleServer {
    LinkedList<String> msgs;
    Set<String> allowedhosts;
    ServerSocket sock;
    boolean good;
    Thread thr = null;
    public SimpleServer(int port, Set<String> allowedhosts) {
        try {
            msgs = new LinkedList<>();
            sock = new ServerSocket(port);
            good = true;
            this.allowedhosts = allowedhosts;
        } catch (Exception e) {
            e.printStackTrace();
            good = false;
        }
    }
    
    public static class ServerThread implements Runnable {
        Socket sock;
        public ServerThread(Socket s) {
            sock = s;
        }
        @Override
        public void run() {
            try {
                
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if(thr != null) {
            return;
        }
        thr = new Thread(new Runnable() {
            public void run() {
                try {
                    while(good && !sock.isClosed()) {
                        Socket s = sock.accept();
                        if(allowedhosts.contains(s.getInetAddress().getHostAddress())) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            try {
                                String name = br.readLine();
                                String msg = br.readLine();
                                synchronized(msgs) {
                                    msgs.addLast("[" + name + "]" + " " + msg);
                                }
                                System.out.println("[" + name + "]" + " " + msg);
                                br.close();
                                s.close();
                            } catch (IOException e) {
                                System.out.println("[STRANGE] Had an issue recieving a chat message.");
                            }
                        } else {
                            s.close();
                        }
                    }
               } catch (Exception e) {}
            }
        });
        thr.start();
    }
    
    public void stop() {
        good = false;
        try {
            sock.close();
        } catch(Exception e) {}
        //thr.stop();
    }
    
    public String getMessage() {
        synchronized(msgs) {
            return msgs.size() > 0 ? msgs.removeFirst() : null;
        }
    }
}
