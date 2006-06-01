/**
    Een Model is een manier om een Map weer te geven. 
    Arne Vansteenkiste
*/
import java.util.*;

public abstract class AbstractModel {

    protected final Interpolator xDouble, yDouble;
    public final int width, height;
    protected int iteration;
    public final double xmin, ymin, xmax, ymax;
    protected final Map map;
    protected Vector listeners = new Vector();
    
    public AbstractModel(Map map, int width, int height, 
                         double xmin, double ymin, double xmax, double ymax){
        this.map = map;
        this.width = width;
        this.height = height;
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        xDouble = new Interpolator(0, xmin, width, xmax);
        yDouble = new Interpolator(height, ymin, 0, ymax);
    }
    
    protected abstract void iterate();
    
    public void reset(){
        
    }
    
    public final void iterate(int n){
        for(int i = 0; i < n; i++)
            iterate();
        fireChanged();
    }
    
    public void runSlowly(){
        for(;;){
            iterate();
            try{
                Thread.currentThread().sleep(1500);
            }
            catch(Exception e){}
        }
    }
    
    public void addListener(ModelListener l){
        listeners.add(l);
    }
    
    public int getIteration(){
        return iteration;
    }
    
    protected final double xNext(double x, double y){
        return map.xNext(x, y);
    }
    
    protected final double yNext(double x, double y){
        return map.yNext(x, y);
    }
    
    public Map getMap(){
        return  map;
    }
    
    public String toString(){
        return map.getClass().getName();
    }
    
    public Interpolator getXInterpolator(){
        return xDouble;
    }
    
    public Interpolator getYInterpolator(){
        return yDouble;
    }
    
    protected void fireChanged(){
        for(int i = 0; i < listeners.size(); i++)
            ((ModelListener)listeners.get(i)).dataChanged();
    }
}
/**
    Een BasinView toont de aantrekkingsgebieden in een bepaalde kleur.
    Arne Vansteenkiste
*/

import java.awt.*;

public class BasinView extends GridView{
    
    private Interpolator xInt, yInt;
                     
    public BasinView(GridModel m){
        super(m);
        colors = new ColorMap1D(m.height);
        xInt = model.getXInterpolator().inverse();
        yInt = model.getYInterpolator().inverse();
    }
    
    public void scaleColors(double factor){
        xInt = xInt.scaleX(factor);
        yInt = yInt.scaleX(factor);
    }
    
    public void paintComponent(Graphics g1){
        final double[][] xData = model.getXData(), 
                         yData = model.getYData();
        final int width = model.width;
        final int height = model.height;
        for(int i = 0; i < width; i++){
            if(Thread.interrupted()) return;
            for(int j = 0; j < height; j++){
                final int x = (int)Math.round(xInt.transf(xData[i][j]));
                final int y = (int)Math.round(yInt.transf(yData[i][j]));
                Color c = colors.get(x, y);
                pixelArray[0] = c.getRed();
                pixelArray[1] = c.getGreen();
                pixelArray[2] = c.getBlue();
                pixelArray[3] = 0xff; //c.getAlpha();
                raster.setPixel(i, j, pixelArray);
            }
        }
        g1.drawImage(image, 0, 0, null);
    }
    
    
    public void paintView(Graphics g){
        //super.paintComponent(g);
        final double[][] xData = model.getXData(), 
                         yData = model.getYData();
        final int width = model.width;
        final int height = model.height;
        for(int i = 0; i < width; i++){
            if(Thread.interrupted()) return;
            for(int j = 0; j < height; j++){
                final int x = (int)Math.round(xInt.transf(xData[i][j]));
                final int y = (int)Math.round(yInt.transf(yData[i][j]));
                g.setColor(colors.get(x, y));
                g.fillRect(i, j, 1, 1);
            }
        }
    }
}
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
/**
    De kat - afbeelding.
    Arne Vansteenkiste
*/
public class Cat extends Map{

     public double xNext(double x, double y){
        return (2*x + y) % 1.0;
    }
    
    public double yNext(double x, double y){
        return (x + y) % 1.0;
    }
}
/**
    Een ColorMap beeld punten af op kleuren.
    Arne Vansteenkiste
*/

import java.awt.Color;

public class ColorMap{
    
