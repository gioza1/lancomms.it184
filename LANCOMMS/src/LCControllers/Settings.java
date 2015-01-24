/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import LCModels.ConnectDB;
import LCModels.UserModel;
import javax.swing.JOptionPane;


/**
 *
 * @author eufrik
 */
public class Settings {
   
    public boolean updatePassword(int userId, String oldpw, String newpw){
        // call check old password
        //  UserModel set = new SettingsModel();
        UserModel setpw = new UserModel();
        boolean check = setpw.checkPassword(userId, oldpw);
        if(check!=false){
            setpw.updatePassword(userId, newpw);
            return true;
            }
        else
            return false;
    }    
}
