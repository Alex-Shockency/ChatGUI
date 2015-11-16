package gui;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PaintPanel extends JPanel {
  private static final long serialVersionUID = 4856815551206358400L;

  public Image getImage(){
    return canvas;
  }
  
  private class drawAdapter extends MouseAdapter{
    @Override
    public void mouseDragged(MouseEvent e){
      paint(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e){
      paint(e);
    }
    
    @Override
    public void mouseExited(MouseEvent e){
      repaint();
    }
    
    public void mouseMoved(MouseEvent e){
      repaint();
    }
    
    public void paint(MouseEvent e){
      Graphics2D canvasGraphics = canvas.createGraphics();
      canvasGraphics.setColor(drawColor);
      int x = e.getX() - (radius/2);
      int y = e.getY() - (radius/2);
      canvasGraphics.fillOval(x, y, radius, radius);
      repaint();
    }
  }
  
  private BufferedImage canvas;
  private Color drawColor;
  public int radius = 10;
  
  public void initialize(){
    drawColor = Color.black;
    int width = (int) getSize().getWidth();
    int height = (int) getSize().getHeight();
    canvas = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    Graphics2D canvasGraphics = canvas.createGraphics();
    canvasGraphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    drawAdapter pencil = new drawAdapter();
    this.addMouseListener(pencil);
    this.addMouseMotionListener(pencil);
    this.setCursor(
        this.getToolkit().createCustomCursor(
            new BufferedImage(
                1,
                1,
                BufferedImage.TYPE_INT_ARGB),
            new Point(0,0),
            "blank"));
  }
  
  public void setColor(Color color){
    drawColor = color;
  }
  
  public void setRadius(int radius){
    this.radius = radius;
  }

  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(canvas, 0, 0, null);
      Point q = this.getMousePosition();
      if(q != null)
        g.drawOval(q.x-radius/2, q.y-radius/2, radius, radius);
      
  }

}