/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCModels;

import LCControllers.Session;
import LCViews.ChatWindowUI;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Gio
 */
public class MessageListener extends Thread {

    ServerSocket server;
    int port;
    WritableGUI gui;

    public MessageListener() {
    }

    public MessageListener(Session s) {

        int test = 0;
        try {
            ServerSocket ss1 = new ServerSocket(0);
            test = ss1.getLocalPort();
            ss1.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        s.setPort(test);
        try {
            server = new ServerSocket(test);
        } catch (IOException ex) {
            System.out.println("ERROR KO!");
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setGui(WritableGUI g) {
        gui = g;
    }

    public void setPort(int port) {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void run() {
////        super.run(); //To change body of generated methods, choose Tools | Templates.
//        Socket clientSocket;
//        try {
//            while ((clientSocket = server.accept()) != null) {
//               InputStream is = clientSocket.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String line = br.readLine();
//                if (line != null) {
//                    gui.write(line);
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    public void run() {
//        /* Keep Thread running */
////        super.run(); //To change body of generated methods, choose Tools | Templates.
//        Socket clientSocket;
//        try {
//            while ((clientSocket = server.accept()) != null) {
//                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
//                JSONObject clientMessage = new JSONObject(in.readUTF());
//                String userName = clientMessage.get("Username").toString();
//                String message = clientMessage.getString("Message").toString();
////                InputStream is = clientSocket.getInputStream();
//
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String line = br.readLine();
//                if (line != null) {
//                    gui.write(line);
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//}
    public void run() {
        /* Keep Thread running */
        while (true) {
            try {
                /* Accept connection on server */
                Socket serverSocket = server.accept();
                /* DataInputStream to get message sent by client program */
                DataInputStream in = new DataInputStream(serverSocket.getInputStream());
                /* We are receiving message in JSON format from client. Parse String to JSONObject */
                JSONObject clientMessage = new JSONObject(in.readUTF());

                /* Flag to check chat window is opened for user that sent message */
                boolean flagChatWindowOpened = false;
                /* Reading Message and Username from JSONObject */
                String userName = clientMessage.get("Username").toString();
                String message = clientMessage.getString("Message").toString();

                /* Get list of Frame/Windows opened by mainFrame.java */
                for (Frame frame : Frame.getFrames()) {
                    /* Check Frame/Window is opened for user */
                    if (frame.getTitle().equals(userName)) {
                        /* Frame/ Window is already opened */
                        flagChatWindowOpened = true;
                        /* Get instance of ChatWindow */
                        ChatWindowUI chatWindow = (ChatWindowUI) frame;
                        /* Get previous messages from TextArea */
                        String previousMessage = chatWindow.getMessageArea().getText();
                        /* Set message to TextArea with new message */
                        chatWindow.getMessageArea().setText(previousMessage + "\n" +userName+": "+ message);
                    }
                }

                /* ChatWindow is not open for user sent message to server */
                if (!flagChatWindowOpened) {
                    /* Create an Object of ChatWindow */
                    ChatWindowUI chatWindow = new ChatWindowUI();
                    /**
                     * We are setting title of window to identify user for next
                     * message we gonna receive You can set hidden value in
                     * ChatWindow.java file.
                     */
                    chatWindow.setTitle(userName);
                    /* Set message to TextArea */
                    chatWindow.getMessageArea().setText(message);
                    /* Make ChatWindow visible */
                    chatWindow.setVisible(true);
                }

                /* Get DataOutputStream of client to repond */
                DataOutputStream out = new DataOutputStream(serverSocket.getOutputStream());
                /* Send response message to client */
                out.writeUTF("Received from " + clientMessage.get("Username").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

//    private class mainServer extends Thread {
//
//        /* Create ServerSocket variable */
//        private ServerSocket serverSocket;
//
//        /* Constructor to initialize serverSocket */
//        public mainServer() throws IOException {
//            serverSocket = new ServerSocket(6666);
//        }
//
//        /* Implement run() for Thread */
//     
//    }
//
//   public void run() {
//            /* Keep Thread running */
//            while (true) {
//                try {
//                    /* Accept connection on server */
//                    Socket clientSocket = server.accept();
//                    /* DataInputStream to get message sent by client program */
//                      InputStream is = clientSocket.getInputStream();
//                    /* We are receiving message in JSON format from client. Parse String to JSONObject */
//                    JSONObject clientMessage = new JSONObject(in.readUTF());
//                    
//                                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String line = br.readLine();
//                    
//                    /* Flag to check chat window is opened for user that sent message */
//                    boolean flagChatWindowOpened = false;
//                    /* Reading Message and Username from JSONObject */
//                    String userName = clientMessage.get("Username").toString();
//                    String message = clientMessage.getString("Message").toString();
//                    
//                    /* Get list of Frame/Windows opened by mainFrame.java */
//                    for(Frame frame : Frame.getFrames()){
//                        /* Check Frame/Window is opened for user */
//                        if(frame.getTitle().equals(userName)){
//                            /* Frame/ Window is already opened */
//                            flagChatWindowOpened = true;
//                            /* Get instance of ChatWindow */
//                            ChatWindowUI chatWindow = (ChatWindowUI) frame;
//                            /* Get previous messages from TextArea */
//                            String previousMessage = chatWindow.getjTextArea1().getText();
//                            /* Set message to TextArea with new message */
//                            chatWindow.getMessageArea().setText(previousMessage+"\n"+message);
//                        }
//                    }
//                    
//                    /* ChatWindow is not open for user sent message to server */
//                    if(!flagChatWindowOpened){
//                        /* Create an Object of ChatWindow */
//                        ChatWindowUI chatWindow = new ChatWindowUI();
//                        /**
//                         * We are setting title of window to identify user for next message we gonna receive
//                         * You can set hidden value in ChatWindow.java file.
//                         */
//                        chatWindow.setTitle(userName);
//                        /* Set message to TextArea */
//                        chatWindow.getMessageArea().setText(message);
//                        /* Make ChatWindow visible */
//                        chatWindow.setVisible(true);
//                    }
//                    
//                    /* Get DataOutputStream of client to repond */
//                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
//                    /* Send response message to client */
//                    out.writeUTF("Received from "+clientMessage.get("Username").toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }

