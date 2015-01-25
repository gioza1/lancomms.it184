/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import LCModels.MessageListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eufrik
 */
public class Session {
    int currentId;
    int currentPort;
    public MessageListener ml;
    
    public Session(int uid)
    {
        currentId = uid;
//        try {
//            currentPort = new ServerSocket(0).getLocalPort(); 
//        } catch (IOException ex) {
//            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
//      
//        }
        ml = new MessageListener(this);
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
    
    public MessageListener getMsgListener()
    {
        return ml;
    }
}
