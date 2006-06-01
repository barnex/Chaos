/**
    Een Model is een manier om een Map weer te geven. 
    Arne Vansteenkiste
*/
import java.util.*;

public abstract class AbstractModel {

    protected final Interpolator xDouble, yDouble;
    public final int width, height;
    protected int iteration;
    public final double xmin, ymin, xmax, ymax;
    protected final Map map;
    protected Vector listeners = new Vector();
    
    public AbstractModel(Map map, int width, int height, 
                         double xmin, double ymin, double xmax, double ymax){
        this.map = map;
        this.width = width;
        this.height = height;
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        xDouble = new Interpolator(0, xmin, width, xmax);
        yDouble = new Interpolator(height, ymin, 0, ymax);
    }
    
    protected abstract void iterate();
    
    public void reset(){
        
    }
    
    public final void iterate(int n){
        for(int i = 0; i < n; i++)
            iterate();
        fireChanged();
    }
    
    public void runSlowly(){
        for(;;){
            iterate();
            try{
                Thread.currentThread().sleep(1500);
            }
            catch(Exception e){}
        }
    }
    
    public void addListener(ModelListener l){
        listeners.add(l);
    }
    
    public int getIteration(){
        return iteration;
    }
    
    protected final double xNext(double x, double y){
        return map.xNext(x, y);
    }
    
    protected final double yNext(double x, double y){
        return map.yNext(x, y);
    }
    
    public Map getMap(){
        return  map;
    }
    
    public String toString(){
        return map.getClass().getName();
    }
    
    public Interpolator getXInterpolator(){
        return xDouble;
    }
    
    public Interpolator getYInterpolator(){
        return yDouble;
    }
    
    protected void fireChanged(){
        for(int i = 0; i < listeners.size(); i++)
            ((ModelListener)listeners.get(i)).dataChanged();
    }
}
