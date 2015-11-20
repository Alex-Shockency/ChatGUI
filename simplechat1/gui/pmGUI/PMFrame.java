package gui.pmGUI;

import gui.ClientGUI;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joshua
 */
public class PMFrame extends javax.swing.JFrame {
    private static PMFrame instance = null;
    private static ClientGUI parent;
    private JTextField textField_1;
    private JTextField textField_3;
    /**
     * Creates new form ChannelFrame
     */
    private PMFrame() {
        initComponents();
    }
    
    public static PMFrame getInstance(ClientGUI parent){
        if(instance == null){
            instance = new PMFrame();
            PMFrame.parent = parent;
        }
        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        
        JLabel label_1 = new JLabel("Message: ");
        panel.add(label_1);
        
        textField_3 = new JTextField();
        textField_3.setColumns(38);
        panel.add(textField_3);
        
        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1);
        
        JLabel label = new JLabel("Username: ");
        panel_1.add(label);
        
        textField_1 = new JTextField();
        textField_1.setColumns(23);
        panel_1.add(textField_1);
        
        JButton button = new JButton("Submit");
        textField_3.addKeyListener(new submitListener());
        textField_1.addKeyListener(new submitListener());
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					parent.ch.sendToServer("#pm "+textField_1.getText()+" "+textField_3.getText());
					instance=null;
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
        	}
        });
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        panel_1.add(button);
        
        JButton button_1 = new JButton("Cancel");
        button_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		instance=null;
        		dispose();
        	}
        });
        button_1.setVerticalAlignment(SwingConstants.BOTTOM);
        panel_1.add(button_1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    class submitListener extends KeyAdapter {
    	@Override
    	public void keyPressed(KeyEvent e) {
    		if(e.getKeyCode()==KeyEvent.VK_ENTER){
    			try {
					parent.ch.sendToServer("#pm "+textField_1.getText()+" "+textField_3.getText());
					instance = null;
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		}
    	}
    }
}