    private final Color color;
    
    public ColorMap(){
        this(Color.BLACK);
    }
    
    public ColorMap(Color c){
        this.color = c;
    }
    
    public Color get(int i, int j){
        return color;
    }
}/**
    Een ColorMap1D beeld y-waarden af op kleuren van rood over zwart naar blauw.
    Arne Vansteenkiste
*/

import java.awt.*;

public final class ColorMap1D extends ColorMap{

    private final Color[] map;
    private final Interpolator ip1, ip2;
    private final int width;
     
    public ColorMap1D(int width){
        this.width = width;
        ip1 = new Interpolator(0, 1, width/2, 0);
        ip2 = new Interpolator(width/2, 0, width, 1);
        map = new Color[width]; 
        for(int i = 0; i < width/2; i++)
            map[i] = new Color((float)ip1.transf(i), 0.0f, (float) 0);
        for(int i = width/2; i < width; i++)
            map[i] = new Color((float)0, 0.0f, (float) ip2.transf(i));

    }
    
    public Color get(int j, int i){
        if(i < 0) i = 0;
        else if(i >= width) i = width-1;
        return map[i];
    }
}
/**
    Een ColorMap2D beeld punten af op een kleurcirkel.
    Arne Vansteenkiste
*/
import java.awt.*;

public final class ColorMap2D extends ColorMap{

    private final Color[][] map;
    private final Interpolator xInt, yInt;
    private final int width, height;
     
    public ColorMap2D(int width, int height){
        this.width = width;
        this.height = height;
        xInt = new Interpolator(0, -1, width, 1);
        yInt = new Interpolator(height, -1, 0, 1);
        map = new Color[width][height]; 
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                double x = xInt.transf(i);
                double y = yInt.transf(j);
                double r = Math.sqrt(2) * Math.sqrt(x*x + y*y);
                double b = 1.0;
                if(r > 0.5)
                    b = 1.5 - r;
                if(r > 1.5)
                    b = 0;
                map[i][j] = Color.getHSBColor((float)(Math.atan2(y,x)/2.0/Math.PI),
                                              (float)(r > 0.5? 1: r*2),
                                              (float) b);
        
        }
        map[0][0] = Color.BLACK;
    }
    
    public Color get(int i, int j){
        //i /= 2;
        //j /= 2;
        if(i < 0 || i >= width || j < 0 || j >= height)
            return Color.BLACK;
        else
            return map[i][j];
    }
}
/**
    Complexe getallen
    Arne Vansteenkiste
*/
import java.io.Serializable;

public final class Complex implements Serializable, Cloneable
{
    private double real;
    private double imag;
  
    public Complex(double real, double imag){	
        this.real = real;
        this.imag = imag;
    }

    public Complex(double real){	
        this(real, 0);
    }

    public Complex(Complex other){	
        this(other.real, other.imag);
    }

    public Complex(){
    	this(0, 0);
    }
    
    public double real(){
    	return real;
    }

    public double imag(){
    	return imag;
    }

    public double abs(){	
        return Math.sqrt(real*real + imag*imag);
    }

    public boolean isZero(){
    	return real == 0.0 && imag == 0.0;
    }

    public double arg(){	
        return Math.atan2(imag, real);
    }

    public boolean isReal(){	
        return(imag == 0.0);
    }

    public boolean isImag(){
    	return(real == 0.0);
    }

    public Complex set(double newValue){
        real = newValue;
        imag = 0;
        return this;
    }
    
    public Complex set(double newReal, double newImag){
        real = newReal;
        imag = newImag;
        return this;
    }
    
    public Complex set(Complex other){
        this.real = other.real;
        this.imag = other.imag;
        return this;
    }
    
    
    public boolean equals(Object obj){
	if(obj instanceof Complex){
	    Complex c = (Complex)obj;
	    return real == c.real && imag == c.imag;
	}
	else
	    return false;
    }
    

    public String toString(){	
        String ret = "";
        if(isZero())
            return "0";
        else{
            if(real != 0.0){
                ret += strip(real);
                if(imag > 0)
                    ret += '+';
            }
            if(imag == 1.0)
                ret += 'i';
            else if(imag == -1.0)
                ret += "-i";
            else if(imag != 0.0)
                ret += strip(imag) + 'i';
        }
        return ret;
    }

