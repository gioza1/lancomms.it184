package LCControllers;

import LCModels.Call_Log;
import LCViews.ChatWindowUI;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * @desc This is the controller that handles 
 * all call functions, including receiving 
 * and starting the streams. The audio and video streams
 * are handled by VLCJ, and are transmitted as a bundle
 * as done by VLC.
 * @author Weej Chavez
 */

public class Call extends BaseFile implements Runnable{

    Thread call;
    private JFrame cframe;
    private JPanel contentPane;
    private JPanel videoPanel;
    private JPanel localPanel;
    private JPanel remotePanel;
    private Canvas canvas;
    private Canvas localCanvas;
    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer localMediaPlayer;
    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface localVideoSurface;
    private CanvasVideoSurface videoSurface;
    private final String caller_sendPort;
    private final String caller_rcvPort;
    private final String caller_ad;
    private String stopcall;
    private String username;
    private boolean isInstance=false;
    private ChatWindowUI cw;
    private Call_Log call_log;
    
    
    //Constructor that sets all fields to NULL. Used for Testing.
    public Call(){
        caller_sendPort = null;
        caller_rcvPort = null;
        caller_ad = null;
        stopcall = null;
        isInstance=false;
        
    }
    
    public Call(String addr, String port, String port2, ChatWindowUI c, String uname){
        this.caller_sendPort = port; //Port Number of target to call to
        this.caller_rcvPort = port2; //Port Number of target to listen from
        this.caller_ad = addr;       //IP Address of target to call to
        this.cw = c;                 //Associate this instance with related ChatWindowUI instance of the same target user. Used to determine if current user is in a call.
        this.username = uname;
        stopcall = "false";          //User has not performed any Call actions.
        isInstance = true;           //Check if current instance exists
        call_log = new Call_Log();   //Create new instance of Call_Log for this current Call and corresponding actions.
    }
    
    /**
    * @desc This function creates the call window. This uses VLCJ's API to set up the renderer or 'Canvas'.
    * 
    */
    public void setScreen(){
        factory = new MediaPlayerFactory();
        localMediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer = factory.newEmbeddedMediaPlayer();
        
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout());
        
        videoPanel = new JPanel();
        videoPanel.setLayout(new GridLayout(1, 2, 8, 0));

        //The Canvas to be created for the user's camera to be displayed
        localCanvas = new Canvas();
        localCanvas.setBackground(Color.black);
        localCanvas.setSize(300, 300);
                    
        localVideoSurface = factory.newVideoSurface(localCanvas);
        localMediaPlayer.setVideoSurface(localVideoSurface);      
        
        localPanel = new JPanel();
        localPanel.setBorder(new TitledBorder("My camera"));
        localPanel.setSize(400, 400);
        localPanel.add(localCanvas, BorderLayout.CENTER);
        
        
        //The Canvas to be created for the target user's camera to be displayed
        canvas = new Canvas();
        canvas.setBackground(Color.black);
        canvas.setSize(300, 300);
        
        videoSurface = factory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        
        remotePanel = new JPanel();
        remotePanel.setBorder(new TitledBorder("Incoming"));
        remotePanel.setSize(400, 400);
        remotePanel.add(canvas, BorderLayout.CENTER); 
        
        videoPanel.add(localPanel);
        videoPanel.add(remotePanel);    
        
        contentPane.add(videoPanel, BorderLayout.CENTER);
        
        cframe = new JFrame("Calling: "+username);
        cframe.setContentPane(contentPane);
        cframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cframe.setTitle("");
        cframe.setLocation(50, 50);
        cframe.setSize(700, 400);
        cframe.setResizable(false);
        javax.swing.JButton stop; stop = new javax.swing.JButton();
        stop.setText("Stop Call");
        stop.setFont(new java.awt.Font("Calibri", 0, 12));
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopCall();
                sendStop();
            }
        });
        cframe.add(stop,BorderLayout.SOUTH);

        cframe.setVisible(true);
      

        cframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopCall();
                sendStop();
            }
        });
    }
    
    /**
    * @desc Starts the stream by giving VLC the output string which includes the settings for the transcoding and the target user's address and port numbers. Default devices will be used.
    * 
    */    
    public void startCall() {
        setScreen();
        localMediaPlayer.playMedia("dshow://", ":live-caching=100",":sout=#transcode{vcodec=MJPG,vb=56,fps=15,scale=0.5,width=96,height=72,acodec=mp3,ab=24,channels=1,samplerate=8000}:duplicate{dst=rtp{dst="+caller_ad+",port="+caller_sendPort+",mux=ts},dst=display}"," :sout-keep");                
        mediaPlayer.playMedia("rtp://@:"+caller_rcvPort+"", " :network-caching=100");    
        call_log.callStartTime(cw.getId(), cw.getToId());
    }
    
    /**
    * @desc Stops the streams and releases the resources associated with them.
    * There is a currently unidentified bug which causes the application to crash when trying to stop a call made without any webcam.
    */    
    public void stopCall() {
        call_log.callEndTime(cw.getId());
        try{
        localMediaPlayer.release();
        mediaPlayer.release();
        }catch(Exception e){
            System.out.println("Something wrong terminating the call.");
        }
        cw.enableCall();
        closeWindow();             
    }

    /**
    * @desc Sends stop call signal to the other user.
    * 
    */  
    public void sendStop(){
        ChatMessage cmsg = new ChatMessage(ChatMessage.STOPCALL, "stopcall");
        cw.sendMessage(cmsg);
        cw.append("\nCall Ended.\n\n");
    }
    
    /**
    * @desc Closes call window.
    * 
    */    
    private void closeWindow() {
        if(cframe.isDisplayable())
            {   
            cframe.dispose();
            cframe.setVisible(false);
            }        
    }
    
    /**
    * @desc Closes call window.
    * 
    */  
    public boolean isInstanced(){
        return isInstance;
    }
               
    /**
    * @desc Displays the call alert and allows user to accept or reject call.
    * 
    */ 
    public String acceptOrReject(String username) throws IOException {
        String[] buttons = {"Accept", "Reject"};
        ImageIcon icon = new ImageIcon(getClass().getResource("/lancomms/call.png"));
        JOptionPane option = new JOptionPane("You have incoming call from "+username+".", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, icon, buttons, buttons[1]);
        JDialog dialog = option.createDialog("Incoming call.");       
        String reply;               //receives user's choice
        String accept = "Reject";   //return value for user's choice. (i know its a waste of memory. :C )
        

        Timer timer = new javax.swing.Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
                option.setValue("Reject");
               
            }
        });       
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);        
        
        reply = option.getValue().toString();
        
        if(reply.equals("Accept")){
            accept="Accept";
        }
        return accept;
    }

    
    /**Test using threads. Apparently, since we are using an API, threads do not make much if any difference
    *at all to video transmission.
    */
    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        startCall();
    }
    

}
