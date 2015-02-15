/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCModels;

import java.sql.*;
import javax.swing.JOptionPane;


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
    public Connection connectToDB(){
        //connects to DB
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.102:3306/lancomms","lancomms","lancomms");           
        }
        catch ( ClassNotFoundException | SQLException e ) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server at this time.");
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );

        }
        System.out.println("Opened database successfully");
        return con;
    }
        
}

