/**
    GridView toont een GridModel van een afbeelding.
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public abstract class GridView extends JPanel implements ModelListener{

    public final GridModel model;
    public int paintEveryIteration = 1;
    protected ColorMap colors = new ColorMap();
    protected BufferedImage image;
    protected WritableRaster raster;
    protected int[] pixelArray = new int[4];
    protected final int w, h;
    
    public GridView(GridModel model){
        this.model = model;
        w = model.width;
        h = model.height;
        model.addListener(this);
        setSize(model.width, model.height);
        setBackground(Color.WHITE);
        GraphicsConfiguration gConf = GraphicsEnvironment.getLocalGraphicsEnvironment().
            getDefaultScreenDevice().getDefaultConfiguration();
        image = gConf.createCompatibleImage(w, h);
        raster = image.getRaster();
    }
    
    public void setColorMap(ColorMap m){
        this.colors = m;
    }
    
    public void dataChanged(){
        if(model.getIteration() % paintEveryIteration == 0)
            paintImmediately(0, 0, model.width, model.height);
    }
    
    public void paintComponent(Graphics g1){
        //super.paintComponent(g1);
        paintView(g1);
    }
    
    public void paintView(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, model.width, model.height);
    }
    
    public JFrame showFrame(Controller c){
        final JFrame frame = new JFrame(toString());
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(this);
        frame.setResizable(false);
        frame.setJMenuBar(c.createMenuBar());
        frame.setSize(model.width, model.height+22);
        frame.show();
        frame.getContentPane().addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                frame.setTitle("" + model.getXInterpolator().transf(e.getX()) + ", " 
                                  + model.getYInterpolator().transf(e.getY()));
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }
    
    public String toString(){
        return getClass().getName() + ": " + model;
    }
}
