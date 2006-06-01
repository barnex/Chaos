/**
    Lineaire interpolator voor het omzetten van coordinaten.
    Arne Vansteenkiste
*/
public final class Interpolator {
    
    private final double x1, y1, x2, y2;
    
    public Interpolator(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public final double transf(double x){
        return y1 + (y2 - y1) / (x2 - x1) * (x-x1);
    }
    
    public Interpolator inverse(){
        return new Interpolator(y1, x1, y2, x2);
    }
    
    public Interpolator scaleX(double factor){
        double c = (x1 + x2) / 2;
        return new Interpolator(x1 * factor - c , y1, x2 * factor - c, y2);
    }
}
