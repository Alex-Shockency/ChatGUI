package gui.paintGUI;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{

  private BufferedImage b;
  
  public ImagePanel(BufferedImage b){
    this.b = b;
  }
  
  @Override
  protected void paintComponent(Graphics g){
    super.paintComponent(g);
    g.drawImage(b, 0, 0, null);
  }
  
}
