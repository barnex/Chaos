/**
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