    private String strip(double number){
        String ret = "" + number;
        if(ret.length() > 2 && ret.substring(ret.length()-2, ret.length()).equals(".0"))
            ret = ret.substring(0, ret.length()-2);
        return ret;
    }
    
    public String toPolarString(){	
        return abs() + "*e^" + arg() + "i";
    }

    public double toDouble(){
        if(imag != 0)
            return Double.NaN;
        else
            return real;
    }
    
    
    public Complex add(Complex c){
        this.real += c.real;
        this.imag += c.imag;
        return this;
    }
    
    public Complex add(double r){
        real += r;
        return this;
    }
  
    public Complex sub(Complex c){
        real -= c.real;
        imag -= c.imag;
        return this;
    }
    
    public Complex sub(double r){
        real -= r;
        return this;
    }
    
    public Complex negate(){	
        real = -real;
        imag = -imag;
        return this;
    }
    
    public Complex conj(){
        imag = -imag;
        return this;
    }

    public Complex multiply(Complex c){
        final double a = real;
        final double b = imag;
        real = a*c.real - b*c.imag;
        imag = a*c.imag + b*c.real;
        return this;
    }
    
    public Complex multiply(double d){
        real *= d;
        imag *= d;
        return this;
    }

    public Complex inv(){
        double n = real*real + imag*imag;
        real =  real/n;
        imag = -imag/n;
        return this;
    }    
}
/**
    Menubar met controls
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.event.*;

public class Controller extends JFrame{

    private AbstractModel model;
    private boolean running;
    private int iterations = 1;
    
    private Action reset = new AbstractAction("reset"){
        public void actionPerformed(ActionEvent e){
            model.reset();
        }
    };

    private Action iterate1 = new AbstractAction("1 iteratie"){
        public void actionPerformed(ActionEvent e){
            model.iterate(1);
        }
    };
    private Action iterate = new AbstractAction("Aantal iteraties"){
        public void actionPerformed(ActionEvent e){
            model.iterate(iterations);
        }
    };
    private Action startStop = new AbstractAction("Start / Stop"){
        public void actionPerformed(ActionEvent e){
            start_stop();
        }
    };
    private class SetIter extends AbstractAction{
        private int n;
        public SetIter(int n){
            super(n + " iteraties");
            this.n = n;
        }
        public void actionPerformed(ActionEvent e){
            iterations = n;
        }
    }
    public Controller(AbstractModel model){
        this.model = model;
    }
    
    public void start_stop(){
        running = !running;
        if(running){
            Run run = new Run();
            run.setPriority(Thread.MIN_PRIORITY);
            run.start();
        }
    }

    private class Run extends Thread{
        public void run(){
            while(running){
                model.iterate(iterations);
                //try{
                    Thread.currentThread().yield();
                /*}
                catch(InterruptedException e){}*/
            }
        }
    }

    public JMenuBar createMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu iter = new JMenu("Itereer");
        iter.add(new JMenuItem(iterate1));
        iter.add(new JMenuItem(iterate));
        iter.add(new JMenuItem(startStop));
        iter.add(new JMenuItem(reset));
        bar.add(iter);
        JMenu iters = new JMenu("Aantal iteraties");
        for(int i = 0; i < 11; i++){
            iters.add(new JMenuItem(new SetIter((int)Math.pow(2, i))));
        }
        bar.add(iters);
        return bar;
    } 
}
/**
    Dynamische Systemen
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.*;
import be.barnex.ajuin.Ajuin;

public class DynSys {
    
    public static void main (String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        
        /*
            1D-maps
        */
        
        Lyapunov1D.skip = -5;
        
        //  (1)     FlowView Logistieke afbeelding -> bifurcatiediagram
        /*
        final int WIDTH = 700, HEIGHT = 512;
        Map map = new Logistic();
        GridModel model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        Controller c = new Controller(model);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        //*/
        
        //  (2)     FlowView Tent-afbeelding -> bifurcatiediagram.
        /*
        final int WIDTH = 700, HEIGHT = 512;
        Map map = new Tent();
        GridModel model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 1, 1);
        Controller c = new Controller(model);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        //*/
        
        // (3)      Flow + Lyapunov Logistieke + introductie kleuren.
        /*
        final int WIDTH = 500, HEIGHT = 400;
        Map map = new Logistic();
        GridModel model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        Controller c = new Controller(model);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        Lyapunov1D lyap = new Lyapunov1D(model);
        new FlowView(lyap).showFrame(c);
        //*/
        
        // (4)  Flow + Lyapunov + Basin Logistieke
        /*
        final int WIDTH = 500, HEIGHT = 300;
        ColorMap map1d = new ColorMap1D(HEIGHT);
        Map map = new Logistic();
        GridModel model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        Controller c = new Controller(model);
        FlowView flow = new FlowView(model);
        flow.setColorMap(map1d);
        flow.showFrame(c);
        Lyapunov1D lyap = new Lyapunov1D(model);
        new FlowView(lyap).showFrame(c);
        BasinView basin = new BasinView(model);
        basin.setColorMap(map1d);
        basin.showFrame(c);
        //*/
        
    
        /*
            2D maps
        */
        
        // (5)      Henon
        /*
        final int WIDTH = 500, HEIGHT = 500;
        Map map = new Henon(1.5, -0.8);
        final double xmin = -2, ymin = -2, xmax = 2, ymax = 2;
        GridModel model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        Controller c = new Controller(model);
        ColorMap map2d = new ColorMap2D(WIDTH, HEIGHT);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        BasinView basin = new BasinView(model);
        basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        basin.showFrame(c);
        //*/
        
        // (6)      Kat
        /*
        final int WIDTH = 600, HEIGHT = 600;
        Map map = new Cat();
        final double xmin = 0, ymin = 0, xmax = 1, ymax = 1;
        GridModel model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        Controller c = new Controller(model);
        ColorMap map2d = new ColorMap2D(WIDTH, HEIGHT);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        BasinView basin = new BasinView(model);
        //basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        basin.showFrame(c);
        //*/
        
        // (7)      Julia
        /*
        final int WIDTH = 600, HEIGHT = 600;
        //Map map = new Julia(0.31,0.04);
        //Map map = new Julia(-0.12, 0.74);
        //Map map = new Julia(0, 1);
        //Map map = new Julia(-0.194, 0.6557);
        //Map map = new Julia(-0.74543, 0.11301);
        //Map map = new Julia(-1.25, 0);
        //Map map = new Julia(-0.39054, -0.58679);
        //Map map = new Julia(0.11031, -0.67037);
        //Map map = new Julia(-0.481762, -0.531657);
        //Map map = new Julia(-0.11, 0.6557);
        final double xmin = -1.5, ymin = -1.5, xmax = 1.5, ymax = 1.5;
        GridModel model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        Controller c = new Controller(model);
        ColorMap map2d = new ColorMap2D(WIDTH, HEIGHT);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        BasinView basin = new BasinView(model);
        basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        basin.showFrame(c);
        //*/
        
        // (8)      Newton
        /*
        final int WIDTH = 500, HEIGHT = 500;
        Map map = new NewtonRaphson(1, 0);
        final double xmin = -1.5, ymin = -1.5, xmax = 1.5, ymax = 1.5;
        GridModel model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        Controller c = new Controller(model);
        ColorMap map2d = new ColorMap2D(WIDTH, HEIGHT);
        FlowView flow = new FlowView(model);
        flow.showFrame(c);
        BasinView basin = new BasinView(model);
        basin.scaleColors(2.0);
        basin.setColorMap(map2d);
        basin.showFrame(c);
        //*/
    }
}
/**
    Een FlowView geeft een Map weer door een grid van initiele punten te itereren
    en elk punt te volgen.
    Arne Vansteenkiste
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class FlowView extends GridView{

    Graphics gImg;
    
    public FlowView(GridModel m){
        super(m);
        gImg = image.getGraphics();
        colors = new ColorMap(new Color(0, 0, 0, 80));
    }
    
    
    public void paintView(Graphics g){
        //super.paintView(g);
        paintOver(g);
    }
    
    public void paintOver(Graphics g){
        gImg.setColor(Color.WHITE);
        gImg.fillRect(0, 0, w, h);
        final double[][] xData = model.getXData(), 
                         yData = model.getYData();
        Interpolator xInt = model.getXInterpolator().inverse(),
                     yInt = model.getYInterpolator().inverse();
        for(int i = 0; i < xData.length; i++){
            if(Thread.interrupted()) return;
            for(int j = 0; j < xData[0].length; j++){
                final int x = (int)Math.round(xInt.transf(xData[i][j]));
                final int y = (int)Math.round(yInt.transf(yData[i][j]));
                if(x >= 0 && y >= 0 && x < w && y < h){
                    Color c = colors.get(x, y);
                    pixelArray[0] = c.getRed();
                    pixelArray[1] = c.getGreen();
                    pixelArray[2] = c.getBlue();
                    pixelArray[3] = c.getAlpha();
                    raster.setPixel(x, y, pixelArray);
                }
            }
        }
        g.drawImage(image, 0, 0, null);
    }
}
/**
    Een GridModel geeft een Map weer door een grid van initiele punten te itereren.
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GridModel extends AbstractModel{
    
    protected final double[][] xData, yData;
        
    public GridModel(Map map, int width, int height, 
                 double xmin, double ymin, double xmax, double ymax){
        super(map, width, height, xmin, ymin, xmax, ymax);
        xData = new double[width][height];
        yData = new double[width][height];
        initData();
    }
    
    public final void iterate(){
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                final double x = xData[i][j], y = yData[i][j];
                xData[i][j] = xNext(x, y);
                yData[i][j] = yNext(x, y);
            }
        iteration++;
        //fireChanged();
    }
    
    public final double[][] getXData(){
        return xData;
    }
    
    public final double[][] getYData(){
        return yData;
    }
    
    public void reset(){
        System.out.println("reset");
        initData();
        fireChanged();
    }
    
    public void initData(){
        for(int i = 0; i < width; i++){
            final double x = xDouble.transf(i);
            for(int j = 0; j < height; j++){
                final double y = yDouble.transf(j);
                xData[i][j] = x;
                yData[i][j] = y;
            }
        }
        fireChanged();
    }
}
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
/**
    De Henon afbeelding.
    Arne Vansteenkiste
*/
public final class Henon extends Map{
    
