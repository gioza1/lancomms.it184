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
public class Call_Log {
        
        public void callStartTime(int userIdfrom, int userIdto ){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();         
        Statement stmt=null; 
        try{        
            con = callConnector.connectToDB(); 
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String sql = "INSERT INTO `call` (user_idc,call_start_time,call_to) VALUES ('"+userIdfrom+"','"+tstamp+"','"+userIdto+"');";
            System.out.println("CALL START: "+tstamp);
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
        
        public void callEndTime(int userId){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();   
        Statement stmt=null; 
        try{    
            con = callConnector.connectToDB(); 
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String sql = "UPDATE `call` SET call_end_time='"+tstamp+"' WHERE user_idc = "+userId+" ORDER BY call_start_time DESC LIMIT 1;";
            System.out.println("CALL STOP: "+tstamp);
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
