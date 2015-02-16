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
import java.net.*;
import java.io.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client {

    // for I/O
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private transient Socket socket;

    //main server
    private ObjectInputStream mainSInput;		// to read from the socket
    private ObjectOutputStream mainSOutput;		// to write on the socket
    private transient Socket mSocket;

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
//    Client(String server, int port, String username, ClientGUI cg) {
//        this.server = server;
//        this.port = port;
//        this.username = username;
//        // save if we are in GUI mode or not
//        this.cg = cg;
//    }
    Client(ClientObject test,String serv, int port) {
//        server = test.getServer();
//        port = test.getPort();
//        username = test.getUsername();
//        // save if we are in GUI mode or not
//        cg = test.getGui();
        runningServer = serv;
        runningPort = port;
        me = test;
    }

    /*
     * To start the dialog
     */
    public boolean start() {
        boolean successFlag = false;
        System.out.println(me.getServer() + me.getPort() + me.getUsername());
        try {
            socket = new Socket("localhost", runningPort);
        } // if it failed not much I can so
        catch (UnknownHostException ex) {
            display("Host was not found: " + runningServer);
        } catch (IOException ec) {
            ec.printStackTrace();
            display("Error connecting to server:" + ec);
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        System.out.println(me.getUsername());
        try {
            sOutput.writeObject(me);
//            sOutput.flush();
        } catch (IOException eIO) {
            eIO.printStackTrace();
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    public boolean connectToMainServer() {
        // try to connect to the server
        try {
            mSocket = new Socket("localhost", 1500);
        } // if it failed not much I can so
        catch (Exception ec) {
            display("Error connectiong to server:" + ec);
            return false;
        }

        String msg = "Connection accepted " + mSocket.getInetAddress() + ":" + mSocket.getPort();
        display(msg);

        /* Creating both Data Stream */
        try {
            mainSInput = new ObjectInputStream(mSocket.getInputStream());
            mainSOutput = new ObjectOutputStream(mSocket.getOutputStream());
        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new ListenFromMainServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            mainSOutput.writeObject(me);
        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    /*
     * To send a message to the console or the GUI
     */
    private void display(String msg) {
            System.out.println(msg);      // println in console mode
    }

    /*
     * To send a message to the server
     */
    void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            display("Exception writing to server: " + e);
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
            display("Exception writing to server: " + e);
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

                } catch (IOException e) {
                    display("Server has close the connection: " + e);
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
                try {
                    String msg = (String) mainSInput.readObject();
                    // if console mode print the message and add back the prompt
        
                } catch (IOException e) {
                    display("Server has close the connection: " + e);
                    break;
                } // can't happen with a String object but need the catch anyhow
                catch (ClassNotFoundException e2) {
                }
            }
        }
}
}
