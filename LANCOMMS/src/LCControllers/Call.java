/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LCControllers;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
/**
 *
 * @author eufrik
 */
public class Call extends BaseFile{
            
    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas canvas;
    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;
    
    public Call(){
        canvas = new Canvas();
        canvas.setBackground(Color.black);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas, BorderLayout.CENTER);

        frame = new JFrame("Capture");
        //frame.setIconImage(new ImageIcon(getClass().getResource("/icons/vlcj-logo.png")).getImage());
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopCall();
                }
            });    
        frame.setLocation(50, 50);
        frame.setSize(400, 300);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.newEmbeddedMediaPlayer();
        videoSurface = factory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);     
    }
    
    public void startCall(String ipAdd, String port) {
        
        frame.setVisible(true);

        // GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
        //String[] options = {":sout=#rtp{vcodec=mp4v,vb=4096,scale=1,acodec=mpga,ab=128,channels=2,samplerate=44100},dst=display,port=6111}"};
        //String stroptions = formatRtspStream("192.168.1.109", 6111, "TestStream");
        //System.out.println("Streaming '" + mrl + "' to '" + stroptions + "'");
        System.out.println(ipAdd);
        System.out.println(port);
        mediaPlayer.playMedia("dshow://",":live-caching=1200 :dshow-size=128x96 :sout=#transcode{vcodec=h264,acodec=mpga,ab=128,channels=2,samplerate=44100}:duplicate{dst=rtp{dst="+ipAdd+",port="+port+",mux=ts,sap,name=test},dst=display}",":sout-keep");
        //Thread.currentThread().join();
    }
    
    private void stopCall(){
        mediaPlayer.stop();
        System.out.println("Streaming stopped!");
    }
    
    public JFrame getFrame(){
        return frame;
    }
}
