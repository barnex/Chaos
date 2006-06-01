/**
    De Julia afbeelding.
    Arne Vansteenkiste
*/

public final class Julia extends Map{
    
    public Julia(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public double xNext(double x, double y){
        return x*x - y*y + a;
    }
    
    public double yNext(double x, double y){
        return 2*x*y + b;
    }
}
