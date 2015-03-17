/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LCViews;

import LCControllers.ChatMessage;
import LCControllers.Client;
import LCControllers.ClientObject;
import LCControllers.Contacts;
import LCControllers.ConvObject;
import LCControllers.Conversations;
import LCControllers.Login;
import LCControllers.ParseRoute.ParseRoute;
import LCControllers.Server;
import LCControllers.Session;
import LCModels.UserModel;
import com.sun.security.auth.module.NTSystem;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class MainUI extends JFrame implements Serializable {

    private Session sess;
    private int userId;
    private int idrow;
    private String namerow;
    private int counter;
    private ClientObject myCObj;
    private Conversations convo;
    private Client myClient;
    private UserModel umodel;
    private Server myServer;
    private ArrayList<ChatWindowUI> algui;
    private ArrayList<ConversationUI> cvgui;
    private ArrayList<ConvObject> convos;
    private ArrayList<ClientObject> userList;
    private boolean callDisabled;
    private List<String> chatting;
    private List<String> conving;
    private SettingsUI sui;
    private ImageIcon img;
    private boolean settings;
    private boolean isAlreadyOnline;
    private String host;
    private ServerRunning sr;
    DefaultListModel<ClientObject> model = new DefaultListModel<>();

    /**
     * Creates new form MainUI
     *
     * @param uid
     * @param co
     */
    public MainUI(int uid, String co, String hostadd) {                  //that username is so legit.
        myServer = null;
        sui = null;
        umodel = new UserModel();
        callDisabled = false;
        settings = false;
        algui = new ArrayList<ChatWindowUI>();
        cvgui = new ArrayList<ConversationUI>();
        chatting = new ArrayList<String>();
        conving = new ArrayList<String>();
        host = hostadd;

        int port = 0;
        ParseRoute pr = new ParseRoute();
        String server = pr.getLocalIPAddress();

        //create own port
        try {
            ServerSocket getp;
            getp = new ServerSocket(0);
            port = getp.getLocalPort();
            getp.close();
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        myCObj = new ClientObject(server, port, co, uid);
        //-------//
        //creating own client instance. handles connecting to main server
        myClient = new Client(myCObj, server, port);
        System.out.println("Name: " + co + " |Server: " + server + " |Port: " + port);
        System.out.println(myCObj.toString());
        System.out.println(myClient.getCo().toString());

        myClient.setMainUI(this);
        myServer = new Server(myCObj, this);
        sr = new ServerRunning();

        sr.start();

        if (!myClient.connectToMainServer(host)) {
            return;
        }

        if (!myClient.start()) {
            return;
        }

        this.counter = 0; //window count
        sess = new Session(uid); // store session
        userId = sess.getId();

        setConvoList();

        initComponents();
        initComponents2();

        updateConvoList(convos);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationByPlatform(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 4 - this.getSize().width / 4, dim.height / 2 - this.getSize().height / 2);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExtendedState(JFrame.ICONIFIED);
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents2() {

        jPanel1 = new javax.swing.JPanel();
        MainTabs = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        Contacts contact = new Contacts();
        jTable1 = new javax.swing.JList(model);
        ConvoList = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        UserNameDisplay = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        profpic = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        LancommsMenu = new javax.swing.JMenu();
        broadcast = new javax.swing.JMenuItem();
        groupChat = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();
//        sendToMany = new javax.swing.JMenuItem();
        ContactsList = new javax.swing.JPanel();
        ContactsList.setForeground(new java.awt.Color(255, 255, 255));

        WindowListener l = new WindowAdapter() {
            List<Window> windows = new ArrayList<Window>();

            @Override
            public void windowOpened(WindowEvent e) {
                windows.add(e.getWindow());
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (windows.size() > 1) {
                    windows.remove(e.getWindow());
                    e.getWindow().dispose();
                }
            }
        };

        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(112, 196, 59));
        setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255)); //greenish 251, 255, 246
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 589));

        MainTabs.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(jTable1.getFont());
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setInheritsPopupMenu(true);

        jTable1.addMouseListener(null);
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        ClientObject o = (ClientObject) theList.getModel().getElementAt(index);
                        if (!chatting.contains(o.getFullName())) {

                            ChatWindowUI cw = new ChatWindowUI(o, myCObj);
                            System.out.println(o.toString());
                            System.out.println(myCObj.toString());
                            cw.setTitle(o.getFullName());
                            cw.setVisible(true);
                            cwSetMainUI(cw);
                            addChatWindow(cw);
                            chatting.add(o.getFullName());
                        } else {

                            for (Frame cwindow : Frame.getFrames()) {
                                if (!cwindow.isShowing() && cwindow.getTitle().equals(o.getFullName())) {
                                    System.out.println("Setting visible the chat window of: " + cwindow.getTitle());
                                    cwindow.setVisible(true);
                                }
                            }
                        }
                    }
                }
            }
        };
        jTable1.addMouseListener(mouseListener);

