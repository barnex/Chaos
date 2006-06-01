/**
    De bakkers - afbeelding.
    Arne Vansteenkiste
*/

public class SkinnyBaker extends Map{

    public double xNext(double x, double y){
        double ynext = y * 2.0;
        if(ynext > 1.0)
            return x / 3.0 + 2.0/3.0;
        else
            return x / 3.0;
    }
    
    public double yNext(double x, double y){
        return  (y * 2.0) % 1.0;
    }
}
