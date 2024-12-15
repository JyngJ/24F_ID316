package note.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import note.noteApp;
import note.notePtCurve;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToDrawPtCurve extends XLoggableCmd {
    // fields 
    private Point mScreenPt = null;
    private Point2D.Double mWorldPt = null;
    private notePtCurve mPtCurve = null;
    
    // private constructor
    private noteCmdToDrawPtCurve(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        noteCmdToDrawPtCurve cmd = new noteCmdToDrawPtCurve(app, pt);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        notePtCurve currPtCurve = note.getPtCurveMgr().getCurrPtCurve();
        
        int size = currPtCurve.getPts().size();
        Point2D.Double lastWorldPt = currPtCurve.getPts().get(size - 1);
        Point lastScreenPt = note.getXform().calcPtFromWorldToScreen(
            lastWorldPt);
        if (this.mScreenPt.distance(lastScreenPt) < 
            notePtCurve.MIN_DIST_BTWN_PTS) {
            return true;
        }
        this.mWorldPt = note.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        currPtCurve.addPt(this.mWorldPt);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
    
}
