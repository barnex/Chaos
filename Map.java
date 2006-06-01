/**
    Een Map stelt een willekeurige (2D) afbeelding voor. De abstracte methodes
    xNext en yNext geven de nieuwe x en y na een iteratie.
    Arne Vansteenkiste
*/
public abstract class Map implements Cloneable{
    
    protected double a, b;
    
    public abstract double xNext(double x, double y);
    public abstract double yNext(double x, double y);
    
    private static final double H = 1.0/131072.0;
    
    public double diffY(double x, double y){
        return (yNext(x, y+H) - yNext(x, y-H)) / (2.0*H);
    }
    
    public void setParams(double a, double b){
        this.a = a;
        this.b = b;
    }
    
    public Map clone(double a, double b){
        try{
            Map map = (Map)clone();
            map.setParams(a, b);
            return map;
        }
        catch(CloneNotSupportedException e){
            return null;
        }
    }
    
    public String toString(){
        return getClass().getName();// + "(" + a + ", " +  b + ")";
    }
}
