
public class Lyapunov extends GridModel implements ModelListener{

    private GridModel model;
    private double[][] prev;
    private double[][] prev2;
    private int xy;
    public static final int X = 0, Y = 1;
    
    public Lyapunov(GridModel m, int xy){
        super(null, m.width, m.height, m.xmin, 0.0, m.xmax, 2.0);
        this.model = m;
        this.xy = xy;
        prev = new double[width][height];
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++){
                if(xy == X){
                    prev[i][j] = Double.NaN;
                    yData[i][j] = yDouble.transf(j);
                }
                else{
                    prev[i][j] = Double.NaN;
                    xData[i][j] = xDouble.transf(i);
                }
            }
        m.addListener(this);
    }
    
    public void dataChanged(){
        final double[][] currX = model.getXData();
        final double[][] currY = model.getYData();
        
        if(xy == X){
            for(int i = 0; i < width; i++)
                for(int j = 0; j < height; j++){
                    xData[i][j] = currX[i][j] / prev[i][j];
                    prev[i][j] = currX[i][j];
                }
        }
        else{
            for(int i = 0; i < width; i++)
                for(int j = 0; j < height; j++){
                    yData[i][j] = currY[i][j] / prev[i][j];
                    prev[i][j] = currY[i][j];
                }
        }
        fireChanged();
    }
    
    public String toString(){
        return getClass().getName() + ": " + model;
    }
}
