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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author eufrik
 */
public class Message {
    
    public void messageTime(int fromuserid, String message, int touserid){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();    
        Statement stmt=null; 
        try{        
            con = callConnector.connectToDB();            
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
    
    public ArrayList<String> getMessages(int userFrom, int userTo){
        ArrayList<String> messages = null;
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();           
        ResultSet rs=null;
        Statement stmt=null; 
        Date tstamp = null;
        String time = null;
        String message=null;
        String name=null;
        String item=null;
        UserModel um = new UserModel();
        try{        
            
            con = callConnector.connectToDB(); 
            stmt = con.createStatement();
            String sql = "SELECT * FROM `message` WHERE (user_id_from="+userFrom+" AND user_id_to="+userTo+") OR (user_id_from="+userTo+" AND user_id_to="+userFrom+") ORDER BY message_timestamp ASC LIMIT 300;";
            rs = stmt.executeQuery(sql);
            messages = new ArrayList<String>();
            while(rs.next()){
                tstamp = rs.getTimestamp("message_timestamp");
                time = new SimpleDateFormat("MM/dd/yyyy, h:mm a").format(tstamp);
                message = rs.getString("message_text");
                name=um.getName(rs.getInt("user_id_from"));
                item="("+time+") "+name+": "+message+"\n";
                messages.add(item);
                }
        }
        catch(SQLException e){
            System.out.println("Get messages: "+e.getMessage());
        }
        finally{
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};   
            return messages;
        }         
    }  
    
    public String getLatest(int userFrom, int userTo){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();           
        ResultSet rs=null;
        Statement stmt=null; 
        Date tstamp = null;
        String time = null;
        String name=null;
        String item=null;
        UserModel um = new UserModel();
        try{        
            con = callConnector.connectToDB(); 
            stmt = con.createStatement();
            String sql = "SELECT * FROM `message` WHERE (user_id_from="+userFrom+" AND user_id_to="+userTo+") OR (user_id_from="+userTo+" AND user_id_to="+userFrom+") ORDER BY message_timestamp DESC LIMIT 1;";
            rs = stmt.executeQuery(sql);
            rs.next();
                tstamp = rs.getTimestamp("message_timestamp");
                time = new SimpleDateFormat("MM/dd/yyyy, h:mm a").format(tstamp);
                name=um.getName(userTo);
                item= name+" ("+time+")" ;            
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally{
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};   
            return item;
        }         
    }    
     
    
}

