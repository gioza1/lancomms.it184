package LCControllers;

import LCViews.MainUI;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author WeeJ
 */
public class ClientObject implements Serializable {

//    protected static final long serialVersionUID = 1112122200L;
    private ClientGUI cg;
    private MainUI mg;
    // the server, the port and the username
    private String server, username;
    private int port;
    private int myPort;

    public ClientObject(String server, int port, String username, ClientGUI cg) {
        this.server = server;
        this.port = port;
        this.username = username;
        // save if we are in GUI mode or not
        this.cg = cg;

        //comment this out after running one instance for testing (the try catch portion)
//        try {
//            ServerSocket x;
//            x = new ServerSocket(0);
//            myPort = x.getLocalPort();
//            x.close();
//        } catch (IOException ex) {
//            Logger.getLogger(ClientObject.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

//        public ClientObject(String server, int port, String username, ClientGUI cg) {
//        this.server = server;
//        this.port = port;
//        this.username = username;
//        // save if we are in GUI mode or not
//        this.cg = cg;
//
//    }
    public String getServer() {
        return server;
    }

    public String getUsername() {
        return username;
    }

    public int getPort() {
        return port;
    }

    public int getMyPort() {
        return myPort;
    }

    public ClientGUI getGui() {
        return cg;
    }

}
