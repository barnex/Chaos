/**
    Methode van Newton voor het vinden van wortels.
    Arne Vansteenkiste
*/

public class NewtonRaphson extends Map{

    private final Complex z = new Complex(), z2 = new Complex(), z3 = new Complex(), 
    z4 = new Complex(), z5 = new Complex();
    private final Complex n = new Complex();
    private final Complex c = new Complex();
    
    public NewtonRaphson(double a, double b){
        setParams(a, b);
        
    }
    
    public NewtonRaphson(){
        this(1, 0);
    }
    
    public void setParams(double a, double b){
        c.set(a, b);
    }
    
    public double xNext(double x, double y){
        x5_1(x, y);
        return n.real();
    }

    public double yNext(double x, double y){
        return n.imag();
    }
    
    //wortels van x^3 - c;
    private void x3_1(double x, double y){
        z.set(x, y);
        z2.set(z).multiply(z);
        z3.set(z2).multiply(z);
        n.set( z .sub ( z3.sub(c) .multiply ( z2.multiply(3).inv() ) ) );

    }
    
    //wortels van x^5 - c;
    private void x5_1(double x, double y){
        z.set(x, y);
        z2.set(z).multiply(z);
        z4.set(z2).multiply(z2);
        z5.set(z4).multiply(z);
        n.set( z .sub ( z5.sub(c) .multiply ( z4.multiply(5).inv() ) ) );
    }
}
