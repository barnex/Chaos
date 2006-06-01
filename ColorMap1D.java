/**
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
