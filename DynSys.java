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
