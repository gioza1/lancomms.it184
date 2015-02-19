/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCViews;

import LCControllers.Settings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 *
 * @author user
 */
public class SettingsUI extends JFrame{
    
    private javax.swing.JButton OkButton;
    private javax.swing.JButton CancelButton;
    private javax.swing.JLabel changepass;
    private javax.swing.JLabel oldpasswordLabel;
    private javax.swing.JLabel newpasswordLabel;
    private javax.swing.JPasswordField oldpasswordField;
    private javax.swing.JPasswordField newpasswordField;
    private javax.swing.JPasswordField newpasswordField2;
    private MainUI mui;
    private int uid;
    
    public SettingsUI(int userid, MainUI mainui){
        
        uid = userid;
        this.setSize(320,160);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Settings");JPanel panel = new JPanel(); 
        this.mui = mainui;
        mainui.setSettingsOpened();
        
            JLabel usernameLabel = new JLabel("       Current Password: ");
            panel.add(usernameLabel);
            oldpasswordField = new JPasswordField("", 20);
            oldpasswordField.setToolTipText("Input current password");
            panel.add(oldpasswordField);
            
            JLabel passwordLabel = new JLabel("             New Password: ");
            panel.add(passwordLabel);
            newpasswordField = new JPasswordField("", 20);
            newpasswordField.setToolTipText(" Input new password");
            panel.add(newpasswordField);
            
            JLabel passwordLabel2 = new JLabel("Confirm New Password: ");
            panel.add(passwordLabel2);
            newpasswordField2 = new JPasswordField("", 20);
            newpasswordField2.setToolTipText("Confirm new password");
            panel.add(newpasswordField2);            
            
            JButton SaveButton = new JButton("Save");
            panel.add(SaveButton);
            SaveButton.addActionListener(new ActionListener() {
                
                public void actionPerformed(ActionEvent e)
                {
                    HandlingSaveButton();
                }
            });  
            
            JButton BackButton = new JButton("Cancel");
            panel.add(BackButton);
            BackButton.addActionListener(new ActionListener() {    
                public void actionPerformed(ActionEvent e)
                {
                    mainui.setSettingsClosed();
                    setVisible(false);
                }
            });
            
        this.add(panel);
        this.setVisible(true);      
        this.setLocationByPlatform(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainui.setSettingsClosed();
                setVisible(false);
            }
        });        
    }
	 /**
          * sending the updated data to the Setting Controller
          */
	
        public void HandlingSaveButton() {
            Settings set = new Settings();
            boolean check = set.updatePassword(uid, oldpasswordField.getText(), newpasswordField.getText(), newpasswordField2.getText());
            if (check!=false){
                JOptionPane.showMessageDialog(null, "Password has been changed.");
            }
            else
                {  JOptionPane.showMessageDialog(null, "One of the fields is incorrect. Please try again.");}
            oldpasswordField.setText(""); 
            newpasswordField.setText("");
            newpasswordField2.setText("");
            
	}
        /**
         * closes window
         */
        void HandlingBackButton(){
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            //this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            this.dispose();
        }


}