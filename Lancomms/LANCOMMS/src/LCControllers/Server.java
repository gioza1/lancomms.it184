package LCControllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gio
 */
import LCControllers.ClientObject;
import LCControllers.ParseRoute.ParseRoute;
import LCViews.ChatWindowUI;
import LCViews.MainUI;
import java.awt.Frame;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server implements Runnable {

    Clip clip = null;
    // a unique ID for each connection
    private static int uniqueId;
    // an ArrayList to keep the list of the Client
    private ArrayList<ClientThread> al;
    // if I am in a GUI
    private MainUI sg;
    // to display time
    private SimpleDateFormat sdf;
    // the port number to listen for connection
    private int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;
    private ServerSocket serverSocket;
    private ClientObject me;

    private ArrayList<ClientObject> alCo = new ArrayList<ClientObject>();

    /*
     *  server constructor that receive the port to listen to for connection as parameter
     *  in console
     */
    public Server(ClientObject test, MainUI mui) {
//		this(port, null, null);
        this.sg = mui;
        this.me = test;
        this.port = test.getPort();
        // to display hh:mm:ss
        sdf = new SimpleDateFormat("HH:mm:ss");
        // ArrayList for the Client list
        al = new ArrayList<ClientThread>();
    }

    public void run() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ParseRoute pr = new ParseRoute();
            InetAddress addr = InetAddress.getByName(pr.getLocalIPAddress());
            serverSocket = new ServerSocket(me.getPort(), 50, addr);

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                Socket socket = serverSocket.accept();  	// accept connection
                System.out.println(socket.toString());
                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }
                ClientThread t = new ClientThread(socket);  // make a thread of it
                al.add(t);									// save it in the ArrayList
                t.start();
                System.out.println("CLIENT THREAD COUNT: " + al.size());
