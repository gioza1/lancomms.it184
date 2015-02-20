///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package lancomms;

import LCViews.LoginUI;
import LCViews.MainUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LANCOMMS {

    private String host;

    public LANCOMMS() {
        
        try {
            getPropHost();
        } catch (IOException ex) {
            System.out.println("meeehh: " + ex);
        }
    }

    public void getPropValues() throws IOException {

        Properties prop = new Properties();
        String propFileName = "host.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("../config/" + propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        // get the property value and print it out
        host = prop.getProperty("host");
    }

    public String getHost() {
        return host;
    }

    public String getPropHost() throws IOException {

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
        System.out.println(host);
        return host;
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
        //MainUI test = new MainUI(1);
        test.setVisible(true);

    }
}
