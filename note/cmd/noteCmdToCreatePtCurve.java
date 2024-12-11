package note.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import note.noteApp;
import note.notePtCurve;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToCreatePtCurve extends XLoggableCmd {
    private Point mScreenPt = null;
    private Point2D.Double mWorldPt = null;
    
    private noteCmdToCreatePtCurve(XApp app, Point pt) {
        super(app);
        this.mScreenPt = pt;
    }
    
    public static boolean execute(XApp app, Point pt) {
        noteCmdToCreatePtCurve cmd = new noteCmdToCreatePtCurve(app, pt);
        return cmd.execute(); 
    }
    
    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        this.mWorldPt= note.getXform().calcPtFromScreenToWorld(this.mScreenPt);
        notePtCurve ptCurve = new notePtCurve(this.mWorldPt,
            note.getCanvas2D().getCurrColorForCurve(),
            note.getCanvas2D().getCurrStrokeForPtCurve());
        note.getPtCurveMgr().setCurrPtCurve(ptCurve);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mScreenPt).append("\t");
        sb.append(this.mWorldPt);
        return sb.toString();
    }
}
