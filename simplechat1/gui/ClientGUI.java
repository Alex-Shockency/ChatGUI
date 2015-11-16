package gui;
import static javax.swing.JOptionPane.showMessageDialog;
import client.ChatClient;
import common.ChatIF;
import gui.channelGUI.CreateChannelFrame;
import gui.channelGUI.JoinChannelFrame;
import gui.channelGUI.LeaveChannelFrame;

import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import server.serverNotification;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joshua
 */
public class ClientGUI extends javax.swing.JFrame implements Observer, ChatIF{
    ChatClient ch;
    String lastMessageUser;
    /**
     * Creates new form ClientGUI
     */
    public ClientGUI(String name, String password, String hostname, int portNumber) {
        try {
            ch = new ChatClient(name,password,hostname,portNumber,this);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            lastMessageUser = "";
        } catch (Exception ex) {
            System.exit(-1);
        }
    	initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    	menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        mnUser = new JMenu("User");
        menuBar.add(mnUser);
        
        mntmLogin = new JMenuItem("Login");
        mnUser.add(mntmLogin);
        
        mntmLogoff = new JMenuItem("Logoff");
        mnUser.add(mntmLogoff);
        
        mnNewMenu_2 = new JMenu("Set Status");
        mnUser.add(mnNewMenu_2);
        
        mntmAvailable = new JMenuItem("Available");
        mnNewMenu_2.add(mntmAvailable);
        
        mntmUnavailable = new JMenuItem("Unavailable");
        mnNewMenu_2.add(mntmUnavailable);
        
        mnNewMenu = new JMenu("Channel");
        menuBar.add(mnNewMenu);
        
        mntmCreate = new JMenuItem("Create");
        mnNewMenu.add(mntmCreate);
        
        mntmJoin = new JMenuItem("Join");
        mnNewMenu.add(mntmJoin);
        
        mntmLeave = new JMenuItem("Leave");
        mnNewMenu.add(mntmLeave);
        
        mnNewMenu_1 = new JMenu("Status");
        menuBar.add(mnNewMenu_1);
        
        mntmUser = new JMenuItem("User");
        mnNewMenu_1.add(mntmUser);
        
        mntmChannel = new JMenuItem("Channel");
        mnNewMenu_1.add(mntmChannel);
        
        mnBlocking = new JMenu("Blocking");
        menuBar.add(mnBlocking);
        
        mntmBlock = new JMenuItem("Block");
        mnBlocking.add(mntmBlock);
        
        mnNewMenu_3 = new JMenu("Unblock");
        mnBlocking.add(mnNewMenu_3);
        
        mntmNewMenuItem_1 = new JMenuItem("Unblock User");
        mnNewMenu_3.add(mntmNewMenuItem_1);
        
        mntmNewMenuItem_2 = new JMenuItem("Unblock All");
        mnNewMenu_3.add(mntmNewMenuItem_2);

        GUITitle = new javax.swing.JLabel();
        MessageInputArea = new javax.swing.JTextField();
        MessageSendButton = new javax.swing.JButton();
        MessageScrollPane = new javax.swing.JScrollPane();
        MessagePanel = new javax.swing.JPanel();
        ChannelLabel = new javax.swing.JLabel();
        ChannelName = new javax.swing.JLabel();
        ChannelOptionsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        GUITitle.setText("Chat Client");

        MessageInputArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MessageInputAreaKeyPressed(evt);
            }
        });

        MessageSendButton.setText("Send");
        MessageSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MessageSendButtonActionPerformed(evt);
            }
        });

        MessagePanel.setBackground(new java.awt.Color(255, 255, 255));
        MessagePanel.setLayout(new javax.swing.BoxLayout(MessagePanel, javax.swing.BoxLayout.Y_AXIS));
        MessageScrollPane.setViewportView(MessagePanel);

        ChannelLabel.setText("Channel:");

        ChannelName.setText("General Chat");

        ChannelOptionsButton.setText("Options");
        ChannelOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChannelOptionsButtonActionPerformed(evt);
            }
        });
        
        mntmCreate.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		CreateChannelOptionSelected(evt);
        	}
        });
        mntmLeave.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		LeaveChannelOptionSelected(evt);
        	}
        });
        
        mntmJoin.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		JoinChannelOptionSelected(evt);
        	}
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(MessageInputArea, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(MessageSendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(MessageScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChannelOptionsButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(GUITitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ChannelLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ChannelName)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(GUITitle)
                    .addComponent(ChannelLabel)
                    .addComponent(ChannelName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ChannelOptionsButton)
                    .addComponent(MessageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MessageInputArea, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MessageSendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CreateChannelOptionSelected(ActionEvent evt) {
    	   CreateChannelFrame chan = CreateChannelFrame.getInstance(this);
           Point p = MouseInfo.getPointerInfo().getLocation();
           int xOffset = (int) p.getX() - 25;
           int yOffset = (int) p.getY() - 25;
           chan.setLocation(xOffset, yOffset);
           chan.setVisible(true);
           chan.setAlwaysOnTop(true);
	}
    private void JoinChannelOptionSelected(ActionEvent evt) {
 	    JoinChannelFrame chan = JoinChannelFrame.getInstance(this);
        Point p = MouseInfo.getPointerInfo().getLocation();
        int xOffset = (int) p.getX() - 25;
        int yOffset = (int) p.getY() - 25;
        chan.setLocation(xOffset, yOffset);
        chan.setVisible(true);
        chan.setAlwaysOnTop(true);
	}
    private void LeaveChannelOptionSelected(ActionEvent evt) {
 	   LeaveChannelFrame chan = LeaveChannelFrame.getInstance(this);
        Point p = MouseInfo.getPointerInfo().getLocation();
        int xOffset = (int) p.getX() - 25;
        int yOffset = (int) p.getY() - 25;
        chan.setLocation(xOffset, yOffset);
        chan.setVisible(true);
        chan.setAlwaysOnTop(true);
	}

	private void MessageSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MessageSendButtonActionPerformed
        String text = MessageInputArea.getText();
        MessageInputArea.setText("");
            ch.handleMessageFromClientUI(text);
    }//GEN-LAST:event_MessageSendButtonActionPerformed

    private void MessageInputAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MessageInputAreaKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            String text = MessageInputArea.getText();
        MessageInputArea.setText("");
            ch.handleMessageFromClientUI(text);
        }
    }//GEN-LAST:event_MessageInputAreaKeyPressed

    private void ChannelOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChannelOptionsButtonActionPerformed
        ChannelFrame chan = ChannelFrame.getInstance(this);
        Point p = MouseInfo.getPointerInfo().getLocation();
        int xOffset = (int) p.getX() - 25;
        int yOffset = (int) p.getY() - 25;
        chan.setLocation(xOffset, yOffset);
        chan.setVisible(true);
        chan.setAlwaysOnTop(true);
    }//GEN-LAST:event_ChannelOptionsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChannelLabel;
    private javax.swing.JLabel ChannelName;
    private javax.swing.JButton ChannelOptionsButton;
    private javax.swing.JLabel GUITitle;
    private javax.swing.JTextField MessageInputArea;
    private javax.swing.JPanel MessagePanel;
    private javax.swing.JScrollPane MessageScrollPane;
    private javax.swing.JButton MessageSendButton;
    private JMenuBar menuBar;
    private JMenu mnNewMenu;
    private JMenu mnNewMenu_1;
    private JMenu mnUser;
    private JMenuItem mntmLeave;
    private JMenuItem mntmLogin;
    private JMenuItem mntmLogoff;
    private JMenuItem mntmBlock;
    private JMenu mnBlocking;
    private JMenuItem mntmUser;
    private JMenuItem mntmChannel;
    private JMenu mnNewMenu_2;
    private JMenuItem mntmAvailable;
    private JMenuItem mntmUnavailable;
    private JMenuItem mntmCreate;
    private JMenu mnNewMenu_3;
    private JMenuItem mntmNewMenuItem_1;
    private JMenuItem mntmNewMenuItem_2;
    private JMenuItem mntmJoin;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof serverNotification){
        	serverNotification sn = (serverNotification) arg;
        	if(sn.getMessage().equals("LOGIN_FAILED")){
        		showMessageDialog(this, "Login failed. Please try again.",
    					"Error", JOptionPane.ERROR_MESSAGE);
        		java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new LoginGUI().setVisible(true);
                    }
                });
        		setVisible(false);
        		dispose();
        	}
        	else if(sn.getMessage().equals("CONNECTION_FAILED")){
        		showMessageDialog(this, "Failed to connect to server. Please try again.",
    					"Error", JOptionPane.ERROR_MESSAGE);
        		java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                    	setVisible(false);
                		dispose();
                        new LoginGUI().setVisible(true);
                    }
                });
        		
        	}
        }
    }

    @Override
    public void display(String msg) {
        JLabel message;
        if(msg.contains("> ")){
            String[] nameAndMessage = msg.split("> ",2);
            JLabel name = new JLabel("  " + nameAndMessage[0]);
            message = new JLabel("   " + nameAndMessage[1]);
            if(!lastMessageUser.equals(nameAndMessage[0])){
                MessagePanel.add(name);
                lastMessageUser = nameAndMessage[0];
            }
        }else{
            message = new JLabel(" " + msg); 
            message.setForeground(Color.blue);
            lastMessageUser = "";
        }
        try{
            message.setFont(message.getFont().deriveFont(message.getWidth()/2));
            MessagePanel.add(message);
            MessagePanel.revalidate();
            MessagePanel.repaint();
            int height = (int)MessagePanel.getPreferredSize().getHeight();
            Rectangle rect = new Rectangle(0,height,10,10);
            MessagePanel.scrollRectToVisible(rect);
        }catch(Exception e){}
    }
}
