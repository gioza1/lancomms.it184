/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import LCModels.Message;
import LCModels.UserModel;
import LCViews.ConversationUI;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 *
 * @author eufrik
 */
public class ConvObject {
    
    private int userId;
    private int userIdTo;
    private String fullnameFrom;
    private String fullnameTo;
    private String latest;
    private Timestamp tstamp;
    private ArrayList<String> messages;
    private Message ms;
    private UserModel um;
    private boolean isnone;
    
    public ConvObject(){
        userId=-1;
        latest=null;
        fullnameFrom=null;
        tstamp=null;
        messages=null;
    }
    
    public ConvObject(int userFrom, int userTo){
        ms = new Message();
        um = new UserModel();
        userId=userFrom;
        userIdTo=userTo;
        latest=null;
        tstamp=null;
        messages=null;
        setNames();
        setLatest();
    }   
    
    
    public void setMessages(){
        messages = ms.getMessages(userId, userIdTo);           
    }
    
    private void setNames(){
        fullnameFrom = um.getName(userId);
        fullnameTo = um.getName(userIdTo);
        
    }
    
    public ArrayList<String> getMessages(){
        return messages;
    }
    
    private void setLatest(){
        latest = ms.getLatest(userId, userIdTo);
    }

    public String getLatest(){
        return latest;
    }
    
    public String getNameTo(){
        return fullnameTo;
    }
    
    public boolean isNone(){
        return isnone;
    }
}