    public Henon(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public double xNext(double x, double y){
        return a - x*x + b*y;
    }
    
    public double yNext(double x, double y){
        return x;
    }

}
/**
    Lineaire interpolator voor het omzetten van coordinaten.
    Arne Vansteenkiste
*/
public final class Interpolator {
    
    private final double x1, y1, x2, y2;
    
    public Interpolator(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public final double transf(double x){
        return y1 + (y2 - y1) / (x2 - x1) * (x-x1);
    }
    
    public Interpolator inverse(){
        return new Interpolator(y1, x1, y2, x2);
    }
    
    public Interpolator scaleX(double factor){
        double c = (x1 + x2) / 2;
        return new Interpolator(x1 * factor - c , y1, x2 * factor - c, y2);
    }
}
/**
    De Julia afbeelding.
    Arne Vansteenkiste
*/

public final class Julia extends Map{
    
    public Julia(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public double xNext(double x, double y){
        return x*x - y*y + a;
    }
    
    public double yNext(double x, double y){
        return 2*x*y + b;
    }
}
/**
    De logistieke afbeelding als 2D afbeelding. x is de paramter, y is de waarde.
    Arne Vansteenkiste
*/
public final class Logistic extends Map{
    
    public double xNext(double x, double y){
        return x;
    }
    
