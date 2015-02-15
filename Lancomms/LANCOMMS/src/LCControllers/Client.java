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
import LCControllers.Server.ClientThread;
import LCViews.MainUI;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    // for I/O
    private transient ObjectInputStream sInput;		// to read from the socket
    private transient ObjectOutputStream sOutput;		// to write on the socket
    private transient Socket socket;

    //main server
    private transient ObjectInputStream mainSInput;		// to read from the socket
    private transient ObjectOutputStream mainSOutput;		// to write on the socket
    private transient Socket mSocket;

    // if I use a GUI or not
    private MainUI cg;

    // the server, the port and the username
    private String runningServer, username;
    private int runningPort;

    private transient ClientObject me;
    /*
     *  Constructor called by console mode
     *  server: the server address
     *  port: the port number
     *  username: the username
     */
//    Client(String server, int port, String username) {
//        // which calls the common constructor with the GUI set to null
//        this(server, port, username, null);
//    }

    public Socket getSocket() {
        return socket;
    }

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
        System.out.println(me.getServer() + me.getPort() + me.getUsername());
        try {
            socket = new Socket(runningServer, runningPort); // CONNECTS TO HIS/HER OWN SERVER
        } // if it failed not much I can so
        catch (UnknownHostException ex) {
            System.out.println("Host was not found: " + runningServer);
            return false;
        } catch (IOException ec) {
            ec.printStackTrace();
            System.out.println("Error connecting to server:" + ec);
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        System.out.println(msg);

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
        System.out.println("Now listening to server");
        // success we inform the caller that it worked
        return true;
    }

    public boolean connectToMainServer() {
        // try to connect to the server
        try {
            mSocket = new Socket("192.168.1.102", 1500);
        } // if it failed not much I can so
        catch (Exception ec) {
            System.out.println("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted with" + mSocket.getInetAddress() + ":" + mSocket.getPort();
        System.out.println("Successfully connected to the server");

        /* Creating both Data Stream */
        try {
            mainSInput = new ObjectInputStream(mSocket.getInputStream());
            mainSOutput = new ObjectOutputStream(mSocket.getOutputStream());
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
            disconnect();
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
//        try {
//            mainSOutput.writeObject(msg);
//        } catch (IOException e) {
//            display("Exception writing to server: " + e);
//        }
    }

    void sendMessageToServer(ChatMessage msg) {
        try {
            mainSOutput.writeObject(msg);
        } catch (IOException e) {
            System.out.println("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
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

        // inform the GUI
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
                    // if console mode print the message and add back the prompt

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
//            boolean connected = true;
            while (true) {
                System.out.println("ListenFromMainServer Started");
//                ArrayList<ClientObject> eh = new ArrayList<ClientObject>();
                ArrayList<ClientObject> eh = (ArrayList<ClientObject>) Client.receiveObject(mainSInput);
                if (eh != null) {
                    cg.updateList(eh);
                } else {
                    break;
                }
            }
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
    
    public ClientObject getCo()
    {
        return me;
    }
}
//}