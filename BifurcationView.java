/**
    BifurcationView is een FlowView die de eerste "skip" iteraties overslaat en dan
    de volgende iteraties boven elkaar tekent.
*/
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class BifurcationView extends FlowView{

    private int skip = 1000;
    private final BufferedImage img;
    private final Graphics g;
    
    public BifurcationView(GridModel m, int skip){
        super(m);
        this.skip = skip;
        img = new BufferedImage(m.width, m.height, BufferedImage.TYPE_3BYTE_BGR);
        g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, m.width, m.height);
        setColorMap(new ColorMap(new Color(0, 0, 0, 10)));
    }
    
    public BifurcationView(GridModel m){
        this(m, 1000);
    }
    
    public void dataChanged(){
        if(model.getIteration() > skip){
            super.paintOver(g);
            repaint();
        }
    }
    
    public void paintComponent(Graphics g){
        g.drawImage(img, 0, 0, null);
    }
}
