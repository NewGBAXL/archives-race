/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AdvancedSocketThreaded;
import java.lang.Runnable;
import java.util.concurrent.locks.*;
/**
 *
 * @author btowle
 */
public class MyFirstThread implements Runnable
{
    public String msg;
    public int count;
    public static Lock conLock = new ReentrantLock();
    public MyFirstThread(String m, int c)
    {
        msg = m;
        count = c;
    }
    
    @Override
    public void run() 
    {
        for(int i =0; i < count; i ++)
        {
           conLock.lock();
           for(int j =0; j< msg.length(); j ++)
           {
           try{
           Thread.sleep(10);
           
           }
           catch (InterruptedException e)
           {
               System.out.println("Exception: "+e.getMessage());
           }
            System.out.print(msg.charAt(j));
           }
           System.out.println("");
           conLock.unlock();
           //Buisness Logic...take time..

        }
        
    }
    
}