    public double yNext(double x, double y){
        return logistic(x, y);
    }
    
    public double diffY(double x, double y){
        return x*(1-2*y);
    }
    
    private final double logistic(double a, double x){
        return a*x*(1-x);
    }
}

public class Lyapunov extends GridModel implements ModelListener{

    private GridModel model;
    private double[][] prev;
    private double[][] prev2;
    private int xy;
    public static final int X = 0, Y = 1;
    
    public Lyapunov(GridModel m, int xy){
        super(null, m.width, m.height, m.xmin, 0.0, m.xmax, 2.0);
        this.model = m;
        this.xy = xy;
        prev = new double[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                if(xy == X){
                    prev[i][j] = Double.NaN;
                    yData[i][j] = yDouble.transf(j);
                }
                else{
                    prev[i][j] = Double.NaN;
                    xData[i][j] = xDouble.transf(i);
                }
            }
        m.addListener(this);
    }
    
    public void dataChanged(){
        final double[][] currX = model.getXData();
        final double[][] currY = model.getYData();
        
        if(xy == X){
            for(int i = 0; i < width; i++)
                for(int j = 0; j < height; j++){
                    xData[i][j] = currX[i][j] / prev[i][j];
                    prev[i][j] = currX[i][j];
                }
        }
        else{
            for(int i = 0; i < width; i++)
                for(int j = 0; j < height; j++){
                    yData[i][j] = currY[i][j] / prev[i][j];
                    prev[i][j] = currY[i][j];
                }
        }
        fireChanged();
    }
    
