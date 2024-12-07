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
    
    private noteEventListener mEventListener = null;
    
    private XLogMgr mLogMgr = null;
    @Override
    public XLogMgr getLogMgr() {
         return this.mLogMgr;
    }
    
    private noteFormulaMgr mFormulaMgr = null;
    
    public noteFormulaMgr getFormulaMgr() {
        return this.mFormulaMgr;
    }
    
    public noteApp() {
        //create components
        // 1) frame 2) canvas
        this.mFrame = new JFrame("New Note App");
        this.mCanvas2D = new noteCanvas2D(this);
        this.mScenarioMgr = new noteScenarioMgr(this);
        this.mEventListener = new noteEventListener(this);
        this.mLogMgr = new XLogMgr();
        this.mLogMgr.setPrintOn(true);
        this.mFormulaMgr = new noteFormulaMgr();
        
        //connect event listeners
        this.mCanvas2D.addMouseListener(this.mEventListener);
        this.mCanvas2D.addMouseMotionListener(this.mEventListener);
        this.mCanvas2D.setFocusable(true);
        this.mCanvas2D.addKeyListener(this.mEventListener);
        
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
