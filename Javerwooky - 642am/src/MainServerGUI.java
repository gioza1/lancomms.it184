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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.BevelBorder;

/*
 * The server as a GUI
 */
public class MainServerGUI extends JFrame implements ActionListener {

    DefaultListModel<ClientObject> model = new DefaultListModel<>();
//    final JList list = new JList(model);
    JList list = new JList(model);

    private static final long serialVersionUID = 1L;
    // the stop and start buttons
    private JButton stopStart;
    // JTextArea for the chat room and the events
    private JTextArea chat, separate, event;
    // The port number
    private JTextField tPortNumber, tIpAddress;
    // my server
    private MainServer server;

    private ClientObject me;

    // server constructor that receive the port to listen to for connection as parameter
    MainServerGUI() {
        super("LANCOMMS Main Server");
        server = null;

        ParseRoute pr = new ParseRoute();

        JPanel north = new JPanel();
        north.add(new JLabel("Server Address: "));

        try {
            north.add(new JLabel(InetAddress.getByName(pr.getLocalIPAddress()).toString()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainServerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
//        north.add(tIpAddress);
        north.add(new JLabel("   "));
        north.add(new JLabel("Port number: "));
        north.add(new JLabel("1500"));

        add(north, BorderLayout.NORTH);
        //MENU BAR//
        JMenuBar menuBar = new JMenuBar();
        // Add the menubar to the frame
        setJMenuBar(menuBar);
        // Define and add two drop down menu to the menubar
        JMenu manageMenu = new JMenu("Server");
        menuBar.add(manageMenu);

        JMenuItem manageUsers = new JMenuItem("Manage Accounts");
        JMenuItem exitAction = new JMenuItem("Exit");
        manageMenu.add(manageUsers);
        manageMenu.add(exitAction);
        manageUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                new AccountManager().setVisible(true);
            }
        });
        exitAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (server != null) {
                    try {
                        server.stop();			// ask the server to close the conection
                    } catch (Exception eClose) {
                    }
                    server = null;
                }
                System.exit(1);
            }
        });

        // the event and chat room
        JPanel center = new JPanel(new GridLayout(2, 1));
//        chat = new JTextArea(80, 80);
//        chat.setEditable(false);
//        appendRoom("Chat room.\n");
//        center.add(new JScrollPane(chat));
        event = new JTextArea(80, 80);
        event.setEditable(false);
        event.setLineWrap(true);
        appendEvent("Events log.\n");
        center.add(new JScrollPane(event));
        add(center);
//        center.add(new JScrollPane(list));
//        add(center);

//        separate = new JTextArea(10, 10);
//        separate.setEditable(false);
//        separate.setLineWrap(true);
//        appendEvent("List of Connected Users:");
        /*--------------------------------------------------------*/
        /*------------------------------------*/
        list.addMouseListener(null);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        ClientObject o = (ClientObject) theList.getModel().getElementAt(index);

                        System.out.println("Double-clicked on: " + o.getUsername() + " IP: " + o.getServer() + " PORT: " + o.getPort());
                    }
                }
            }
        };
        list.addMouseListener(mouseListener);

        list.setVisibleRowCount(10);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof ClientObject) {
                    // Here value will be of the Type 'CD'
                    ((JLabel) renderer).setText(((ClientObject) value).getUsername());
                }
                return renderer;
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        JLabel label = new JLabel("Header", JLabel.CENTER);
        label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        scrollPane.setColumnHeaderView(label);
        
        center.add(new JScrollPane(list));
        add(center);
        /*------------------------------------*/

        // need to be informed when the user click the close button on the frame
//        addWindowListener(this);
        setSize(400, 600);

        setVisible(true);
        if (server != null) {
            server.stop();
            server = null;
            tPortNumber.setEditable(true);
            stopStart.setText("Start");
            return;
        }
      	// OK start the server	

        // ceate a new Server
        server = new MainServer(this);
        // and start it as a thread
        new ServerRunning().start();

        setVisible(true);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        this.setLocationByPlatform(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });

    }

    // append message to the two JTextArea
    // position at the end
    void appendUsers(String str) {
        separate.append(str);
        separate.setCaretPosition(separate.getText().length() - 1);
    }

    void appendEvent(String str) {
        event.append(str);
        event.setCaretPosition(event.getText().length() - 1);

    }

    void appendContact(ClientObject str) {

        int test = model.getSize();
        model.addElement(str);
        if (model.getSize() > test) {
            System.out.println("Append contact success");
        }
//        event.setCaretPosition(chat.getText().length() - 1);
//        cl.add(str);
    }
//        event.setCaretPosition(chat.getText().length() - 1);

    void deleteContact(ClientObject str) {
        for (int i = 0; i < model.size(); i++) {
            if (model.elementAt(i).equals(str)) {
                model.remove(i);
            }
        }
    }

    // start or stop where clicked
    public void actionPerformed(ActionEvent e) {
        // if running we have to stop

    }

    // entry point to start the Server
    public static void main(String[] arg) {
        // start server default port 1500
        new MainServerGUI();
    }

    /*
     * If the user click the X button to close the application
     * I need to close the connection with the server to free the port
     */
//    @Override
//    public void windowClosing(WindowEvent e) {
//
//    }
    // I can ignore the other WindowListener method
    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    /*
     * A thread to run the Server
     */
    class ServerRunning extends Thread {

        public void run() {
            server.start();         // should execute until if fails
            // the server failed
            stopStart.setText("Start");
            tPortNumber.setEditable(true);
            appendEvent("Server crashed\n");
            server = null;
        }
    }

}
