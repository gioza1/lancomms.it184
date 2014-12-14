/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lancomms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class Login {
    
    int userId=0;
    /**
     * 
     * @param username the user name what is the user is trying to log in
     * @param password the password
     * @return userID of the user who logs in
     */
    public int login(String username, String password){
        
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();
        
        try{
            ResultSet rs;
            Statement stmt;
            stmt = con.createStatement();
            
            String sql = "SELECT * FROM Account_Details WHERE USERNAME == '" + username + "';";
            rs = stmt.executeQuery(sql);
            userId = rs.getInt("ID");

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return userId;
    }
}