/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdvancedSocketThreaded;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author btowle
 */
public class ThreadedClient implements Runnable {

    public Socket con;
    public Boolean writer;
    Scanner jin;
    SharedMemoryObject intCounter;
    //I can make this static as there will only be two threads on the client.
    static int myId;

    public ThreadedClient(Socket c, Boolean w) {
        con = c;
        writer = w;
        jin = new Scanner(System.in);
    }

    @Override
    public void run() {
        System.out.println("Inside Run - Thread started");
        try {
            
            //ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
            //ObjectInputStream in = new ObjectInputStream(con.getInputStream());
            System.out.println("in try...: ");

            if (writer) {
                ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
                while (con.isConnected()) {
                    if (intCounter != null /*&& new ObjectOutputStream(con.getOutputStream())*/) {
                        System.out.println("Enter a guess between 1 and 10: ");
                        int requestedNumber = 0;
                        do {
                            requestedNumber = jin.nextInt();
                        } while (requestedNumber > 10 || requestedNumber < 1);
                        out.writeInt(jin.nextInt());
                        out.flush();
                    }
                    //Can use Out 
                }
            } else {
                System.out.println("in else...: ");
                ObjectInputStream in = new ObjectInputStream(con.getInputStream());
                myId = in.readInt();
                System.out.println("My User ID is: " + myId);
                while (con.isConnected()) {
                    System.out.println("Inside Reader");
                    
                    //System.out.println("Received Shared Memory Object: ");
                    //System.out.println(intCounter.toString());
                    
                    try {
                        System.out.println("needto Shared Memory Object... ");
                        intCounter = (SharedMemoryObject) in.readObject();
                        System.out.println(intCounter.toString());
                        //Can use In.-     
                        //System.out.println(intCounter);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ThreadedClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        System.out.println("Inside Sleeping");
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Something really went wrong.
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR with Data Writer/Reader: " + e.toString());
        }
        System.out.println("Done with while connected");
    }
}

