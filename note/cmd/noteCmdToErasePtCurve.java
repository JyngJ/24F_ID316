package note.cmd;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import note.noteApp;
import note.notePtCurve;
import note.notePtCurveMgr;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToErasePtCurve extends XLoggableCmd {
    private ArrayList<Point2D> dragPath;

    private noteCmdToErasePtCurve(XApp app, ArrayList<Point2D> dragPath) {
        super(app);
        this.dragPath = dragPath;
    }

    public static boolean execute(XApp app, ArrayList<Point2D> dragPath) {
        noteCmdToErasePtCurve cmd = new noteCmdToErasePtCurve(app, dragPath);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        notePtCurveMgr curveMgr = note.getPtCurveMgr();

        // 드래그 영역의 최소 Bounding Box
        Rectangle2D.Double dragBox = this.getBoundingBox(this.dragPath);

        // 곡선 제거
        ArrayList<notePtCurve> curvesToRemove = new ArrayList<>();
        for (notePtCurve curve : curveMgr.getPtCurves()) {
            if (this.isCurveInBox(curve, dragBox)) {
                curvesToRemove.add(curve);
            }
        }

        for (notePtCurve curve : curvesToRemove) {
            curveMgr.removeCurve(curve);
        }

        return true;
    }

    private Rectangle2D.Double getBoundingBox(ArrayList<Point2D> points) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (Point2D pt : points) {
            minX = Math.min(minX, pt.getX());
            minY = Math.min(minY, pt.getY());
            maxX = Math.max(maxX, pt.getX());
            maxY = Math.max(maxY, pt.getY());
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    private boolean isCurveInBox(notePtCurve curve, Rectangle2D.Double box) {
        for (Point2D pt : curve.getPts()) {
            if (box.contains(pt)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String createLog() {
        return this.getClass().getSimpleName() + "\tDrag path size: " + this.dragPath.size();
    }
}
