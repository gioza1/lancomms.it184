/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import LCModels.ConnectDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author user
 */
public class Login {
    
    int userId=-1;
    String userPw = null;
    /**
     * 
     * @param username the user name what is the user is trying to log in
     * @param password the password
     * @return userID of the user who logs in
     */
    public int login(String username, String password){
        
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();    
        ResultSet rs=null;
        Statement stmt=null; 
        try{
            con = callConnector.connectToDB();
            stmt = con.createStatement();                
            String sql = "SELECT * FROM `user` WHERE user_username = '" + username + "' AND user_status = 1;";
            try{
                rs = stmt.executeQuery(sql);
                rs.next();
                userPw = rs.getString("user_password");
                if(password.equals(userPw))
                    {
                    userId = rs.getInt("user_id");              
                    }  
                else{
                    userId = -1;
                }
            }catch(SQLException ev){
                userId = -1;   
            }
        }
        catch(SQLException e){
            userId = -2;
            System.out.println("An error has occurred: "+e.getMessage()+" !!!");
        }
        finally{
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {};     
            return userId; 
        }   
        
    }
    
    public void loginTime(int userId){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();  
        Statement stmt=null; 
        try{
            con = callConnector.connectToDB();         
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String room = System.getProperty("user.name");
            String sql = "INSERT INTO `user_log` (ul_in_timestamp,ul_room,user_id) VALUES ('"+tstamp+"','"+room+"','"+userId+"');";
            System.out.println("IN: "+tstamp);
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
    
    public void logoutTime(int userId){
        Connection con=null;
        ConnectDB callConnector = new ConnectDB();  
        Statement stmt=null; 
        try{
            con = callConnector.connectToDB();         
            stmt = con.createStatement();
            java.util.Date date = new java.util.Date();
            Timestamp tstamp = new Timestamp(date.getTime());
            String sql = "UPDATE `user_log` SET ul_out_timestamp='"+tstamp+"' WHERE user_id = "+userId+" ORDER BY ul_in_timestamp DESC LIMIT 1;";
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
    
//    public ClientObject createClientObj(String uname)
//    {
//          int port = 0;
//        String server = "";
////        umodel = new UserModel();
//        try {
//            server = InetAddress.getLocalHost().getHostAddress().toString();
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        ServerSocket getp;
//        try {
//            ServerSocket getp;
//            getp = new ServerSocket(0);
//            port = getp.getLocalPort();
//            getp.close();
//        } catch (IOException ex) {
//            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        ClientObject myCObj = new ClientObject(server, port,uname, );
//        return myCObj;
//    }    
}