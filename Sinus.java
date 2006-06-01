/**
    Een sinus-afbeelding die zeer goed lijkt op de logistieke.
    Arne Vansteenkiste
*/
public final class Sinus extends Map{

    public double xNext(double x, double y){
        return x;
    }
    
    public double yNext(double x, double y){
        return x*Math.sin(y*Math.PI);
    }
}
