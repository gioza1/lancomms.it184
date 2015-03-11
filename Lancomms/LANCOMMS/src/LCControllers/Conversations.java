/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCControllers;

import LCModels.UserModel;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @desc sets retrieved conversations unto an array list
 * of ConvObject for display in the Main UI's conversation list
 * @author eufrik
 */
public class Conversations {

    private ArrayList<ConvObject> cobjs;
    private ArrayList<Integer> userList;
    private UserModel um;
    private int myId;

    public Conversations(int uid) {
        myId = uid;
        cobjs = new ArrayList<ConvObject>();
        um = new UserModel();
    }

    public void setConvList() {
        try {
            userList = um.getUserList();
        } catch (SQLException ex) {
            System.out.println("conversations thread run failed. something wrong with db query Conversations.setConvList()");
        }
        for (int userid : userList) {
            ConvObject cobj = new ConvObject(myId, userid);
            if (cobj.getLatest() != null) {
                cobjs.add(cobj);
            }
        }
    }

    public ArrayList<ConvObject> getConvList() {
        return cobjs;
    }
}