//        JList list = new JList(new Vector<Client>(cds));
        jTable1.setVisibleRowCount(10);
        jTable1.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof ClientObject) {
                    // Here value will be of the Type 'CD'
//                    if(!((ClientObject) value).getUsername().contentEquals(myCObj.getUsername()))
                    //((JLabel) renderer).setText(((ClientObject) value).getUsername()+" "+((ClientObject) value).getStatus());
                    //ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/pp2.png"));

                    //setIcon(imageIcon);
                    ((JLabel) renderer).setText(((ClientObject) value).getFullName() + "  (" + ((ClientObject) value).getStatus() + ") ");
                }
                return renderer;
            }
        });
        //jTable1.addMouseListener(mouseListener);
//        try {
//            jTable1.removeColumn(jTable1.getColumnModel().getColumn(2));
//            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(rootPane, "Error " + e.getMessage());
//            e.printStackTrace();
//        }

//        jTable1.setFillsViewportHeight(true);
        jScrollPane3.setViewportView(jTable1);

        javax.swing.GroupLayout ContactsListLayout = new javax.swing.GroupLayout(ContactsList);
        ContactsList.setLayout(ContactsListLayout);
        ContactsListLayout.setHorizontalGroup(
                ContactsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ContactsListLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ContactsListLayout.setVerticalGroup(
                ContactsListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ContactsListLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 16, Short.MAX_VALUE))
        );

        MainTabs.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=38 marginheight=5>Online Users</body></html>", ContactsList);

//        jList2.setModel(new javax.swing.AbstractListModel() {
//            String[] strings = {"Mike Test (2015-2-15 06:47)"};
//
//            public int getSize() {
//                return strings.length;
//            }
//
//            public Object getElementAt(int i) {
//                return strings[i];
//            }
//        });
        jList2.addMouseListener(null);
        MouseListener mouseListener2 = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        ConvObject o = (ConvObject) theList.getModel().getElementAt(index);
                        if (!conving.contains(o.getNameTo())) {
                            o.setMessages();
                            ConversationUI cv = new ConversationUI(o);
                            cv.setTitle("Message Logs with " + o.getNameTo());
                            cv.setVisible(true);
                            cvSetMainUI(cv);
                            addConvoWindow(cv);
                            conving.add(o.getNameTo());
                        } else {

                            for (Frame cwindow : Frame.getFrames()) {
                                if (!cwindow.isShowing() && cwindow.getTitle().equals("Message Logs with " + o.getNameTo())) {
                                    System.out.println("Setting visible the convo window of: " + cwindow.getTitle());
                                    o.setMessages();
                                    cwindow.setVisible(true);
                                }
                            }
                        }
                    }
                }
            }
        };
        jList2.addMouseListener(mouseListener2);
        jList2.setVisibleRowCount(10);
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setToolTipText("Message Logs");
        jList2.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof ConvObject) {
                    ((JLabel) renderer).setText(((ConvObject) value).getLatest());
                }
                return renderer;
            }
        });

        jScrollPane2.setViewportView(jList2);

        javax.swing.GroupLayout ConvoListLayout = new javax.swing.GroupLayout(ConvoList);
        ConvoList.setLayout(ConvoListLayout);
        ConvoListLayout.setHorizontalGroup(
                ConvoListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );
        ConvoListLayout.setVerticalGroup(
                ConvoListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );

        MainTabs.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=40 marginheight=5>Message Logs</body></html>", ConvoList);

        NTSystem NTSystem = new NTSystem();//test
