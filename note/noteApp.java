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
    public noteColorChooser mColorChooser = null;

    public noteColorChooser getColorChooser() {
        return this.mColorChooser;
    }

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

    private notePtCurveMgr mPtCurveMgr = null;

    public notePtCurveMgr getPtCurveMgr() {
        return this.mPtCurveMgr;
    }

    public noteXform mXform = null;

    public noteXform getXform() {
        return this.mXform;
    }

    private notePenMarkMgr mPenMarkMgr = null;

    public notePenMarkMgr getPenMarkMgr() {
        return this.mPenMarkMgr;
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
        this.mPtCurveMgr = new notePtCurveMgr();
        this.mXform = new noteXform();
        this.mColorChooser = new noteColorChooser();
        this.mPenMarkMgr = new notePenMarkMgr();

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
