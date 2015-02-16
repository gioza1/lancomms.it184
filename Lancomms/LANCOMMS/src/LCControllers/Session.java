/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

/**
 *
 * @author eufrik
 */
public class Session {
    int currentId;
    int currentPort;
    
    public Session(int uid)
    {
        currentId = uid;
    }
    
    public int getId(){
        return currentId;
    }
    
    public void setPort(int p)
    {
        currentPort = p;
    }
    
    public int getPort()
    {
        return currentPort;
    }
   
}
