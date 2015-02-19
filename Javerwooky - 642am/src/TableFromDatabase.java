/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gio
 */
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class TableFromDatabase extends JFrame {

    public TableFromDatabase() {
        Vector<Object> columnNames = new Vector<Object>();
        Vector<Object> data = new Vector<Object>();

        try {
            //  Connect to an Access Database

//            String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
//            String url = "jdbc:odbc:???";  // if using ODBC Data Source name
//            String url =
//                "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=c:/directory/???.mdb";
//            String userid = "";
//            String password = "";
//            Class.forName( driver );
            Connection connection = connectToDB();

            //  Read data from a table
            String sql = "SELECT * FROM `user`";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            //  Get column names
            columnNames.addElement("Username");
            columnNames.addElement("Name");
            columnNames.addElement("Account Status");

            while (rs.next()) {
                Vector<Object> row = new Vector<Object>(columns);

//                for (int i = 1; i <= columns; i++) {
                row.addElement(rs.getString("user_username"));
                row.addElement(rs.getString("user_fname")+" "+rs.getString("user_lname"));
                row.addElement(rs.getString("user_username"));
//                }

                data.addElement(row);
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        //  Create table with database data
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                for (int row = 0; row < getRowCount(); row++) {
                    Object o = getValueAt(row, column);

                    if (o != null) {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane);

        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        TableFromDatabase frame = new TableFromDatabase();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

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
