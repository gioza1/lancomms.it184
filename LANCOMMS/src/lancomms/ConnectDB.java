/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lancomms;

import java.sql.*;


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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lancomms","root","1234");           
        }
        catch ( ClassNotFoundException | SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return con;
    }
        
}

