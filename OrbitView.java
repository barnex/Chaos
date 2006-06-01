/**
    OrbitView toont de baan van een OrbitModel.
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class OrbitView extends JPanel implements ModelListener{

    protected OrbitModel model;
    protected ColorMap colors = new ColorMap();
    private final BufferedImage img;
    private final Graphics g;
    private Graphics screen;
    private Interpolator xInt, yInt;
    private int persistence, counter;
    private final Color BLEND = new Color(255, 255, 255, 20);
    
    public OrbitView(OrbitModel m, int persistence){
        this.model = m;
        this.persistence = persistence;
        model.addListener(this);
        setSize(model.width, model.height);
        setBackground(Color.WHITE);
        img = new BufferedImage(model.width, model.height, BufferedImage.TYPE_3BYTE_BGR);
        g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, model.width, model.height);
        xInt = model.getXInterpolator().inverse();
        yInt = model.getYInterpolator().inverse();
        g.setColor(new Color(0, 0, 100, 100));
        screen = getGraphics();
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                model.setData(model.getXInterpolator().transf(e.getX()),
                              model.getYInterpolator().transf(e.getY()));
            }
        });
    }
    
    public OrbitView(OrbitModel model){
        this(model, -1);
    }
    
    public void setColorMap(ColorMap m){
        this.colors = m;
    }
    
    public void dataChanged(){
        paintView(g);
        if(screen == null)
            screen = getGraphics();
        paintView(screen);
        counter++;
        if(counter == persistence){
            counter = 0;
            g.setColor(BLEND);
            g.fillRect(0, 0, model.width, model.height);
            screen.setColor(BLEND);
            screen.fillRect(0, 0, model.width, model.height);
            screen.setColor(Color.BLACK);
        }
    }
    
    public void paintComponent(Graphics g1){
        g1.drawImage(img, 0, 0, null);
        //paintView(g1);
    }
    
    public void paintView(Graphics g1){
        final int x = (int)Math.round(xInt.transf(model.getX()));
        final int y = (int)Math.round(yInt.transf(model.getY()));
        g1.setColor(Color.BLACK);
        g1.fillRect(x, y, 1, 1);
    }
    
    public JFrame showFrame(Controller c){
        JFrame frame = new JFrame(toString());
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(this);
        Insets rand = frame.getInsets();
        frame.setSize(model.width + rand.left + rand.right, 
                      model.height + rand.top + rand.bottom);
        frame.setResizable(false);
        frame.setJMenuBar(c.createMenuBar());
        frame.show();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
    
    public String toString(){
        return getClass().getName() + ": " + model;
    }
}
