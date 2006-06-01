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
