/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCModels;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gio
 */

/*

*/
public class MessageTransmitter extends Thread {
    
    //Message = what to send
    //hostname = who to send
    //port = 
    String message, hostname;
    int port;
    
    //optional
    public MessageTransmitter(){
    }

    public MessageTransmitter(String message, String hostname, int port) {
        this.message = message;
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // Socket connects to the ServerSocket, ServerSocket receives a Socket, what? haha//
            //send data as points
            Socket s = new Socket(hostname,port);
            s.getOutputStream().write(message.getBytes());
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}
