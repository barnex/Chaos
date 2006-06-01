/**
    De logistieke afbeelding als 2D afbeelding. x is de paramter, y is de waarde.
    Arne Vansteenkiste
*/
public final class Logistic extends Map{
    
    public double xNext(double x, double y){
        return x;
    }
    
    public double yNext(double x, double y){
        return logistic(x, y);
    }
    
    public double diffY(double x, double y){
        return x*(1-2*y);
    }
    
    private final double logistic(double a, double x){
        return a*x*(1-x);
    }
}
