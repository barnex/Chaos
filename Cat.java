/**
    De kat - afbeelding.
    Arne Vansteenkiste
*/
public class Cat extends Map{

     public double xNext(double x, double y){
        return (2*x + y) % 1.0;
    }
    
    public double yNext(double x, double y){
        return (x + y) % 1.0;
    }
}
