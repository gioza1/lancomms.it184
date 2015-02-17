package LCControllers;

import LCModels.UserModel;
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
    private String fullname;
    private String status;

    public ClientObject(String server, int port, String username, int uid) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.userId = uid;
        status = "Online";
        setName();
    }

    public String getServer() {
        return server;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getName(){
        return fullname;
    }
    
    public int getPort() {
        return port;
    }

    private void setName(){
        UserModel um = new UserModel();
        fullname = um.getName(userId);   
        System.out.println(fullname);
    }
    
    public int getMyPort() {
        return myPort;
    }

    public int getMyId() {
        return userId;
    }    
   

}
