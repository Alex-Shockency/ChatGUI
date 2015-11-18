package gui.paintGUI;

import gui.ClientGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintFrame extends JFrame{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1433710136495154587L;
  private PaintPanel paint;
  private ClientGUI parent;
  
  public Image getImage(){
    return paint.getImage();
  }
  
  public PaintFrame(final ClientGUI parent) {
    super();
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }catch(Exception e){}
    this.parent = parent;
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

    paint = new PaintPanel();
    paint.setPreferredSize(new Dimension(500, 500));
    this.add(paint);
    final JColorChooser choose = new JColorChooser();
    choose.setColor(Color.black);
    choose.getSelectionModel().addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent arg0) {
        paint.setColor(choose.getColor());

      }

    });
    this.add(choose.getChooserPanels()[1]);
    final JSlider slide = new JSlider(1, 100);
    slide.setValue(paint.radius);
    slide.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        paint.setRadius(slide.getValue());

      }

    });
    this.add(slide);
    
    JPanel buttonGroup = new JPanel();
    buttonGroup.setLayout(new BoxLayout(buttonGroup,BoxLayout.X_AXIS));
    JButton clearButton = new JButton("Clear");
    clearButton.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			paint.clear();
			
		}
    	
    });
    buttonGroup.add(clearButton);
    JButton sendButton = new JButton("Send");
    sendButton.addActionListener(new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				ImageIcon image=new ImageIcon(paint.getImage());
				parent.ch.sendToServer(image);
				dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
    	
    });
    buttonGroup.add(sendButton);
    this.add(buttonGroup);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    paint.initialize();
  }
  
  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){

      @Override
      public void run() {
        new PaintFrame(null).setVisible(true);
        
      }
      
    });
  }
}