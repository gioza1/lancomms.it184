/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCModels;

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
    
    public ConnectDB(){
        LANCOMMS lc = new LANCOMMS();
        host = lc.getHost();

    }
    
    
    public Connection connectToDB() throws SQLException{
        //connects to DB
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://"+host+":3306/lancomms","lancomms","lancomms");           
        }
        catch ( ClassNotFoundException | SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
        System.out.println("Opened database successfully: ConnectDB()");
        return con;
    }
        
}

