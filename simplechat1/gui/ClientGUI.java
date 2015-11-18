package gui;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import client.ChatClient;
import common.ChatIF;
import gui.blockGUI.BlockUserFrame;
import gui.blockGUI.UnblockUserFrame;
import gui.channelGUI.CreateChannelFrame;
import gui.channelGUI.JoinChannelFrame;
import gui.monitorGUI.MonitorUserFrame;
import gui.paintGUI.ImagePanel;
import gui.paintGUI.PaintFrame;
import gui.statusGUI.ChannelStatusFrame;
import gui.statusGUI.UserStatusFrame;
import server.serverNotification;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joshua Fuller,Alex Shockency
 */
public class ClientGUI extends javax.swing.JFrame implements Observer, ChatIF{
    public ChatClient ch;
    String lastMessageUser;
    /**
     * Creates new form ClientGUI
     */
    public ClientGUI(String name, String password, String hostname, int portNumber) {
    	setTitle("Chat Client");
        try {
        	initComponents();
            setLocationRelativeTo(null);
            ch = new ChatClient(name,password,hostname,portNumber,this);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            lastMessageUser = "";
        } catch (Exception ex) {
            System.exit(-1);
        }
    	
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
        
        mntmStatusUser = new JMenuItem("User");
        mnNewMenu_1.add(mntmStatusUser);
        
        mntmStatusChannel = new JMenuItem("Channel");
        mnNewMenu_1.add(mntmStatusChannel);
        
        mnBlocking = new JMenu("Blocking");
        menuBar.add(mnBlocking);
        
        mntmBlock = new JMenuItem("Block");
        mnBlocking.add(mntmBlock);
        
        mnNewMenu_3 = new JMenu("Unblock");
        mnBlocking.add(mnNewMenu_3);
        
        mntmUnblockUser = new JMenuItem("Unblock User");
        mnNewMenu_3.add(mntmUnblockUser);
        
        mntmUnblockAll = new JMenuItem("Unblock All");
        mnNewMenu_3.add(mntmUnblockAll);
        
        mntmWhoBlocksMe = new JMenuItem("Who Blocks Me");
        mnBlocking.add(mntmWhoBlocksMe);
        
        mntmWhoIBlock = new JMenuItem("Who I Block");
        mnBlocking.add(mntmWhoIBlock);
        
        mnImage = new JMenu("Image");
        menuBar.add(mnImage);
        
        mntmSendImage = new JMenuItem("Send Image");
        mnImage.add(mntmSendImage);
        
        mnMonitor = new JMenu("Monitor");
        menuBar.add(mnMonitor);
        
        mntmMonitorUser = new JMenuItem("User");
        mnMonitor.add(mntmMonitorUser);

        GUITitle = new javax.swing.JLabel();
        MessageInputArea = new javax.swing.JTextField();
        MessageSendButton = new javax.swing.JButton();
        MessageScrollPane = new javax.swing.JScrollPane();
        MessagePanel = new javax.swing.JPanel();
        ChannelLabel = new javax.swing.JLabel();
        ChannelName = new javax.swing.JLabel();

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

        ChannelName.setText("public");
       
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
        
        mntmBlock.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		BlockUserOptionSelected(evt);
        	}
        });
        
        mntmUnblockUser.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		UnblockUserOptionSelected(evt);
        	}
        });
        
        mntmUnblockAll.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		UnblockAllOptionSelected(evt);
        	}
        });
        mntmLogoff.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		LogoffOptionSelected(evt);
        	}
        });
        mntmAvailable.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		AvailableOptionSelected(evt);
        	}
        });
        mntmUnavailable.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		UnavailableOptionSelected(evt);
        	}
        });
        mntmStatusUser.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		StatusUserOptionSelected(evt);
        	}
        });
        mntmStatusUser.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		StatusUserOptionSelected(evt);
        	}
        });
        mntmStatusChannel.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		StatusChannelOptionSelected(evt);
        	}
        });
        mntmSendImage.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		SendImageOptionSelected(evt);
        	}
        });
        mntmWhoBlocksMe.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		WhoBlocksMeOptionSelected(evt);
        	}
        });
        mntmWhoIBlock.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		WhoIBlockOptionSelected(evt);
        	}
        });
        mntmMonitorUser.addActionListener(new java.awt.event.ActionListener(){
        	public void actionPerformed(java.awt.event.ActionEvent evt){
        		MonitorUserOptionSelected(evt);
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
                        )
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
 	   try {
		ch.sendToServer("#leaveChannel");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
    private void BlockUserOptionSelected(ActionEvent evt) {
  	  BlockUserFrame chan = BlockUserFrame.getInstance(this);
         Point p = MouseInfo.getPointerInfo().getLocation();
         int xOffset = (int) p.getX() - 25;
         int yOffset = (int) p.getY() - 25;
         chan.setLocation(xOffset, yOffset);
         chan.setVisible(true);
         chan.setAlwaysOnTop(true);
 	}
    private void UnblockUserOptionSelected(ActionEvent evt) {
    	  UnblockUserFrame chan = UnblockUserFrame.getInstance(this);
           Point p = MouseInfo.getPointerInfo().getLocation();
           int xOffset = (int) p.getX() - 25;
           int yOffset = (int) p.getY() - 25;
           chan.setLocation(xOffset, yOffset);
           chan.setVisible(true);
           chan.setAlwaysOnTop(true);
   	}
    private void UnblockAllOptionSelected(ActionEvent evt) {
		try {
			ch.sendToServer("#unblock");
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
    private void LogoffOptionSelected(ActionEvent evt) {
		try {
			ch.handleCommand("#logoff");
//			showMessageDialog(this, "Login failed. Please try again.",
//					"Error", JOptionPane.ERROR_MESSAGE);
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new LoginGUI().setVisible(true);
				}
			});
			setVisible(false);
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
    private void AvailableOptionSelected(ActionEvent evt) {
		try {
			ch.sendToServer("#available");
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
    private void UnavailableOptionSelected(ActionEvent evt) {
		try {
			ch.sendToServer("#notavailable");
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
    private void StatusUserOptionSelected(ActionEvent evt) {
    	UserStatusFrame chan = UserStatusFrame.getInstance(this);
        Point p = MouseInfo.getPointerInfo().getLocation();
        int xOffset = (int) p.getX() - 25;
        int yOffset = (int) p.getY() - 25;
        chan.setLocation(xOffset, yOffset);
        chan.setVisible(true);
        chan.setAlwaysOnTop(true);
 	}
    private void StatusChannelOptionSelected(ActionEvent evt) {
    	ChannelStatusFrame chan = ChannelStatusFrame.getInstance(this);
        Point p = MouseInfo.getPointerInfo().getLocation();
        int xOffset = (int) p.getX() - 25;
        int yOffset = (int) p.getY() - 25;
        chan.setLocation(xOffset, yOffset);
        chan.setVisible(true);
        chan.setAlwaysOnTop(true);
 	}
    private void SendImageOptionSelected(ActionEvent evt) {
    	PaintFrame paint=new PaintFrame(this);
 	}
    private void WhoBlocksMeOptionSelected(ActionEvent evt) {
    	try {
			ch.sendToServer("#whoblocksme");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
    private void WhoIBlockOptionSelected(ActionEvent evt) {
    	try {
			ch.sendToServer("#whoiblock");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
    private void MonitorUserOptionSelected(ActionEvent evt) {
    	MonitorUserFrame chan = MonitorUserFrame.getInstance(this);
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

   //GEN-LAST:event_ChannelOptionsButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ChannelLabel;
    private javax.swing.JLabel ChannelName;
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
    private JMenuItem mntmLogoff;
    private JMenuItem mntmBlock;
    private JMenu mnBlocking;
    private JMenuItem mntmStatusUser;
    private JMenuItem mntmStatusChannel;
    private JMenu mnNewMenu_2;
    private JMenuItem mntmAvailable;
    private JMenuItem mntmUnavailable;
    private JMenuItem mntmCreate;
    private JMenu mnNewMenu_3;
    private JMenuItem mntmUnblockUser;
    private JMenuItem mntmUnblockAll;
    private JMenuItem mntmJoin;
    private JMenu mnImage;
    private JMenuItem mntmSendImage;
    private JMenuItem mntmWhoBlocksMe;
    private JMenuItem mntmWhoIBlock;
    private JMenu mnMonitor;
    private JMenuItem mntmMonitorUser;
    // End of variables declaration//GEN-END:variables

    @Override
	public void update(Observable o, Object arg) {
		if (arg instanceof serverNotification) {
			serverNotification sn = (serverNotification) arg;
			switch (sn.getMessage()) {
			case "LOGIN_FAILED":
				showMessageDialog(this, "Login failed. Please try again.",
						"Error", JOptionPane.ERROR_MESSAGE);
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						new LoginGUI().setVisible(true);
					}
				});
				setVisible(false);
				dispose();
				break;

			case "CONNECTION_FAILED":
				showMessageDialog(this,
						"Failed to connect to server. Please try again.",
						"Error", JOptionPane.ERROR_MESSAGE);
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						setVisible(false);
						dispose();
						new LoginGUI().setVisible(true);
					}
				});
				break;
			
			case "CHANNEL_CHANGED":
				ChannelName.setText(sn.getInfo());
				break;
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
    
    @Override
    public void display(ImageIcon image){
    	System.out.println("working");
    	JFrame frame = new JFrame();
    	
    	BufferedImage bf = new BufferedImage(
    			image.getIconWidth(),
    			image.getIconHeight(),
    			BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics = bf.createGraphics();
    	image.paintIcon(null, graphics, 0, 0);
    	graphics.dispose();
    	
    	ImagePanel panel = new ImagePanel(bf);
    	
    	frame.setSize(bf.getWidth(), bf.getHeight());
    	frame.getContentPane().add(panel);
    	frame.setLocationRelativeTo(this);
    	frame.setVisible(true);
    	
    	
    }
}
