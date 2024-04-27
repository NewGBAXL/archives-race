/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdvancedSocketThreaded;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;

/**
 *
 * @author btowle
 */
public class ServerListener {

    SharedMemoryObject intCounter;
    ExecutorService ex;
    public Socket clients[];
    ServerSocket serve;
    //int connectedClients;
    int connectionCount;
    public ServerListener(int MAXPLAYERS) {
        intCounter = new SharedMemoryObject(MAXPLAYERS, 0);

        //connectedClients = 0;
        connectionCount = 0;

        try {
            serve = new ServerSocket(8888);
        } catch (IOException e) {
            System.out.println("ERROR Creating the server: " + e.getMessage());
        }
        ex = Executors.newFixedThreadPool(10);
        //intCounter.clients = new Socket[10];
    }
    
    public void BuisnessLogic(int MAXPLAYERS) {
        //int connectionCount = 0;
        //fix this to check if S pressed and then send the shared memory object to all clients
        while (connectionCount < MAXPLAYERS-1) {
            Socket con;
            try {                
                
                System.out.println("Listening for new clients.");
                con = serve.accept();
                //intCounter.clients[connectionCount] = con;
                ex.execute(new ServerThreaded(con, true, intCounter, connectionCount));
                ex.execute(new ServerThreaded(con, false, intCounter, connectionCount));
                connectionCount++;
                intCounter.setIsDirty(true);
                System.out.println("Client connected!");
            } catch (IOException e) {
                System.out.println("ERROR accepting: " + e.getMessage());
                break;
            }

        }
        System.out.println("connected players" + MAXPLAYERS + " " + connectionCount);
        if (connectionCount == MAXPLAYERS) {
            System.out.println("Setting up game... ");
            
            // Create and populate the shared memory object
            SharedMemoryObject intCounter = new SharedMemoryObject(connectionCount, 1);
            // Set user's turn to 0
            //intCounter.setIsDirty(true); // Set dirty flag to notify clients about the updated shared memory object
            System.out.println("Is it dirty in ServL" + intCounter.getIsDirty());
                                    
            intCounter.startGame = 1;
            intCounter.setIsDirty(true);
           // intCounter.clients[] = clients[];
            
            
            // Send the shared memory object to all connected clients
           /* for (int i = 0; i < connectionCount; i++) {
                Socket clientSocket = clients[i]; // Assuming 'clients' is an array of Socket objects containing the client connections
                try {
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    out.writeObject(intCounter);
                    out.flush();
                } catch (IOException e) {
                    System.out.println("Error sending shared memory object to client " + i + ": " + e.getMessage());
                    // Handle the error if necessary
                }
            }*/

            try {
                serve.close();
                ex.shutdown();
            } catch (IOException e) {
                System.out.println("ERROR Closing: " + e.getMessage());

            }
        }
    }
}
