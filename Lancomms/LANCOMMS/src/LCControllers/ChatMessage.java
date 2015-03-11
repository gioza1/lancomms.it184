package LCControllers;

/**
 *
 * @author Gio
 */
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

    
    public static final int CLIENTBROADCAST = 0, MESSAGE = 1, LOGOUT = 2, CALL = 3, RESPONSE = 4, STOPCALL = 5, STATUS = 6, UPDATELIST = 7, SERVERBROADCAST = 8, GROUPCHATINVITE = 9, GROUPCHATMESSAGE = 10, GROUPCHATLEAVE = 11, GROUPCHATUPDATELIST = 12;
    /** The above variables represent different signals sent by a user:
     *  CLIENBROADCAST is a broadcast message
     *  MESSSAGE is a chat message
     *  LOGOUT is a signal that the user has logged out and will be used by main server to update everyone that the user has logged out
     *  CALL is a signal that alerts the target user that he/she is being called by another user
     *  RESPONSE is the signal of the target user's response to the call (accept or reject)
     *  STOPCALL is a signal to the target user that the user has stopped the call 
     *  STATUS tells main server that the current user's status has been changed
     *  UPDATELIST updates the online list of each client
     *  GROUPCHATMESSAGE for group chat messages
     *  GROUPCHATLEAVE tells server that a user has left the group chat
     *  GROUPCHATDATELIST
     */
    
    
    private int type;
    private String message;
    private ArrayList<ClientObject> clients;
    private ClientObject client;

    //The instance is used for chat messages
    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    //The instace is used for starting group chat by holding a list of possible clients
    public ChatMessage(int ty, ArrayList<ClientObject> cos) {
        type = ty;
        clients = cos;
    }

    //The instance is used for connecting to another client directly
    public ChatMessage(int ty, ClientObject co) {
        type = ty;
        client = co;
    }

    //The instance is used to send group message in group chat or broadcast
    public ChatMessage(int ty, ArrayList<ClientObject> cos, String text) {
        type = ty;
        clients = cos;
        message = text;
    }

    //Gets type of the ChatMessage object
    public int getType() {
        return type;
    }

    //Gets message content of the ChatMessage object
    public String getMessage() {
        return message;
    }

    //Gets list of clients from the given/set array
    public ArrayList<ClientObject> getList() {
        return clients;
    }

    //Get ClientObject of client being connected to
    public ClientObject getClientObject() {
        return client;
    }
}
