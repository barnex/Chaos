/**
    MultiView kan gebruikt worden om meerdere views boven elkaar af te beelden.
    Arne Vansteenkiste
*/

import java.awt.*;

public class MultiView extends GridView{

    private GridView view1, view2;
    
    public MultiView(GridView view1, GridView view2){
        super(view1.model);
        view1.model.addListener(this);
        this.view1 = view1;
        this.view2 = view2;
    }
    
    public void paintComponent(Graphics g){
        view1.paintView(g);
        view2.paintView(g);
    }
    
    public void modelChanged(){
        repaint();
    }
}