//        UserNameDisplay.setText(System.getProperty("user.name"));
        UserNameDisplay.setText(myCObj.getFullName());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/mainui1-1-2.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/mainui1-2.png"))); // NOI18N

        profpic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/pp2.png"))); // NOI18N

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/mainui1-1-1.png"))); // NOI18N

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/mainui1-1-3.png"))); // NOI18N

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lancomms/mainui1-1-4.png"))); // NOI18N

        img = new ImageIcon(getClass().getResource("/lancomms/pp2.png"));
        this.setIconImage(img.getImage());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(MainTabs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 345, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(UserNameDisplay)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(UserNameDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                        .addComponent(MainTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        LancommsMenu.setText("LANCOMMS");

        groupChat.setText("Group Chat");
        groupChat.setInheritsPopupMenu(true);
        groupChat.setIconTextGap(0);
        groupChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                new InviteGroupChat(userList, myClient).setVisible(true);
            }
        });

        LancommsMenu.add(groupChat);

        broadcast.setText("Send to Many");
        broadcast.setInheritsPopupMenu(true);
        broadcast.setIconTextGap(0);
        broadcast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SendToMany(userList, myClient).setVisible(true);;
            }
        });

        LancommsMenu.add(broadcast);

        settingsMenuItem.setText("Settings");

