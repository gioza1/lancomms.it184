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
import LCViews.ChatWindowUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    // will first hold "Username:", later on "Enter message"
    private JLabel label;
    // to hold the Username and later on the messages
    private JTextField tf;
    // to hold the server address an the port number
    private JTextField tfServer, tfPort;
    // to Logout and get the list of the users
    private JButton login, logout, whoIsIn;
    // for the chat room
    private JTextArea ta;
    // if it is for connection
    private boolean connected;
    // the Client object
    private transient Client client;
    // the default port number
    private String username;

    private int defaultPort;
    private String defaultHost;

    private ClientObject tocObj;
    private ClientObject fromcObj;

    // Constructor connection receiving a socket number
    public ClientGUI(ClientObject to, ClientObject from) {

        super(to.getUsername());
//        System.out.println(port);
//        cObj = co;
        tocObj = to;
        fromcObj = from;
//        defaultPort = port;
//        defaultHost = server;

        // The NorthPanel with:
        JPanel northPanel = new JPanel(new GridLayout(3, 1));
        // the server name anmd the port number
        JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 3));
        // the two JTextField with default value for server address and port number
        tfServer = new JTextField(defaultHost);
//        try {
//            tfServer = new JTextField(InetAddress.getLocalHost().getHostAddress().toString());
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
        tfPort = new JTextField("" + defaultPort);
        tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

        serverAndPort.add(new JLabel("Server Address:  "));
        serverAndPort.add(tfServer);
        serverAndPort.add(new JLabel("Port Number:  "));
        serverAndPort.add(tfPort);
        serverAndPort.add(new JLabel(""));
        // adds the Server an port field to the GUI
        northPanel.add(serverAndPort);

        // the Label and the TextField
        label = new JLabel("Enter your username below", SwingConstants.CENTER);
        northPanel.add(label);
        tf = new JTextField("Anonymous");
        tf.setBackground(Color.WHITE);
        northPanel.add(tf);
        add(northPanel, BorderLayout.NORTH);

        // The CenterPanel which is the chat room
        ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        centerPanel.add(new JScrollPane(ta));
        ta.setEditable(false);
        add(centerPanel, BorderLayout.CENTER);

        // the 3 buttons
        login = new JButton("Login");
        login.addActionListener(this);
        logout = new JButton("Logout");
        logout.addActionListener(this);
        logout.setEnabled(true);		// you have to login before being able to logout
        whoIsIn = new JButton("Call");
        whoIsIn.addActionListener(this);
        whoIsIn.setEnabled(true);		// you have to login before being able to Who is in

        JPanel southPanel = new JPanel();
        southPanel.add(login);
        southPanel.add(logout);
        southPanel.add(whoIsIn);
        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 550);
        setVisible(true);
        tf.requestFocus();

    }

    // called by the Client to append text in the TextArea 
    public void append(String str) {
        ta.append(str);
        ta.setCaretPosition(ta.getText().length() - 1);
    }

    // called by the GUI is the connection failed
    // we reset our buttons, label, textfield
    public void connectionFailed() {
        connected = false;
    }

    /*
     * Button or JTextField clicked
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        // if it is the Logout button
        if (o == logout) {
//            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
            client.sendMessageToServer(new ChatMessage(ChatMessage.WHOISIN, ""));
            return;
        }
        // if it the who is in button
        if (o == whoIsIn) {
//            client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));
//            client.sendMessageToServer(new ChatMessage(ChatMessage.WHOISIN, ""));
//            return;
            try {
            // TODO add your handling code here:

                //call.startCall(ipTextField.getText(), targetPort.getText());
                //Call call = new Call();
                //call.startCall("192.168.1.103", "6112");
            } catch (Exception ex) {
                Logger.getLogger(ChatWindowUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // ok it is coming from the JTextField
        if (connected) {
            // just have to send the message
            ChatMessage cmsg = new ChatMessage(ChatMessage.MESSAGE, tf.getText());
            tf.setText("");

            client.sendMessage(cmsg);
            client.sendMessageToServer(cmsg);

            return;
        }

        if (o == login) {
            // ok it is a connection request
            username = tf.getText().trim();
            // empty username ignore it
            if (username.length() == 0) {
                return;
            }
            // empty serverAddress ignore it
            String server = tfServer.getText().trim();
            if (server.length() == 0) {
                return;
            }
            // empty or invalid port numer, ignore it
            String portNumber = tfPort.getText().trim();
            if (portNumber.length() == 0) {
                return;
            }

//            if (cObj == null) {
            // try creating a new Client with GUI
//                cObj = new ClientObject(defaultHost, Integer.parseInt(portNumber), username, this);
//            System.out.println(client.getPort());
        }
//            client = new Client(cObj, server, Integer.parseInt(portNumber));
//            new ServerGUI(clObj);
//            Server x = new Server
        // test if we can start the Client
//            
//
//            if (!client.connectToMainServer()) {
//                return;
//            }
//            if (!client.start()) {
//                return;
//            }

        tf.setText("");
        label.setText("Enter your message below");
        connected = true;

        // disable login button
        login.setEnabled(false);
        // enable the 2 buttons
        logout.setEnabled(true);
        whoIsIn.setEnabled(true);
        // disable the Server and Port JTextField
        tfServer.setEditable(false);
        tfPort.setEditable(false);
        // Action listener for when the user enter a message
        tf.addActionListener(this);

    }

}

    // to start the whole thing the server
//    public static void main(String[] args) {
//
//        int port = 0;
//        ServerSocket test;
//        try {
//            test = new ServerSocket(0);
//            port = test.getLocalPort();
//            test.close();
//        } catch (IOException ex) {
//            //Logger.getLogger(ServerGUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        new ClientGUI("1515", port);
//    }

