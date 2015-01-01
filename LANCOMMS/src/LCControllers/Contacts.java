/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import javax.swing.*;
import java.sql.*;
import LCModels.ConnectDB;
/**
 *
 * @author user
 */
public class Contacts
{
    
    public void displayContacts(int userID){
    DefaultListModel clist = new DefaultListModel();
    Connection con = null;
    ConnectDB callConnector = new ConnectDB();
    con = callConnector.connectToDB(); 
    ResultSet rs = null;
    Statement stmt = null;              
        try{
            stmt = con.createStatement();                
            String sql = "SELECT * FROM `user` WHERE user_id != '" + userID + "';";
            rs = stmt.executeQuery(sql);
            
            while(rs.next()){
            String ID = rs.getString("user_id");
            String fname = rs.getString("user_fname");
            String lname = rs.getString("user_lname");
 
                
                
            clist.addElement(new Object[]{ID, fname, lname});
            } 
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally{
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};                           
        }
    }
    
}
