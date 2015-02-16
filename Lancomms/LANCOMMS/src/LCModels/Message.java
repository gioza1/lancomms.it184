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
import java.sql.Timestamp;

/**
 *
 * @author eufrik
 */
public class Message {
    
        public void messageTime(int fromuserid, String message, int touserid){
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();    
        Statement stmt=null; 
        try{        
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String sql = "INSERT INTO `message` (message_timestamp, message_text, user_id_to, user_id_from) VALUES ('"+tstamp+"','"+message+"','"+touserid+"',"+fromuserid+");";
            System.out.println("message log : time: "+tstamp+"| message: "+message+"| from: "+fromuserid+"| to: "+touserid);
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

