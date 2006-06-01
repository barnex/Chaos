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