//                System.out.println("Client thread started");
            }
        } catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        } // I was asked to stop
        finally {
            try {
                serverSocket.close();
                System.out.println("Server succsefully stopped");
            } catch (Exception e) {
                System.out.println("Exception closing the server and clients: " + e);
            }
            for (int i = 0; i < al.size(); ++i) {
                System.out.println("Stopping all client threads. CT#: " + i);
                ClientThread tc = al.get(i);
                try {
                    tc.sInput.close();
                    tc.sOutput.close();
                    tc.socket.close();
                } catch (IOException ioE) {
                    // not much I can do
                }
            }
        }
        // something went bad
    }

    /*
     * For the GUI to stop the server
     */
    public void stop() {
//    protected void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement 
        // Socket socket = serverSocket.accept();
        try {
            new Socket(me.getServer(), port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }
    /*
     * Display an event (not a message) to the console or the GUI
     */

    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        if (sg == null) {
            System.out.println(time);
        } else {
//            sg.appendEvent(time + "\n");
        }
    }

    private void addToContactList(ClientObject e) {
//        sg.appendContact(e);
    }
    /*
     *  to broadcast a message to all Clients
     */

    private synchronized void broadcast(String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
        String messageLf = time + " " + message + "\n";
        // display message on console or GUI
        if (sg == null) {
            System.out.print(messageLf);
        } else {
//            sg.appendRoom(messageLf);     // append in the room window
        }
        // we loop in reverse order in case we would have to remove a Client
        // because it has disconnected
        for (int i = al.size(); --i >= 0;) {
            ClientThread ct = al.get(i);

            // try to write to the Client if it fails remove it from the list
            if (!ct.writeMsg(messageLf)) {
                al.remove(i);
                display("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    // for a client who logoff using the LOGOUT message
    synchronized void remove(int id) {
        // scan the array list until we found the Id
        for (int i = 0; i < al.size(); ++i) {
            ClientThread ct = al.get(i);
            // found it
            if (ct.id == id) {
                al.remove(i);
                return;
            }
        }
    }

    /*
     *  To run as a console application just open a console window and: 
     * > java Server
     * > java Server portNumber
     * If the port number is not specified 1500 is used
     */
    public static void main(String[] args) {
        // start server on port 1500 unless a PortNumber is specified 
        int portNumber = 1500;
        switch (args.length) {
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            case 0:
                break;
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;

        }
        // create a server object and start it
//		Server server = new Server(me.get);
//		server.start();
    }

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {

        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        // my unique id (easier for deconnection)
        int id;
        // the Username of the Client
        String username;
        // the only type of message a will receive
        ChatMessage cm;
        // the date I connect
        String date;

        //client object
        ClientObject co; // sender's info

        ChatWindowUI cw;

        // Constructore
        ClientThread(Socket socket) {
            // a unique id
            id = ++uniqueId;
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Thread trying to create Object Input/Output Streams");
            try {
                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());

            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                System.out.println("Exception creating new Input/output Streams: " + e);
                return;
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

//			// have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
//			catch(ClassNotFoundException a){}
            date = new Date().toString() + "\n";
        }

        // what will run forever
        @Override
        public void run() {
            // to loop until LOGOUT           
            Call call = new Call();

            boolean keepGoing = true;
            boolean isCallInstancedHere = false;

            while (keepGoing) {
                if (co == null) {
                    try {
                        co = (ClientObject) sInput.readObject();
                    } catch (IOException e) {
                        display(username + " Exception reading Streams: " + e);
                    } catch (ClassNotFoundException e2) {
                    }

                    /* Flag to check chat window is opened for user that sent message */
                    boolean flagChatWindowOpened = false;
                    /* Reading Message and Username from JSONObject */
                    String userName = co.getFullName();

                    for (Frame frame : Frame.getFrames()) {
                        /* Check Frame/Window is opened for user */
                        if (frame.getTitle().equals(userName)) {
                            /* Frame/ Window is already opened */
                            flagChatWindowOpened = true;
                            /* Get instance of ChatWindow */
                            cw = (ChatWindowUI) frame;
                            /* Get previous messages from TextArea */
//                        String previousMessage = chatWindow.getMessageArea().getText();
                        /* Set message to TextArea with new message */
//                        chatWindow.getMessageArea().setText(previousMessage + "\n" + userName + ": " + message);
                        }
                    }

                    /* ChatWindow is not open for user sent message to server */
                    if (!flagChatWindowOpened) {
                        /* Create an Object of ChatWindow */
                        cw = new ChatWindowUI(co, me);
                        sg.cwSetMainUI(cw);
                        sg.addChatWindow(cw);
                        /**
                         * We are setting title of window to identify user for
                         * next message we gonna receive You can set hidden
                         * value in ChatWindow.java file.
                         */
                        cw.setTitle(userName);
                        /* Set message to TextArea */
                    }

                    alCo.add(co);
                }
                try {
                    cm = (ChatMessage) sInput.readObject();
                } catch (IOException e) {
                    display(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                // the messaage part of the ChatMessage
                String message = cm.getMessage();
                // set up call address-port if not instanced yet
                if (cw.isCallInstanced() == false) {
                    call = new Call(co.getServer(), Integer.toString(co.getPort()), Integer.toString(port), cw, co.getFullName());
                    cw.setCallInstance();
                    isCallInstancedHere = true;
                } else if (cw.isCallInstanced() == true) {
                    if (isCallInstancedHere == false) {
                        call = cw.getCallInstance();
                        isCallInstancedHere = true;
                    }
                }

                // Switch on the type of message receive
                switch (cm.getType()) {

                    case ChatMessage.MESSAGE:

                        cw.append(co.getFullName() + ": " + message + "\n");
                        playSound(0);

                        if (cw.isVisible() == false) {
                            cw.setVisible(true);
                        }
                        break;
                    case ChatMessage.LOGOUT:
                        cw.setSendDisabled();
//                        display(username + " disconnected with a LOGOUT message.");
                        keepGoing = false;
                        break;
//                    case ChatMessage.WHOISIN:
//                        writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
//                        // scan al the users connected
//                        for (int i = 0; i < al.size(); ++i) {
//                            ClientThread ct = al.get(i);
//                            writeMsg((i + 1) + ") " + ct.username + " since " + ct.date);
//                        }
//                        break;
                    case ChatMessage.CALL:
                        try {
                            String cresponse = null;
                            playSound(0);
                            if (!sg.checkCallDisabled()) {
                                cresponse = call.acceptOrReject(co.getFullName());
                                cm = new ChatMessage(ChatMessage.RESPONSE, cresponse);
                                cw.sendResponse(cm);

                                if (cresponse.equals("Accept")) {
                                    if (cw.isVisible() == false) {
                                        cw.setVisible(true);
                                    }
                                    cw.append("\nYou are now in a call with " + co.getFullName() + "\n");
                                    call.startCall();
                                    sg.setCallDisabled();
                                    stopSound();

                                } else if (cresponse.equals("Reject")) {

                                    cw.append("\nYou rejected the call from " + co.getFullName() + "\n");
                                    stopSound();
                                }
                            } else if (sg.checkCallDisabled()) {
                                cresponse = "Busy";
                                cm = new ChatMessage(ChatMessage.RESPONSE, cresponse);
                                cw.sendResponse(cm);
                            }
                            System.out.println("My response is: " + cresponse);

                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        break;
                    case ChatMessage.RESPONSE:
                        switch (message) {
                            case "Accept":
                                call.startCall();
                                sg.setCallDisabled();

                                cw.append("\nYou are now in a call with " + co.getFullName() + "\n");
                                stopSound();

                                break;
                            case "Reject":
                                cw.append("\n" + co.getFullName() + " has rejected the call!\n");
                                sg.setCallEnabled();
                                stopSound();
                                break;
                            case "Busy":
                                cw.append("\n" + co.getFullName() + " is in a call right now. Please try again later!\n");
                                sg.setCallEnabled();
                                stopSound();
                                break;
                        }
                        break;
                    case ChatMessage.STOPCALL:
                        call.stopCall();
                        break;
                }
            }
            // remove myself from the arrayList containing the list of the
            // connected Clients

            //cw.dispose();
            cw.append(co.getFullName() + " is now offline.\n");
            try {
                call.stopCall();
            } catch (Exception e) {
                //
            }
            remove(id);
            co = null;
            alCo.remove(co);

            disconnect();
        }

        // try to close everything
        public void disconnect() {
            // try to close the connection
            try {
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                System.out.println("Oh noes.");
            }
        }

        /*
         * Write a String to the Client output stream
         */
        private boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                disconnect();
                return false;
            }
            // write the message to the stream
            try {
                sOutput.writeObject(msg);
            } // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }

        public boolean playSound(int whatSound) {
            final java.util.Date date = new java.util.Date();

            File wavfile = null;
            final int CHAT = 0, CALL = 1;
            switch (whatSound) {
                case CHAT:
                    wavfile = new File("resources/lancommschat.wav");
                    break;
                case CALL:
                    wavfile = new File("resources/lancommscall.wav");
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
//        DataLine.Info info = new DataLine.Info(Clip.class, format);
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
            clip.loop(clip.LOOP_CONTINUOUSLY);
            return x;
        }

        public void stopSound() {
            if (clip != null) {
                clip.stop();
            }
        }

    }

}
