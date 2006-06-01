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
