/**
    Een OrbitModel geeft een Map weer door de baan van 1 punt te tonen.
    Arne Vansteenkiste
*/
public class OrbitModel extends AbstractModel{

    private double x, y;
    
    public OrbitModel(Map map, int width, int height, 
                         double xmin, double ymin, double xmax, double ymax){
        super(map, width, height, xmin, ymin, xmax, ymax);
    }
    
    protected void iterate(){
        double bufX = x, bufY = y;
        x = map.xNext(bufX, bufY);
        y = map.yNext(bufX, bufY);
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public void setData(double x, double y){
        this.x = x;
        this.y = y;
        iteration = 0;
        fireChanged();
    }
}
