///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package lancomms;

import LCViews.LoginUI;
import LCViews.MainUI;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LANCOMMS {

    public static void main(String[] args) throws InterruptedException, UnsupportedLookAndFeelException {
        try {
            // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("Something's wrong with the GUI theme selected: "+e.getMessage());
        }
        
        LoginUI test = new LoginUI();
        //MainUI test = new MainUI(1);
        test.setVisible(true);
        
    }
}
