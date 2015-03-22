package LCModels;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import javax.swing.JOptionPane;
import lancomms.LANCOMMS;


/**
 *
 * @author user
 */
public class ConnectDB {
    public Connection con = null; //the Connector Variable
    /**
     * The method where the connection made
     * @return 
     */
    private String host;
    private String port;
    private String db;
    private String dbUsername;
    private String dbPassword;
    
    public ConnectDB(){
        LANCOMMS lc = new LANCOMMS();
        host = lc.getHost();
        port = lc.getPort();
        db = lc.getDB();
        dbUsername = lc.getDbUsername();
        dbPassword = lc.getDbPassword();
    }
    
    
    public Connection connectToDB(){
        //connects to DB
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+db,dbUsername,dbPassword);           
        }
        catch ( ClassNotFoundException | SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
        System.out.println("Opened database successfully: ConnectDB()");
        return con;
    }
        
}

