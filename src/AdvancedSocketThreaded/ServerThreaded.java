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
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author btowle
 */
public class ServerThreaded implements Runnable {

    Socket con;
    boolean writer;
    int requestedNumber;
    SharedMemoryObject intCounter;
    public Socket clients[];
    int myId;
    int monster = 0;

    public ServerThreaded(Socket c, boolean w, SharedMemoryObject so, int conId) {
        myId = conId;

        con = c;
        writer = w;
        intCounter = so;
    }

    /*public void setupServer(){
     */
    @Override
    public void run() {
        try {
            if (writer) {
                //Any initialization I need to do can be sent here
                //before the while loop
                //The client just needs to 'read' the correct
                //data.
                ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
                out.writeInt(myId);
                out.flush();
                while (con.isConnected()) {
                    //System.out.println("Are we in the while? as a writer");
                    System.out.println("Is it dirty in servT: " + intCounter.getIsDirty());
           
                    if (intCounter.getIsDirty() == true /*&& intCounter.startGame ==1*/)
                    {// && intCounter.startGame ==1) {
                        System.out.println("dirty in serverthreaded... ");
                                    
                        //if (intCounter.startGame == 1) {
                         //   System.out.println("inside start game.... ");
                                    
                            /*for (int i = 0; i < intCounter.getNumPlayers(); i++) {
                                Socket clientSocket = intCounter.clients[i]; // Assuming 'clients' is an array of Socket objects containing the client connections
                                try {
                                    ObjectOutputStream out1 = new ObjectOutputStream(clientSocket.getOutputStream());
                                    out1.writeObject(intCounter);
                                    out1.flush();
                                } catch (IOException e) {
                                    System.out.println("Error sending shared memory object to client " + i + ": " + e.getMessage());
                                    // Handle the error if necessary
                                }
                            }*/
                       // }
                        System.out.println("Sending " + intCounter);
                        out.reset();
                        out.writeObject(intCounter);
                        out.flush();
                        //We have to wait For is Dirty to be set to False 
                        //So other clients can get the update!
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            //Something really went wrong.
                        }
                        intCounter.setIsDirty(false);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Something really went wrong.
                    }
                }
            } else {
                ObjectInputStream in = new ObjectInputStream(con.getInputStream());
                while (con.isConnected()) {
                    //System.out.println("Are we in the while? as a reader");

                    if (myId == intCounter.getTurn()) {
                        int temp = in.readInt();
                        int guess = temp;
                        if (guess <= 10 && guess >= 1) {
                            intCounter.addNumber(myId, 10 - Math.abs((int) (Math.random() * 10) - guess));
                        }
                        intCounter.nextTurn();
                        //intCounter.turn %= ;
                        //not sure if this check is needed, this logic also seems a little messy
                        if (intCounter.getTurn() == 0) {
                            intCounter.moveMonster((int) (Math.random() * 6) + 1);
                        }
                    }
                    //System.out.println("We recieved an "+temp);
                    //intCounter.addNumber(temp);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        //Something really went wrong.
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR with Data Writer/Reader: " + e.toString());
        }
    }

}
