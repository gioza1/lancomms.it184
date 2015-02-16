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
public class Call {
        
        public void callStartTime(int userId){
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();    
        ResultSet rs=null;
        Statement stmt=null; 
        try{        
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String room = System.getProperty("user.name");
            String sql = "UPDATE `call` SET ul_out_timestamp='"+tstamp+"' WHERE user_id = "+userId+" ORDER BY ul_in_timestamp DESC LIMIT 1;";
            System.out.println("OUT: "+tstamp);
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
        Connection con;
        ConnectDB callConnector = new ConnectDB();
        con = callConnector.connectToDB();    
        ResultSet rs=null;
        Statement stmt=null; 
        try{        
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String room = System.getProperty("user.name");
            String sql = "UPDATE `call` SET ul_out_timestamp='"+tstamp+"' WHERE user_id = "+userId+" ORDER BY ul_in_timestamp DESC LIMIT 1;";
            System.out.println("OUT: "+tstamp);
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
