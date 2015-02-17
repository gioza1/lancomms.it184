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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * The server as a GUI
 */
public class ServerGUI extends JFrame implements WindowListener {

    DefaultListModel<ClientObject> model = new DefaultListModel<>();
//    List<Client> cl = new ArrayList<Client>();
    final JList list = new JList(model);
//    JList list = new JList(new Vector<Client>(cl));

    private static final long serialVersionUID = 1L;
    // the stop and start buttons
    private JButton stopStart;
    // JTextArea for the chat room and the events
    private JTextArea chat, event;
    // The port number
    private JTextField tPortNumber;
    // my server
    private Server server;

    private ClientObject me;

    // server constructor that receive the port to listen to for connection as parameter
    ServerGUI(ClientObject test) {

        super("Chat Server");
        me = test;
        server = null;
        // in the NorthPanel the PortNumber the Start and Stop buttons
//        JPanel north = new JPanel();
//        north.add(new JLabel("Port number: "));
//        tPortNumber = new JTextField("  " + port);
//        north.add(tPortNumber);

        // the event and chat room
        JPanel center = new JPanel(new GridLayout(3, 1));
        chat = new JTextArea(80, 80);
        chat.setEditable(false);
        appendRoom("Chat room.\n");
        center.add(new JScrollPane(chat));
        event = new JTextArea(80, 80);
        event.setEditable(false);
        appendEvent("Events log.\n");
        center.add(new JScrollPane(event));
        add(center);

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

//        JList list = new JList(new Vector<Client>(cds));
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
        center.add(new JScrollPane(list));
        add(center);

        // need to be informed when the user click the close button on the frame
        addWindowListener(this);
        setSize(400, 600);
        setVisible(true);

        // if running we have to stop
        if (server != null) {
            server.stop();
            server = null;
            tPortNumber.setEditable(true);
            stopStart.setText("Start");
            return;
        }

//         ceate a new Server
//        server = new Server(this, me);
        System.out.println("USERNAME = " + test.getUsername());
//         and start it as a thread
        new ServerRunning().start();

    }

    // append message to the two JTextArea
    // position at the end
    void appendRoom(String str) {
        chat.append(str);
        chat.setCaretPosition(chat.getText().length() - 1);
    }

    void appendEvent(String str) {
        event.append(str);
        event.setCaretPosition(chat.getText().length() - 1);

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
//
    void deleteContact(ClientObject str) {
        for (int i = 0; i < model.size(); i++) {
            if (model.elementAt(i).equals(str)) {
                model.remove(i);
            }
        }
    }

    // start or stop where clicked
//	public void actionPerformed(ActionEvent e) {
//		// if running we have to stop
//		if(server != null) {
//			server.stop();
//			server = null;
//			tPortNumber.setEditable(true);
//			stopStart.setText("Start");
//			return;
//		}
//      	// OK start the server	
//		int port;
//		try {
//			port = Integer.parseInt(tPortNumber.getText().trim());
//		}
//		catch(Exception er) {
//			appendEvent("Invalid port number");
//			return;
//		}
//		// ceate a new Server
//		server = new Server(port, this);
//		// and start it as a thread
//		new ServerRunning().start();
//		stopStart.setText("Stop");
//		tPortNumber.setEditable(false);
//	}
//	// entry point to start the Server
//	public static void main(String[] arg) {
//		// start server default port 1500
//		new ServerGUI(1500);
//	}

    /*
     * If the user click the X button to close the application
     * I need to close the connection with the server to free the port
     */
    public void windowClosing(WindowEvent e) {
        // if my Server exist
        if (server != null) {
            try {
                server.stop();			// ask the server to close the conection
            } catch (Exception eClose) {
            }
            server = null;
        }
        // dispose the frame
        dispose();
        System.exit(0);
    }

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
//    public static void main(String[] args) {
//
//      new ServerGUI(52279);
//    }

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
