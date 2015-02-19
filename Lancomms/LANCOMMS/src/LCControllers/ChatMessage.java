package LCControllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gio
 */
import LCModels.Message;
import java.io.*;
import java.util.ArrayList;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */

public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    public static final int CLIENTBROADCAST = 0, MESSAGE = 1, LOGOUT = 2, CALL = 3, RESPONSE = 4, STOPCALL = 5, STATUS = 6, UPDATELIST = 7, SERVERBROADCAST=8;
    private int type;
    private String message;
    private ArrayList<ClientObject> clients;

    // constructor
    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public ChatMessage(int ty, ArrayList<ClientObject> cos) {
        type = ty;
        clients = cos;
    }

    public ChatMessage(int ty, ArrayList<ClientObject> cos, String text) {
        type = ty;
        clients = cos;
        message = text;
    }

    // getters
    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ClientObject> getList() {
        return clients;
    }
}