    public String toString(){
        return getClass().getName() + ": " + model;
    }
}
/**
    Een Lyapunov-model geeft de Lyapunov getallen van alle banen in een GridModel.
    Arne Vansteenkiste
*/
public class Lyapunov1D extends GridModel implements ModelListener{

    protected final GridModel model;
    protected final double[][] lyap;
    protected int k;
    public static int skip = 0;
    
    public Lyapunov1D(GridModel m){
        super(null, m.width, m.height, m.xmin, 0.0, m.xmax, 3.0);
        this.model = m;
        lyap = new double[m.width][m.height];
        for(int i = 0; i < m.width; i++)
            for(int j = 0; j < m.height; j++){
                xData[i][j] = m.getXData()[i][j];
                lyap[i][j] = 1.0;
                }
        m.addListener(this);
    }
    
    public void dataChanged(){
        skip++;
        if(skip < 0)
            return;
        k++;
        final double[][] xdata = model.getXData();
        final double[][] ydata = model.getYData();
        for(int i = 0; i < model.width; i++)
            for(int j = 0; j < model.height; j++){
                final double x = xdata[i][j];
                final double y = ydata[i][j];
                lyap[i][j] *= (Math.abs(model.getMap().diffY(x, y)));
            }
        for(int i = 0; i < model.width; i++)
            for(int j = 0; j < model.height; j++){
                yData[i][j] = Math.pow(lyap[i][j],1.0/k);
            }
        fireChanged();
    }
    
