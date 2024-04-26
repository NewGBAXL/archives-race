/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdvancedSocketThreaded;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Phase 2: The Shared Game Object
 * 
 * The standard shared object will have an integer “position” variable for up to 3 clients.
 * - You can use an array indexed by the clients ID for this.
 * - Players will start at position 10.
 */

public class SharedMemoryObject implements java.io.Serializable
{
    ReentrantLock myLock;    
    private int[] playerPositions; // Variable to store the positions of up to 3 clients
    //private int numPlayers;
    
    private int monsterPosition; // Variable to store the position of the monster
    
    private int turn; // Variable to indicate whose turn it is
    
    private boolean isDirty = false;
    
    public SharedMemoryObject(int numPlayers)
    {
        myLock = new ReentrantLock();
        playerPositions = new int[(numPlayers==0)?10:numPlayers];
        for (int i = 0; i < playerPositions.length; i++) {
            playerPositions[i] = 10; // Initialize player positions to 10
        }
        monsterPosition = 0; // Initialize monster position
        turn = 0; // Initialize turn
        
    }
    
    public void addNumber(int n, int roll)
    {
        if(n <10)
        {   
            System.out.println("BEFORE adding "+n+": "+playerPositions[n]);
            myLock.lock();
            playerPositions[n] += roll;
            myLock.unlock();
            System.out.println("After adding "+n+": "+playerPositions[n]);
            isDirty = true;
        }
    }
    
    /**
     * Moves a player by the specified number of steps.
     * @param playerID The ID of the player to move
     * @param steps The number of steps to move the player
     */
    public void movePlayer(int playerID, int steps)
    {
        if(playerID >= 0 && playerID < playerPositions.length)
        {
            myLock.lock();
            playerPositions[playerID] += steps;
            myLock.unlock();
            isDirty = true;
        }
    }
    
    /**
     * Moves the monster by the specified number of steps.
     * @param steps The number of steps to move the monster
     */
    public void moveMonster(int steps)
    {
        myLock.lock();
        monsterPosition += steps;
        myLock.unlock();
        isDirty = true;
    }
    
    /**
     * Advances to the next player's turn.
     */
    public void nextTurn()
    {
        myLock.lock();
        turn = (turn + 1) % playerPositions.length;
        myLock.unlock();
    }
    
    //return the turn
    public int getTurn()
    {
        return turn;
    }
    
    /**
     * Gets the state of the shared object.
     * @return True if the shared object is dirty, false otherwise
     */
    public boolean getIsDirty()
    {
        return isDirty;
    }
    
    /**
     * Sets the state of the shared object.
     * @param d The state of the shared object (dirty or not)
     */
    public void setIsDirty(boolean d)
    {
        isDirty = d;
    }
    
    /**
     * Generates a string representation of the race state, with each player on a different row.
     * @return The string representation of the race state
     */
    @Override
    public String toString()
    {
        myLock.lock();
        StringBuilder raceState = new StringBuilder();
        for (int i = 0; i < playerPositions.length; i++) {
            raceState.append(generateRow(i)).append("\n");
        }
        myLock.unlock();
        return raceState.toString();
    }
    
    /**
     * Generates a row representing the race state for a player.
     * @param playerID The ID of the player
     * @return The row representing the race state for the player
     */
    private String generateRow(int playerID)
    {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < monsterPosition; i++) {
            row.append("_");
        }
        row.append("M");
        for (int i = monsterPosition + 1; i < playerPositions[playerID]; i++) {
            row.append("_");
        }
        for (int i = 0; i < playerID; i++) {
            row.append("_");
        }
        row.append(playerID + 1);
        return row.toString();
    }
}
