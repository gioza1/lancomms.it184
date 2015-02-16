/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*   Status: ok so far, you were able to get the stuff running BUT, you realize you need threads cuz it gets stuck afte performing linearly
*
*
 */
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
 *
 * @author eufrik
 */
public class Call extends BaseFile{

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
    
    private ServerSocket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private final String caller_sendPort;
    private final String caller_rcvPort;
    private final String caller_ad;
    private String stopcall;
    private String username;
    private boolean isInstance=false;
    private ChatWindowUI cw;
    private Call_Log call_log;
    
    public Call(){
        server = null;
        connection = null;
        output = null;
        input = null;
        caller_sendPort = null;
        caller_rcvPort = null;
        caller_ad = null;
        stopcall = null;
        isInstance=false;
        
    }
    
    public Call(String addr, String port, String port2, ChatWindowUI c, String uname){
        this.caller_sendPort = port; //port to send to
        this.caller_rcvPort = port2; //port to listen from
        this.caller_ad = addr; //ip to send to
        this.cw = c; //associate with chatwindowui as message/signal sender
        this.username = uname;
        stopcall = "false";
        isInstance = true;
        call_log = new Call_Log();
    }
    
    public void setScreen(){
        factory = new MediaPlayerFactory();
        localMediaPlayer = factory.newEmbeddedMediaPlayer();
        mediaPlayer = factory.newEmbeddedMediaPlayer();
        
        
        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout());
        
        videoPanel = new JPanel();
        videoPanel.setLayout(new GridLayout(1, 2, 8, 0));

        //local canvas
        localCanvas = new Canvas();
        localCanvas.setBackground(Color.black);
        localCanvas.setSize(300, 300);
                    
        localVideoSurface = factory.newVideoSurface(localCanvas);
        localMediaPlayer.setVideoSurface(localVideoSurface);      
        
        localPanel = new JPanel();
        localPanel.setBorder(new TitledBorder("My camera"));
        //localPanel.setLayout(new BorderLayout(0, 8));
        localPanel.setSize(400, 400);
        localPanel.add(localCanvas, BorderLayout.CENTER);
        
        
        //remote canvas
        canvas = new Canvas();
        canvas.setBackground(Color.black);
        canvas.setSize(300, 300);
        
        videoSurface = factory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        
        remotePanel = new JPanel();
        remotePanel.setBorder(new TitledBorder("Incoming"));
        //remotePanel.setLayout(new BorderLayout(0, 8));
        remotePanel.setSize(400, 400);
        remotePanel.add(canvas, BorderLayout.CENTER); 
        
        videoPanel.add(localPanel);
        videoPanel.add(remotePanel);    
        
        contentPane.add(videoPanel, BorderLayout.CENTER);
        
        cframe = new JFrame("Calling: "+username);
        cframe.setContentPane(contentPane);
        cframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cframe.setLocation(50, 50);
        cframe.setSize(700, 400);
        cframe.setResizable(false);
        javax.swing.JButton stop; stop = new javax.swing.JButton();
        stop.setText("Stop Call");
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
    
    public void startCall() {
        setScreen();
        localMediaPlayer.playMedia("dshow://", ":live-caching=100",":sout=#transcode{vcodec=MJPG,vb=56,fps=15,scale=0.5,width=96,height=72,acodec=mp3,ab=24,channels=1,samplerate=8000}:duplicate{dst=rtp{dst="+caller_ad+",port="+caller_sendPort+",mux=ts},dst=display}"," :sout-keep");                
        mediaPlayer.playMedia("rtp://@:"+caller_rcvPort+"", " :network-caching=200");    
        call_log.callStartTime(cw.getId(), cw.getToId());
    }
    
    public void stopCall() {
        try{
        localMediaPlayer.release();
        mediaPlayer.release();
        }catch(Exception e){
            System.out.println("Something wrong terminating the call.");
        }
        cw.enableCall();
        closeWindow();      
        System.out.println("Streaming stopped! Call stopped!");
        cw.append("\nCall Ended.\n");
        call_log.callEndTime(cw.getId());
    }

    public void sendStop(){
        ChatMessage cmsg = new ChatMessage(ChatMessage.STOPCALL, "stopcall");
        cw.sendMessage(cmsg);
    }
    
    private void closeWindow() {
        if(cframe.isDisplayable())
            {   
            cframe.dispose();
            cframe.setVisible(false);
            }        
    }

    public JFrame getFrame() {
        return cframe;
    }
    
    public boolean isInstanced(){
        return isInstance;
    }
               
    //get user response; accept or reject call
    public String acceptOrReject(String username) throws IOException {
        String[] buttons = {"Accept", "Reject"};
        JOptionPane option = new JOptionPane("You have incoming call from "+username+".", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[1]);
        JDialog dialog = option.createDialog("Incoming call.");

        
        String reply; //onclick
        String accept = "Reject";
        

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
    

}
