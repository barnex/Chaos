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
