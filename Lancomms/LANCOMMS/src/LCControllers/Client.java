package LCControllers;

/**
 * @desc This class includes all necessary functions related to 
 * a client, which is a representation of the user in the network, 
 * such as listening for signals and/or messages from either another
 * client or the main server
 * 
 * @author Gio
 */
import LCViews.GroupChatUI;
import LCViews.MainUI;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Client {

    private Clip clip;
    
    // for I/O to another client
    private transient ObjectInputStream sInput;                 // to read from the socket
    private transient ObjectOutputStream sOutput;		// to write on the socket
    private transient Socket socket;

    //Setting up connections to the Main Server
    private transient ObjectInputStream mainSInput;		// to read from the socket
    private transient ObjectOutputStream mainSOutput;		// to write on the socket
    private transient Socket mSocket;

    // if I use a GUI or not
    private MainUI cg;

    // the server, the port and the username of
    private String runningServer, username;
    private int runningPort;

    private GroupChatUI gcui;

    private transient ClientObject me;

    //gets socket of the target user
    public Socket getSocket() {
        return socket;
    }
    
    //gets socket of the Main Server
    public Socket getmSocket() {
        return mSocket;
    }

    public void disconnectMain() {
        try {
            mSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getMainServerSocket() {
        return mSocket;
    }

    public ObjectOutputStream getMainSOutput() {
        return mainSOutput;
    }

    /*
     * Constructor call when used from a GUI
     * in console mode the ClienGUI parameter is null
     */
    public Client(ClientObject test, String serv, int port) {
        runningServer = serv;
        runningPort = port;
        me = test;
    }

    public void setMainUI(MainUI e) {
        cg = e;
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        boolean successFlag = false;
        try {
            socket = new Socket(runningServer, runningPort); // CONNECTS TO HIS/HER OWN SERVER
        } // if it failed not much I can do
        catch (UnknownHostException ex) {
            return false;
        } catch (IOException ec) {
            ec.printStackTrace();
            return false;
        }

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            System.out.println("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        // success we inform the caller that it worked
        return true;
    }

    public boolean connectToMainServer(String host) {
        // try to connect to the server
        try {
            mSocket = new Socket(host, 1500);
        } // if it failed not much I can so
        catch (Exception ec) {
            System.out.println("Error connecting to server:" + ec);
            return false;
        }

        String msg = "Connection accepted with" + mSocket.getInetAddress() + ":" + mSocket.getPort();

        /* Creating both Data Stream */
        try {
            mainSOutput = new ObjectOutputStream(mSocket.getOutputStream());

            mainSInput = new ObjectInputStream(mSocket.getInputStream());
        } catch (IOException eIO) {
            System.out.println("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromMainServer().start();

        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            mainSOutput.writeObject(me);
        } catch (IOException eIO) {
            System.out.println("Exception doing login : " + eIO);
            disconnectMain();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to the server
     */
    void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            System.out.println("Exception writing to server: " + e);
        }
    }

    public void sendMessageToServer(ChatMessage msg) {
        try {
            mainSOutput.reset();
            mainSOutput.writeObject(msg);
//          mainSOutput.flush();
        } catch (IOException e) {
            System.out.println("Exception writing to server: " + e);
        }
    }

    public void updateStatus() {
        ChatMessage cmsg = new ChatMessage(ChatMessage.STATUS, "BUSY");
        try {
            mainSOutput.writeObject(cmsg);
        } catch (IOException e) {
            System.out.println("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    public void disconnect() {

        try {
            if (sInput != null) {
                sInput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) {
                sOutput.close();
            }
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        } // not much else I can do

        if (cg != null) {
//            cg.connectionFailed();
        }
    }


    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode
     */
    class ListenFromServer extends Thread {

        public void run() {
            while (true) {
                try {
                    String msg = (String) sInput.readObject();

                    if (cg == null) {
                        System.out.println(msg);
                        System.out.print("> ");
                    } else {
//                        cg.append(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Server has close the connection: " + e);
                    if (cg != null) {
//                        cg.connectionFailed();
                    }
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
    }

    class ListenFromMainServer extends Thread {

        public void run() {
            while (true) {
                ChatMessage eh = (ChatMessage) Client.receiveObject(mainSInput);
                if (eh != null) {
                    switch (eh.getType()) {
                        case ChatMessage.MESSAGE:
                            System.out.println(eh.getMessage());
                            break;
                        case ChatMessage.UPDATELIST:
                            cg.updateList(eh.getList());
                            break;
                        case ChatMessage.CLIENTBROADCAST:
                            playSound(2);
                            JOptionPane op = new JOptionPane(eh.getMessage(), JOptionPane.INFORMATION_MESSAGE);
                            JDialog dialog = op.createDialog("Broadcast Message");
                            dialog.setAlwaysOnTop(true);
                            dialog.setModal(true);
                            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                            dialog.setVisible(true);
                            break;
                        case ChatMessage.GROUPCHATINVITE:
                            if (cg.isInGroupChat()) {
                                gcui = new GroupChatUI(eh.getList(), Client.this);
                                gcui.setVisible(true);
                                cg.disableGroupChat();
                            }
                            break;
                        case ChatMessage.GROUPCHATMESSAGE:
                            System.out.println("GROUPCHATMESSAGE: " + eh.getMessage());
                            gcui.appendToTextArea(eh.getMessage());
                            break;
                        case ChatMessage.GROUPCHATLEAVE:
                            System.out.println("GROUPCHATLEAVE: " + eh.getMessage());
                            cg.enableGroupChat();
                            gcui.dispose();
                            break;
//                        case ChatMessage.GROUPCHATFAILED:
//                            System.out.println("GROUPCHATFAIL: " + eh.getMessage());
//                            cg.enableGroupChat();
//                            gcui.dispose();
//                            break;
                        case ChatMessage.GROUPCHATUPDATELIST:
                            gcui.appendToTextArea(eh.getMessage());
                            gcui.populadaList(eh.getList());
                            break;
                    }
                } else {
                    break;
                }
            }
            cg.logout();
        }

    }

    public static Object receiveObject(ObjectInputStream stream) {
        Object obj = null;
        try {
            obj = stream.readObject();
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return obj;
    }

    public ClientObject getCo() {
        return me;
    }

    public MainUI getMainUI() {
        return cg;
    }

    public boolean playSound(int whatSound) {
        final java.util.Date date = new java.util.Date();

        File wavfile = null;
        final int CHAT = 0, CALL = 1, BROADCAST = 2;
        switch (whatSound) {
            case CHAT:
                wavfile = new File("rsrc/lancomms_newmsg.wav");
                break;
            case CALL:
                wavfile = new File("rsrc/lancomms_call.wav");
                break;
            case BROADCAST:
                wavfile = new File("rsrc/lancomms_broadcast.wav");
                break;

        }

        AudioInputStream audioInput = null;
        boolean x = false;
        try {
            audioInput = AudioSystem.getAudioInputStream(wavfile);
            x = false;
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        AudioFormat format = audioInput.getFormat();
//      DataLine.Info info = new DataLine.Info(Clip.class, format);
        if (clip != null) {
            clip.flush();
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clip.open(audioInput);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        clip.start();
        if (whatSound == CALL) {
            clip.loop(clip.LOOP_CONTINUOUSLY);
        }
        return x;
    }
}
