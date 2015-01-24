/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCViews;

import LCControllers.Settings;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author user
 */
public class SettingsUI extends JFrame{
    
    private javax.swing.JButton OkButton;
    private javax.swing.JButton CancelButton;
    private javax.swing.JLabel oldpasswordLabel;
    private javax.swing.JLabel newpasswordLabel;
    private javax.swing.JPasswordField oldpasswordField;
    private javax.swing.JPasswordField newpasswordField;
    public static String oldpassword;
    public static String newpassword;
    private int uid;
    
    public SettingsUI(int userid){
        
        uid = userid;
        this.setSize(300,150);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("Settings");JPanel panel = new JPanel(); 
       
            
            
            JLabel usernameLabel = new JLabel("Current Password: ");
            panel.add(usernameLabel);
            oldpasswordField = new JPasswordField("", 20);
            oldpasswordField.setToolTipText("Input current password here");
            panel.add(oldpasswordField);
            
            JLabel passwordLabel = new JLabel("      New Password: ");
            panel.add(passwordLabel);
            newpasswordField = new JPasswordField("", 20);
            newpasswordField.setToolTipText("Input new password here");
            panel.add(newpasswordField);
            
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
                    HandlingBackButton();
                }
            });
            
        this.add(panel);
        this.setVisible(true);
    }
	 /**
          * sending the updated data to the Setting Controller
          */
	
        public void HandlingSaveButton() {
            Settings set = new Settings();
            boolean check = set.updatePassword(uid, oldpasswordField.getText(), newpasswordField.getText());
            if (check!=false){
                JOptionPane.showMessageDialog(null, "Password has been changed.");
            }
            else
                {  JOptionPane.showMessageDialog(null, "Woops! Something went wrong. Please try again.");}
            oldpasswordField.setText(""); 
            newpasswordField.setText("");
            
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