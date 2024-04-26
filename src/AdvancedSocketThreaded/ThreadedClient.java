package AdvancedSocketThreaded;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadedClient implements Runnable {

    public Socket con;
    public Boolean writer;
    Scanner jin;
    static SharedMemoryObject intCounter;
    //I can make this static as there will only be two threads on the client.
    static int myId;

    public ThreadedClient(Socket c, Boolean w) {
        con = c;
        writer = w;
    }

    @Override
    public void run() {
        System.out.println("Inside Run - Thread started");
        try {
            jin = new Scanner(System.in);
            ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(con.getInputStream());

            if (writer) {
                while (con.isConnected()) {
                    if (intCounter != null && intCounter.getTurn() == myId) {
                        System.out.println("Enter a guess between 1 and 10: ");
                        //int requestedNumber = 0;
                        //    requestedNumber = jin.nextInt();
                        out.writeInt(jin.nextInt());
                        out.flush();
                    }
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch(InterruptedException ex)
                    {
                        //Something really went wrong.
                    }
                }
            } else {
                myId = in.readInt();
                System.out.println("My User ID is: " + myId);
                while (con.isConnected()) {
                    System.out.println("Inside Reader");

                    // Receive the shared memory object from the server
                    intCounter = (SharedMemoryObject) in.readObject();
                    System.out.println("Received Shared Memory Object: ");
                    System.out.println(intCounter.toString());

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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Done with while connected");
    }
}
