///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package lancomms;

import LCViews.LoginUI;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LANCOMMS {

    private String host;
    private String port;
    private String db;
    private String dbUsername;
    private String dbPassword;

    public LANCOMMS() {
        
        try {
            getPropHost();
        } catch (IOException ex) {
            System.out.println("meeehh: " + ex);
        }
    }

    public String getHost() {
        return host;
    }
    
    public String getPort(){
        return port;
    }

    public String getDB(){
        return db;
    }
    
    public String getDbUsername(){
        return dbUsername;
    }
    
    public String getDbPassword(){
        return dbPassword;
    }
    
    
    public void getPropHost() throws IOException {

        String versionString = null;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file  
        String path = "./host.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are intrested, the app.version
        host = mainProperties.getProperty("host");
        port = mainProperties.getProperty("port");
        db = mainProperties.getProperty("database");
        dbUsername = mainProperties.getProperty("dbUsername");
        dbPassword = mainProperties.getProperty("dbPassword");
        System.out.println(host+"\n"+db+"\n"+dbUsername+"\n"+dbPassword);
    }

    public static void main(String[] args) throws InterruptedException, UnsupportedLookAndFeelException {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("Something's wrong with the GUI theme selected: " + e.getMessage());
        }

        LANCOMMS lc = new LANCOMMS();
        LoginUI test = new LoginUI(lc.getHost());
        test.setVisible(true);

    }
}
