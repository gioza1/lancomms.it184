/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCModels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author eufrik
 */
public class UserModel {
    
    public boolean checkPassword(int userId, String oldpw){
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();    
        ResultSet rs=null;
        Statement stmt=null; 
        try{
        
            stmt = con.createStatement();
                
            String sql = "SELECT * FROM `user` WHERE user_id = '" + userId + "';";
            rs = stmt.executeQuery(sql);
            rs.next();
            String userPw = rs.getString("user_password");
            if(oldpw.equals(userPw))
                {
                return true;            
                }
            else{return false;}
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot connect at this time.");
            System.out.println("An error has occurred: "+e.getMessage());                       
        }
        finally{
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};            
        }
        return true;
    }
    
    public void updatePassword(int userId, String newpw){
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();    
        ResultSet rs=null;
        Statement stmt=null; 
        try{        
            stmt = con.createStatement();
            String sql = "UPDATE `user` SET user_password='"+newpw+"' WHERE user_id='"+userId+"';";
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally{
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};              
        } 
    }
}