//        settingsMenuItem.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
//        settingsMenuItem.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
//        settingsMenuItem.setIconTextGap(1);
        settingsMenuItem.setInheritsPopupMenu(true);
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        LancommsMenu.add(settingsMenuItem);

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        LancommsMenu.add(aboutMenuItem);

        logoutMenuItem.setText("Log Out");
        logoutMenuItem.setIconTextGap(0);
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        LancommsMenu.add(logoutMenuItem);

        jMenuBar1.add(LancommsMenu);

        setJMenuBar(jMenuBar1);

        jComboBox1 = new javax.swing.JComboBox();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Online", "Busy"}));

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel4)
                                                        .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGap(0, 0, Short.MAX_VALUE)
                                                        .addComponent(jLabel5))))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addComponent(profpic)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(UserNameDisplay)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel6)))
                        .addComponent(jLabel1))
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(MainTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(160, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel4))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(profpic))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(UserNameDisplay)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel5)
                                                                .addGap(0, 0, 0)
                                                                .addComponent(jLabel6)))))
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(MainTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(501, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(112, 196, 59));
        setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        ChatMessage status = new ChatMessage(ChatMessage.STATUS, jComboBox1.getSelectedItem().toString());
        myClient.sendMessageToServer(status);
    }

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (settings == false) {
            sui = new SettingsUI(userId, this);
            sui.setVisible(true);
            System.out.println("sui is " + settings);
        } else if (settings == true) {
            if (!sui.isVisible()) {
                sui.setVisible(true);
            }
        }
    }

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        logout();
    }

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        AboutUI about = new AboutUI();
        about.setVisible(true);
    }

    /**
     * Sun property pointing the main class and its arguments. Might not be
     * defined on non Hotspot VM implementations.
     */
    public static final String SUN_JAVA_COMMAND = "sun.java.command";

    /**
     * Restart the current Java application
     *
     * @param runBeforeRestart some custom code to be run before restarting
     * @throws IOException
     */
    public static void restartApplication(Runnable runBeforeRestart) throws IOException {
        try {
            // java binary
            String java = System.getProperty("java.home") + "/bin/java";
            // vm arguments
            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            StringBuffer vmArgsOneLine = new StringBuffer();
            for (String arg : vmArguments) {
                // if it's the agent argument : we ignore it otherwise the
                // address of the old application and the new one will be in conflict
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg);
                    vmArgsOneLine.append(" ");
                }
            }
            // init the command to execute, add the vm args
            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

            // program main and program arguments
            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            // program main is a jar
            if (mainCommand[0].endsWith(".jar")) {
                // if it's a jar, add -jar mainJar
                cmd.append("-jar " + new File(mainCommand[0]).getPath());
            } else {
                // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
            }
            // finally add program arguments
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            // execute the command in a shutdown hook, to be sure that all the
            // resources have been disposed before restarting the application
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // execute some custom code before restarting
            if (runBeforeRestart != null) {
                runBeforeRestart.run();
            }
            // exit
            System.exit(0);
        } catch (Exception e) {
            // something went wrong
            throw new IOException("Error while trying to restart the application", e);
        }
    }

    public void logout() {
        this.dispose();
        System.gc();
        int i = 0;
        for (ChatWindowUI a : algui) {
            a.disconnect();
            a.dispose();
            System.out.println("ChatWindow " + ++i + " is disposed");
        }
        for (Frame frames : Frame.getFrames()) {
            if (!frames.isShowing() || frames.isShowing()) {
                frames.dispose();
            }
        }
        myServer.stop();
        myClient.disconnectMain();
        myClient.disconnect();
        sr.interrupt();
        try {
            restartApplication(null);
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        //disconnect all sockets to other clients
        Login logoutTime = new Login();
        logoutTime.logoutTime(userId);
        LoginUI loggedout = new LoginUI(host);
        loggedout.setVisible(rootPaneCheckingEnabled);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel ConvoList;
    private javax.swing.JMenu LancommsMenu;
    private javax.swing.JMenuItem broadcast;
    private javax.swing.JMenuItem groupChat;
    private javax.swing.JTabbedPane MainTabs;
    private javax.swing.JLabel UserNameDisplay;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel jLabel1; //UI decal
    private javax.swing.JLabel jLabel2; //UI decal
    private javax.swing.JLabel profpic;
    private javax.swing.JLabel jLabel4; //UI decal
    private javax.swing.JLabel jLabel5; //UI decal
    private javax.swing.JLabel jLabel6; //UI decal
    private javax.swing.JList jList2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList jTable1; //now a list, not a table
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JMenuItem settingsMenuItem;
//    private javax.swing.JMenuItem sendToMany;
    private javax.swing.JPanel ContactsList;

    private void runServer(Server myServer) {
        myServer.run(); //To change body of generated methods, choose Tools | Templates.
    }

    public void appendEvent(String str) {
        System.out.println(str);
    }

    public void updateList(ArrayList<ClientObject> str) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Iterator<ClientObject> ite = str.iterator();
                do {
                    if (ite.next().getFullName().contentEquals(myCObj.getFullName())) {
                        ite.remove();
                        break;
                    }
                } while (ite.hasNext());
                System.out.println("removed myself!");
                userList = str;
                jTable1.setListData(str.toArray());
                jTable1.updateUI();

            }
        });

    }

    public void setConvoList() {
        convo = new Conversations(userId);
        convo.setConvList();
        convos = convo.getConvList();
    }

    public void updateConvoList(ArrayList<ConvObject> cobj) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Iterator<ConvObject> ite = cobj.iterator();
                jList2.setListData(cobj.toArray());
                jList2.updateUI();
            }
        });
    }

    public void setCallDisabled() {
        callDisabled = true;
        for (ChatWindowUI cwindow : algui) {
            cwindow.setCallDisabled();
        }
        System.out.println("Call buttons disabled!");
    }

    public void setCallEnabled() {
        callDisabled = false;
        for (ChatWindowUI cwindow : algui) {
            cwindow.setCallEnabled();
        }
    }

    public boolean checkCallDisabled() {
        return callDisabled;
    }

    public void setSettingsOpened() {
        settings = true;
    }

    public void addChatWindow(ChatWindowUI cwindow) {
        algui.add(cwindow);
        chatting.add(cwindow.getUsername());
        System.out.println("ChatWindows#:" + algui.size());
        for (String x : chatting) {
            System.out.println("Chatting to: " + x);
        }
        if (callDisabled) {
            setCallDisabled();
        }
    }

    public void addConvoWindow(ConversationUI cwindow) {
        cvgui.add(cwindow);
    }

    public ArrayList<ClientObject> hideSelf(ArrayList<ClientObject> str) {
        for (ClientObject test : str) {
            if (test.getFullName().contentEquals(myCObj.getFullName())) {
                str.remove(test);
            }
        }
        return str;
    }

    public void deleteContact(ClientObject str) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < model.size(); i++) {
                    if (model.elementAt(i).equals(str)) {
                        model.remove(i);
                    }
                }
                jTable1.updateUI();
            }
        });
    }

    public void cwSetMainUI(ChatWindowUI cw) {
        ChatWindowUI cwwindow = cw;
        cwwindow.setMainUI(this);
    }

    public void cvSetMainUI(ConversationUI cv) {
        ConversationUI cvwindow = cv;
        cvwindow.setMainUI(this);
    }

//    public void disableGroupChat() {
//        groupChat.setEnabled(false);
//    }
//
//    public boolean enableGroupChat() {
//        groupChat.setEnabled(true);
//        return groupChat.isEnabled();
//    }
//    public boolean isInGroupChat() {
//        return groupChat.isEnabled();
//
//    }

    class ServerRunning extends Thread {

        public void run() {
            myServer.run();
            myServer.stop();
            myServer = null;
        }

    }
    public void setIsAlreadyOnline(boolean tralse) {
        isAlreadyOnline = tralse;
    }

    public boolean getIsAlreadyOnline() {
        return isAlreadyOnline;
    }

}