    public String toString(){
        return "Lyapunov: " + model;
    }
}/**
 Main class
 Arne Vansteenkiste
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main {
    
    static JFrame frame;
    
    static JTextField width = new JTextField("512");
    static JTextField height = new JTextField("512");
    static JTextField xMin = new JTextField("0.0");
    static JTextField yMin = new JTextField("0.0");
    static JTextField xMax = new JTextField("1.0");
    static JTextField yMax = new JTextField("1.0"); 
    static JTextField aText = new JTextField("0.0");
    static JTextField bText = new JTextField("0.0");
    static JTextField zoom = new JTextField("1.0");
    static  Map[] maps = new Map[]{
        new Logistic(), 
        new Tent(), 
        new Sinus(), 
        new NewtonRaphson(),
        new Henon(1.4, -0.3),
        new Julia(0.31,0.04),
        new Cat(),
        new SkinnyBaker()};
    
    static JComboBox mapCombo = new JComboBox(maps);
    static JComboBox colorCombo = new JComboBox(new String[]{"1D (blauw-zwart-rood)", 
        "2D (kleurcirkel)"});
    
    static JCheckBox flow = new JCheckBox("FlowView", true);
    static JCheckBox lyap = new JCheckBox("Lyapunov (1D)", false);
    static JCheckBox basin = new JCheckBox("BasinView", true);
    static JButton show = new JButton ("       show        ");
    
    public static void main(String[] args){
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints (); 
        gbc.insets = new Insets (3, 5, 3, 5); 
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        
        gbc.gridx = 0; gbc.gridy = 0;
        pane.add(new JLabel("width (pixels)"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        pane.add(width , gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        pane.add(new JLabel("height (pixels)") , gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        pane.add(height , gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        pane.add(new JLabel("xmin") , gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        pane.add(xMin , gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        pane.add(new JLabel("ymin") , gbc);
        gbc.gridx = 3; gbc.gridy = 2;
        pane.add(yMin , gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        pane.add(new JLabel("xmax") , gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        pane.add(xMax , gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        pane.add(new JLabel("ymax") , gbc);
        gbc.gridx = 3; gbc.gridy = 3;
        pane.add(yMax , gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        pane.add(new JLabel("map "), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        pane.add(mapCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        pane.add(new JLabel("color "), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        pane.add(colorCombo, gbc);
        
        gbc.gridx = 2; gbc.gridy = 4;
        pane.add(new JLabel("a "), gbc);
        gbc.gridx = 3; gbc.gridy = 4;
        pane.add(aText, gbc);
        
        gbc.gridx = 2; gbc.gridy = 5;
        pane.add(new JLabel("b "), gbc);
        gbc.gridx = 3; gbc.gridy = 5;
        pane.add(bText, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        pane.add(flow, gbc);
        
        gbc.gridx = 1; gbc.gridy = 6;
        pane.add(lyap, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        pane.add(basin, gbc);
        
        gbc.gridx = 2; gbc.gridy = 6;
        pane.add(new JLabel("color zoom:"), gbc);
        gbc.gridx = 3; gbc.gridy = 6;
        pane.add(zoom, gbc);
        
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.gridx = 2; gbc.gridy = 7;
        pane.add(show, gbc);
        
        show.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                show();
            }
        });
        
        frame = new JFrame("DynSys (c) Arne Vansteenkiste");
        frame.setContentPane(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 280);
        frame.show();
    }
    
    public static void show(){
        try{
            int w = Integer.parseInt(width.getText());
            int h = Integer.parseInt(height.getText());
            double xmin = Double.parseDouble(xMin.getText());
            double ymin = Double.parseDouble(yMin.getText());
            double xmax = Double.parseDouble(xMax.getText());
            double ymax = Double.parseDouble(yMax.getText());
            double a = Double.parseDouble(aText.getText());
            double b = Double.parseDouble(bText.getText());
            double z = Double.parseDouble(zoom.getText());

            Map map = maps[mapCombo.getSelectedIndex()].clone(a, b);
            ColorMap col;
            if(colorCombo.getSelectedIndex() == 0)
                col = new ColorMap1D(h);
            else
                col = new ColorMap2D(w, h);
            
            GridModel model = new GridModel(map, w, h, xmin, ymin, xmax, ymax);
            Controller c = new Controller(model);
            
            if(flow.isSelected()){
                FlowView flow = new FlowView(model);
                flow.showFrame(c);
            }
            if(basin.isSelected()){
                BasinView basin = new BasinView(model);
                basin.scaleColors(z);
                basin.setColorMap(col);
                basin.showFrame(c);
            }
            if(lyap.isSelected()){
                GridView lyap = new FlowView(new Lyapunov1D(model));
                lyap.showFrame(c);
            }
        }
        catch(Throwable t){
            JOptionPane.showMessageDialog(frame, t, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
/**
    Een Map stelt een willekeurige (2D) afbeelding voor. De abstracte methodes
    xNext en yNext geven de nieuwe x en y na een iteratie.
    Arne Vansteenkiste
*/
public abstract class Map implements Cloneable{
    
    protected double a, b;
    
    public abstract double xNext(double x, double y);
    public abstract double yNext(double x, double y);
    
    private static final double H = 1.0/131072.0;
    
    public double diffY(double x, double y){
        return (yNext(x, y+H) - yNext(x, y-H)) / (2.0*H);
    }
    
    public void setParams(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public Map clone(double a, double b){
        try{
            Map map = (Map)clone();
            map.setParams(a, b);
            return map;
        }
        catch(CloneNotSupportedException e){
            return null;
        }
    }
    
    public String toString(){
        return getClass().getName();// + "(" + a + ", " +  b + ")";
    }
}
/**
    ModelListener
    Arne Vansteenkiste
*/
public interface ModelListener {

    public void dataChanged();
}
/**
    MultiView kan gebruikt worden om meerdere views boven elkaar af te beelden.
    Arne Vansteenkiste
*/

import java.awt.*;

public class MultiView extends GridView{

    private GridView view1, view2;
    
    public MultiView(GridView view1, GridView view2){
        super(view1.model);
        view1.model.addListener(this);
        this.view1 = view1;
        this.view2 = view2;
    }
    
