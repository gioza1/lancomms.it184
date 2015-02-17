///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package lancomms;

import LCViews.LoginUI;
import LCViews.MainUI;
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
    
    public LANCOMMS(){
        try {
            getPropValues();
        } catch (IOException ex) {
            System.out.println("meeehh: "+ex);
        }
    }
    
    public void getPropValues() throws IOException {
        
        
        Properties prop = new Properties();
        String propFileName = "host.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("lancomms/"+propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        
        // get the property value and print it out
        host = prop.getProperty("host");
        System.out.println(host);
    }
    
    public String getHost(){
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
