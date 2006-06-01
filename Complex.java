/**
    Complexe getallen
    Arne Vansteenkiste
*/
import java.io.Serializable;

public final class Complex implements Serializable, Cloneable
{
    private double real;
    private double imag;
  
    public Complex(double real, double imag){	
        this.real = real;
        this.imag = imag;
    }

    public Complex(double real){	
        this(real, 0);
    }

    public Complex(Complex other){	
        this(other.real, other.imag);
    }

    public Complex(){
    	this(0, 0);
    }
    
    public double real(){
    	return real;
    }

    public double imag(){
    	return imag;
    }

    public double abs(){	
        return Math.sqrt(real*real + imag*imag);
    }

    public boolean isZero(){
    	return real == 0.0 && imag == 0.0;
    }

    public double arg(){	
        return Math.atan2(imag, real);
    }

    public boolean isReal(){	
        return(imag == 0.0);
    }

    public boolean isImag(){
    	return(real == 0.0);
    }

    public Complex set(double newValue){
        real = newValue;
        imag = 0;
        return this;
    }
    
    public Complex set(double newReal, double newImag){
        real = newReal;
        imag = newImag;
        return this;
    }
    
    public Complex set(Complex other){
        this.real = other.real;
        this.imag = other.imag;
        return this;
    }
    
    
    public boolean equals(Object obj){
	if(obj instanceof Complex){
	    Complex c = (Complex)obj;
	    return real == c.real && imag == c.imag;
	}
	else
	    return false;
    }
    

    public String toString(){	
        String ret = "";
        if(isZero())
            return "0";
        else{
            if(real != 0.0){
                ret += strip(real);
                if(imag > 0)
                    ret += '+';
            }
            if(imag == 1.0)
                ret += 'i';
            else if(imag == -1.0)
                ret += "-i";
            else if(imag != 0.0)
                ret += strip(imag) + 'i';
        }
        return ret;
    }

    private String strip(double number){
        String ret = "" + number;
        if(ret.length() > 2 && ret.substring(ret.length()-2, ret.length()).equals(".0"))
            ret = ret.substring(0, ret.length()-2);
        return ret;
    }
    
    public String toPolarString(){	
        return abs() + "*e^" + arg() + "i";
    }

    public double toDouble(){
        if(imag != 0)
            return Double.NaN;
        else
            return real;
    }
    
    
    public Complex add(Complex c){
        this.real += c.real;
        this.imag += c.imag;
        return this;
    }
    
    public Complex add(double r){
        real += r;
        return this;
    }
  
    public Complex sub(Complex c){
        real -= c.real;
        imag -= c.imag;
        return this;
    }
    
    public Complex sub(double r){
        real -= r;
        return this;
    }
    
    public Complex negate(){	
        real = -real;
        imag = -imag;
        return this;
    }
    
    public Complex conj(){
        imag = -imag;
        return this;
    }

    public Complex multiply(Complex c){
        final double a = real;
        final double b = imag;
        real = a*c.real - b*c.imag;
        imag = a*c.imag + b*c.real;
        return this;
    }
    
    public Complex multiply(double d){
        real *= d;
        imag *= d;
        return this;
    }

    public Complex inv(){
        double n = real*real + imag*imag;
        real =  real/n;
        imag = -imag/n;
        return this;
    }    
}
