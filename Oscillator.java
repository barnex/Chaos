/**
    Harmonisce oscillator afbeelding.
    Arne Vansteenkiste
*/
public final class Oscillator extends Map{
    
    private double freq, damp;
    
    public Oscillator(double freq, double damp){
        this.freq = freq;
        this.damp = damp;
    }
    
    public double xNext(double x, double y){
        double phase = Math.atan2(y, x);
        double r = Math.sqrt(x*x + y*y);
        phase += freq;
        r *= damp;
        return r*Math.cos(phase);
    }
    
    public double yNext(double x, double y){
        double phase = Math.atan2(y, x);
        double r = Math.sqrt(x*x + y*y);
        phase += freq;
        r *= damp;
        return r*Math.sin(phase);
    }
}
