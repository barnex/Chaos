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
}