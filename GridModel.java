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
