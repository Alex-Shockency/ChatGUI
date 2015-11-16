package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
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
  private JFrame parent;
  
  public Image getImage(){
    return paint.getImage();
  }
  
  public PaintFrame(JFrame parent) {
    super();
    try{
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
    }catch(Exception e){}
    this.parent = parent;
    this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

    paint = new PaintPanel();
    paint.setPreferredSize(new Dimension(500, 500));
    this.add(paint);
    JColorChooser choose = new JColorChooser();
    choose.setColor(Color.black);
    choose.getSelectionModel().addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent arg0) {
        paint.setColor(choose.getColor());

      }

    });
    this.add(choose.getChooserPanels()[1]);
    JSlider slide = new JSlider(1, 100);
    slide.setValue(paint.radius);
    slide.addChangeListener(new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        paint.setRadius(slide.getValue());

      }

    });
    this.add(slide);
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
