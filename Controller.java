/**
    Menubar met controls
    Arne Vansteenkiste
*/
import javax.swing.*;
import java.awt.event.*;

public class Controller extends JFrame{

    private AbstractModel model;
    private boolean running;
    private int iterations = 1;
    public Run run;
    
    private Action reset = new AbstractAction("reset"){
        public void actionPerformed(ActionEvent e){
            model.reset();
        }
    };

    private Action iterate1 = new AbstractAction("1 iteratie"){
        public void actionPerformed(ActionEvent e){
            model.iterate(1);
        }
    };
    private Action iterate = new AbstractAction("Aantal iteraties"){
        public void actionPerformed(ActionEvent e){
            model.iterate(iterations);
        }
    };
    private Action startStop = new AbstractAction("Start / Stop"){
        public void actionPerformed(ActionEvent e){
            start_stop();
        }
    };
    private class SetIter extends AbstractAction{
        private int n;
        public SetIter(int n){
            super(n + " iteraties");
            this.n = n;
        }
        public void actionPerformed(ActionEvent e){
            iterations = n;
        }
    }
    public Controller(AbstractModel model){
        this.model = model;
    }
    
    public void start_stop(){
        running = !running;
        if(running){
            run = new Run();
            run.setPriority(Thread.MIN_PRIORITY);
            run.start();
        }
    }

    public void stop(){
        running = false;
        run = null;
    }
    
    private class Run extends Thread{
        public void run(){
            while(running){
                model.iterate(iterations);
                //try{
                    Thread.currentThread().yield();
                /*}
                catch(InterruptedException e){}*/
            }
        }
    }

    public JMenuBar createMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu iter = new JMenu("Itereer");
        iter.add(new JMenuItem(iterate1));
        iter.add(new JMenuItem(iterate));
        iter.add(new JMenuItem(startStop));
        iter.add(new JMenuItem(reset));
        bar.add(iter);
        JMenu iters = new JMenu("Aantal iteraties");
        for(int i = 0; i < 11; i++){
            iters.add(new JMenuItem(new SetIter((int)Math.pow(2, i))));
        }
        bar.add(iters);
        return bar;
    } 
}
