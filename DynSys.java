/**
Dynamische Systemen
 Arne Vansteenkiste
 */
import javax.swing.*;
import java.awt.*;
import be.barnex.ajuin.Ajuin;
import be.ac.rug.twi.util.*;
import javax.swing.*;

public class DynSys {
    
    public static void main (String args[]) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        
        /*
         1D-maps
         */
        
        Lyapunov1D.skip = -5;
        
        //  (1)     FlowView Logistieke afbeelding -> bifurcatiediagram
        ////*
        int WIDTH = 700; int HEIGHT = 512;
        Map map = new Logistic();
        GridModel model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        Controller c = new Controller(model);
        FlowView flow = new FlowView(model);
        JFrame frame = flow.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("tent");
        
        //  (2)     FlowView Tent-afbeelding -> bifurcatiediagram.
        ///*
        //final int
        WIDTH = 700; HEIGHT = 512;
        map = new Tent();
        model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 1, 1);
        c = new Controller(model);
        flow = new FlowView(model);
        frame = flow.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("lyap");
        
        // (3)      Flow + Lyapunov Logistieke + introductie kleuren.
        ///*
        WIDTH = 500; HEIGHT = 400;
        map = new Logistic();
        model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        c = new Controller(model);
        flow = new FlowView(model);
        frame = flow.showFrame(c);
        Lyapunov1D lyap = new Lyapunov1D(model);
        JFrame frame2 = new FlowView(lyap).showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        frame2.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("basin");
        
        // (4)  Flow + Lyapunov + Basin Logistieke
        ///*
        WIDTH = 500; HEIGHT = 300;
        ColorMap map1d = new ColorMap1D(HEIGHT);
        map = new Logistic();
        model = new GridModel(map, WIDTH, HEIGHT, 0, 0, 4, 1);
        c = new Controller(model);
        flow = new FlowView(model);
        flow.setColorMap(map1d);
        frame = flow.showFrame(c);
        lyap = new Lyapunov1D(model);
        frame2 = new FlowView(lyap).showFrame(c);
        BasinView basin = new BasinView(model);
        basin.setColorMap(map1d);
        JFrame frame3 = basin.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        frame2.dispose();
        frame3.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("henon");
        
        /*
         2D maps
         */
        
        // (5)      Henon
        ///*
        WIDTH = 500; HEIGHT = 500;
        map = new Henon(1.4, -0.3);
        double xmin = -3, ymin = -3, xmax = 3, ymax = 3;
        model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        c = new Controller(model);
        ColorMap map2d = new ColorMap2D(WIDTH, HEIGHT);
        flow = new FlowView(model);
        frame = flow.showFrame(c);
        basin = new BasinView(model);
        basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        frame2 = basin.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        frame2.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("kat");
        
        // (6)      Kat
        ///*
        WIDTH = 600; HEIGHT = 600;
        map = new Cat();
        xmin = 0; ymin = 0; xmax = 1; ymax = 1;
        model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        c = new Controller(model);
        map2d = new ColorMap2D(WIDTH, HEIGHT);
        flow = new FlowView(model);
        frame = flow.showFrame(c);
        basin = new BasinView(model);
        //basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        frame2 = basin.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        frame2.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("julia");
        
        // (7)      Julia
        ///*
        WIDTH = 600; HEIGHT = 600;
        map = new Julia(0.31,0.04);
        //Map map = new Julia(-0.12, 0.74);
        //Map map = new Julia(0, 1);
        //Map map = new Julia(-0.194, 0.6557);
        //Map map = new Julia(-0.74543, 0.11301);
        //Map map = new Julia(-1.25, 0);
        //Map map = new Julia(-0.39054, -0.58679);
        //Map map = new Julia(0.11031, -0.67037);
        //Map map = new Julia(-0.481762, -0.531657);
        //Map map = new Julia(-0.11, c);
        xmin = -1.5; ymin = -1.5; xmax = 1.5; ymax = 1.5;
        model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        c = new Controller(model);
        map2d = new ColorMap2D(WIDTH, HEIGHT);
        flow = new FlowView(model);
        frame = flow.showFrame(c);
        basin = new BasinView(model);
        basin.scaleColors(1.0);
        basin.setColorMap(map2d);
        frame2 = basin.showFrame(c);
        //*/
        
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("stop");
        c.stop();
        frame.dispose();
        frame2.dispose();
        TextReader.in.readLine();TextReader.in.readLine();System.out.println("newton");
        
        // (8)      Newton
        ///*
        WIDTH = 500; HEIGHT = 500;
        map = new NewtonRaphson(1, 0);
        xmin = -1.5; ymin = -1.5; xmax = 1.5; ymax = 1.5;
        model = new GridModel(map, WIDTH, HEIGHT, xmin, ymin, xmax, ymax);
        c = new Controller(model);
        map2d = new ColorMap2D(WIDTH, HEIGHT);
        flow = new FlowView(model);
        flow.showFrame(c);
        basin = new BasinView(model);
        basin.scaleColors(2.0);
        basin.setColorMap(map2d);
        basin.showFrame(c);
        //*/
    }
}
