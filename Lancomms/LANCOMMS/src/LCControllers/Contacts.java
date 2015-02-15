/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCControllers;

import javax.swing.*;
import java.sql.*;
import LCModels.ConnectDB;
import java.net.ServerSocket;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class Contacts {

    String ID;
    String fname;
    String lname;
    int port;
    int numContacts=0;
    DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    //@SuppressWarnings("unchecked")    
    public DefaultTableModel displayContacts(int userID) {
        model.setRowCount(0);
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Port");
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;              
            try{
                ConnectDB callConnector = new ConnectDB();
                con = callConnector.connectToDB();               
                stmt = con.createStatement();                
                String sql = "SELECT * FROM `user` WHERE user_id != '" + userID + "';";
                rs = stmt.executeQuery(sql);
                
                //System.out.println(userID);
                
            while(rs.next()){               
                String rID = rs.getString("user_id");
                String rfname = rs.getString("user_fname");
                String rlname = rs.getString("user_lname");
                port = new ServerSocket(0).getLocalPort();
                StringBuilder sbuilder = new StringBuilder();
                sbuilder.append(rfname);
                sbuilder.append(" ");
                sbuilder.append(rlname);
                String name = sbuilder.toString();
                model.addRow(new Object[]{rID, name, port});
            }
            model.fireTableDataChanged();
            this.numContacts++;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error has occurred: Cannot connect to database.");
        }
        finally{
            try { if (rs != null) rs.close(); } catch (Exception e) {};
            try { if (stmt != null) stmt.close(); } catch (Exception e) {};
            try { if (con != null) con.close(); } catch (Exception e) {}; 
            return model;
        }
    }
    
    public int getNumContacts(){
        return numContacts;
    }

}
