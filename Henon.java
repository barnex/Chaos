/**
    De Henon afbeelding.
    Arne Vansteenkiste
*/
public final class Henon extends Map{
    
    public Henon(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public double xNext(double x, double y){
        return a - x*x + b*y;
    }
    
    public double yNext(double x, double y){
        return x;
    }

}
