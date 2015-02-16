package LCControllers;

import LCViews.MainUI;
import java.io.Serializable;

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
    private MainUI mg;
    // the server, the port and the username
    private String server, username;
    private int port;
    private int myPort;
    private int userId;

    public ClientObject(String server, int port, String username, int uid) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.userId = uid;
    }

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

    public int getMyId() {
        return userId;
    }    
   

}