    public void paintComponent(Graphics g){
        view1.paintView(g);
        view2.paintView(g);
    }
    
    public void modelChanged(){
        repaint();
    }
}
/**
    Methode van Newton voor het vinden van wortels.
    Arne Vansteenkiste
*/

public class NewtonRaphson extends Map{

    private final Complex z = new Complex(), z2 = new Complex(), z3 = new Complex(), 
    z4 = new Complex(), z5 = new Complex();
    private final Complex n = new Complex();
    private final Complex c = new Complex();
    
    public NewtonRaphson(double a, double b){
        setParams(a, b);
        
    }
    
    public NewtonRaphson(){
        this(1, 0);
    }
    
    public void setParams(double a, double b){
        c.set(a, b);
    }
    
    public double xNext(double x, double y){
        x5_1(x, y);
        return n.real();
    }

    public double yNext(double x, double y){
        return n.imag();
    }
    
    //wortels van x^3 - c;
    private void x3_1(double x, double y){
        z.set(x, y);
        z2.set(z).multiply(z);
        z3.set(z2).multiply(z);
        n.set( z .sub ( z3.sub(c) .multiply ( z2.multiply(3).inv() ) ) );

    }
    
    //wortels van x^5 - c;
    private void x5_1(double x, double y){
        z.set(x, y);
        z2.set(z).multiply(z);
        z4.set(z2).multiply(z2);
        z5.set(z4).multiply(z);
        n.set( z .sub ( z5.sub(c) .multiply ( z4.multiply(5).inv() ) ) );
    }
}
/**
    Een OrbitModel geeft een Map weer door de baan van 1 punt te tonen.
    Arne Vansteenkiste
*/
public class OrbitModel extends AbstractModel{

    private double x, y;
    
    public OrbitModel(Map map, int width, int height, 
                         double xmin, double ymin, double xmax, double ymax){
        super(map, width, height, xmin, ymin, xmax, ymax);
    }
    
    protected void iterate(){
        double bufX = x, bufY = y;
        x = map.xNext(bufX, bufY);
        y = map.yNext(bufX, bufY);
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void setData(double x, double y){
        this.x = x;
        this.y = y;
        iteration = 0;
        fireChanged();
    }
}
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
/**
    Harmonisce oscillator afbeelding.
    Arne Vansteenkiste
*/
public final class Oscillator extends Map{
    
    private double freq, damp;
    
    public Oscillator(double freq, double damp){
        this.freq = freq;
        this.damp = damp;
    }
    
    public double xNext(double x, double y){
        double phase = Math.atan2(y, x);
        double r = Math.sqrt(x*x + y*y);
        phase += freq;
        r *= damp;
        return r*Math.cos(phase);
    }
    
    public double yNext(double x, double y){
        double phase = Math.atan2(y, x);
        double r = Math.sqrt(x*x + y*y);
        phase += freq;
        r *= damp;
        return r*Math.sin(phase);
    }
}
/**
    Een sinus-afbeelding die zeer goed lijkt op de logistieke.
    Arne Vansteenkiste
*/
public final class Sinus extends Map{

    public double xNext(double x, double y){
        return x;
    }
    
    public double yNext(double x, double y){
        return x*Math.sin(y*Math.PI);
    }
}
/**
    De bakkers - afbeelding.
    Arne Vansteenkiste
*/

public class SkinnyBaker extends Map{

    public double xNext(double x, double y){
        double ynext = y * 2.0;
        if(ynext > 1.0)
            return x / 3.0 + 2.0/3.0;
        else
            return x / 3.0;
    }
    
    public double yNext(double x, double y){
        return  (y * 2.0) % 1.0;
    }
}
/**
    De tent-afbeelding als 2D afbeelding. x is de paramter, y is de waarde.
    Arne Vansteenkiste
*/
public final class Tent extends Map{
    
    public double xNext(double x, double y){
        return x;
    }
    
    public double yNext(double x, double y){
        return tent(x, y);
    }
    
    public double diffY(double x, double y){
        return 2*x;
    }
    
    private double tent(double x, double y){
        if(y < 0.5)
            return 2*y*x;
        else
            return (-2*y+2)*x;
    }
}
