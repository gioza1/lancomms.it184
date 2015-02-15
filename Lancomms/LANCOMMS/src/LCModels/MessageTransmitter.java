/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCModels;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.json.JSONObject;

/**
 *
 * @author Gio
 */

public class MessageTransmitter extends Thread {

    //Message = what to send
    //hostname = who to send
    //port = 
    String message, hostname;
    int targetPort;
    JSONObject transmitJSON;

    //optional
    public MessageTransmitter(String hostname, int port) {
        this.hostname = hostname;
        targetPort = port;
    }

    public MessageTransmitter(String username,String message, String hostname, int port) {

        transmitJSON = new JSONObject();
        transmitJSON.put("Message", message);
        transmitJSON.put("Username", username);
        transmitJSON.put("Hostname", hostname);
        targetPort = port;
        this.hostname = hostname;
    }

    @Override
    public void run() {
        try {
            // Socket connects to the ServerSocket, ServerSocket receives a Socket, what? haha//
            //send data as points
//            Socket s = new Socket(hostname,targetPort);
//            Socket z = new Socket(hostname, 48500, InetAddress.getByName("127.0.0.1"), targetPort);
            Socket client = new Socket(hostname, targetPort);
           /* Get server's OutputStream */
            OutputStream outToServer = client.getOutputStream();
            /* Get server's DataOutputStream to write/send message */
            DataOutputStream out = new DataOutputStream(outToServer);
            /* Write message to DataOutputStream */
            out.writeUTF(transmitJSON.toString());
            /* Get InputStream to get message from server */
            InputStream inFromServer = client.getInputStream();
            /* Get DataInputStream to read message of server */
            DataInputStream in = new DataInputStream(inFromServer);
            /* Print message received from server */
            System.out.println("Server says..." + in.readUTF());
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//     public void sendMessage(String host, String message) {
//        try {
//            /* Create new socket connection with server host using port 6666 (port can be anything) */
//            Socket client = new Socket(host, 6666);
//            /* Get server's OutputStream */
//            OutputStream outToServer = client.getOutputStream();
//            /* Get server's DataOutputStream to write/send message */
//            DataOutputStream out = new DataOutputStream(outToServer);
//            /* Write message to DataOutputStream */
//            out.writeUTF(message);
//            /* Get InputStream to get message from server */
//            InputStream inFromServer = client.getInputStream();
//            /* Get DataInputStream to read message of server */
//            DataInputStream in = new DataInputStream(inFromServer);
//            /* Print message received from server */
//            System.out.println("Server says..." + in.readUTF());
//            /* Close connection of client socket */
//            client.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

    //--------------------------------------------------------
    /*
         @Override
         public void run() {
         try {
         // Socket connects to the ServerSocket, ServerSocket receives a Socket, what? haha//
         //send data as points
         //            Socket s = new Socket(hostname,targetPort);
         //            Socket z = new Socket(hostname, 48500, InetAddress.getByName("127.0.0.1"), targetPort);
         Socket z = new Socket(hostname, targetPort);
         z.getOutputStream().write(message.getBytes());

         z.close();
         } catch (IOException ex) {
         Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, null, ex);

         //        }
         }
    
         */
