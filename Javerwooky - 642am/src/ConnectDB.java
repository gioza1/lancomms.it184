import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gio
 */
public class ConnectDB {

    public Connection connectToDB() {
        //connects to DB
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.1.4:3306/lancomms", "lancomms", "lancomms");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Cannot connect to server at this time.");
            System.err.println(e.getClass().getName() + ": " + e.getMessage());

        }
        System.out.println("Opened database successfully");
        return con;
    }
}
