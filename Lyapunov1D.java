/**
    Een Lyapunov-model geeft de Lyapunov getallen van alle banen in een GridModel.
    Arne Vansteenkiste
*/
public class Lyapunov1D extends GridModel implements ModelListener{

    protected final GridModel model;
    protected final double[][] lyap;
    protected int k;
    public static int skip = 0;
    
    public Lyapunov1D(GridModel m){
        super(null, m.width, m.height, m.xmin, 0.0, m.xmax, 3.0);
        this.model = m;
        lyap = new double[m.width][m.height];
        for(int i = 0; i < m.width; i++)
            for(int j = 0; j < m.height; j++){
                xData[i][j] = m.getXData()[i][j];
                lyap[i][j] = 1.0;
                }
        m.addListener(this);
    }
    
    public void dataChanged(){
        skip++;
        if(skip < 0)
            return;
        k++;
        final double[][] xdata = model.getXData();
        final double[][] ydata = model.getYData();
        for(int i = 0; i < model.width; i++)
            for(int j = 0; j < model.height; j++){
                final double x = xdata[i][j];
                final double y = ydata[i][j];
                lyap[i][j] *= (Math.abs(model.getMap().diffY(x, y)));
            }
        for(int i = 0; i < model.width; i++)
            for(int j = 0; j < model.height; j++){
                yData[i][j] = Math.pow(lyap[i][j],1.0/k);
            }
        fireChanged();
    }
    
    public String toString(){
        return "Lyapunov: " + model;
    }
}