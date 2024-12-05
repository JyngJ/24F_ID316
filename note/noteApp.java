package note;

import javax.swing.JFrame;
import x.XApp;
import x.XLogMgr;
import x.XScenarioMgr;


public class noteApp extends XApp {
    private JFrame mFrame = null;
    private noteCanvas2D mCanvas2D = null;
    public noteCanvas2D getCanvas2D() {
        return this.mCanvas2D;
    }
    
    private XScenarioMgr mScenarioMgr = null;
    
    @Override
    public XScenarioMgr getScenarioMgr() {
        return this.mScenarioMgr;
    }
    
    private XLogMgr mLogMgr = null;
    @Override
    public XLogMgr getLogMgr() {
         return this.mLogMgr;
    }
    
    public noteApp() {
        //create components
        // 1) frame 2) canvas
        this.mFrame = new JFrame("New Note App");
        this.mCanvas2D = new noteCanvas2D(this);
        this.mScenarioMgr = new noteScenarioMgr(this);
        
        //build & show visual components
        this.mFrame.add(this.mCanvas2D);
        this.mFrame.setSize(800, 600);
        this.mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mFrame.setVisible(true);
    }
    
    // Entry point of note project, create a note instance
    public static void main(String[] args) {
        new noteApp();
    }

    
    
}
