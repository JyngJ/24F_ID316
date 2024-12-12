
package note.cmd;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.noteApp;
import note.notePtCurve;
import note.notePtCurveMgr;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToErasePtCurve extends XLoggableCmd {
    private ArrayList<Point2D> dragPath;

    private noteCmdToErasePtCurve(XApp app, ArrayList<Point2D> dragPath, boolean isFinalErase) {
        super(app);
        this.dragPath = dragPath;
    }

    public static boolean execute(XApp app, ArrayList<Point2D> dragPath, boolean isFinalErase) {
        noteCmdToErasePtCurve cmd = new noteCmdToErasePtCurve(app, dragPath, isFinalErase);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        notePtCurveMgr curveMgr = note.getPtCurveMgr();
        
        // 드래그 경로 -> Path2D
        Path2D dragPath2D = createPathFromDragPoints(this.dragPath);
        
        // 삭제할 곡선 선택
        ArrayList<notePtCurve> curvesToRemove = new ArrayList<>();
        for (notePtCurve curve : curveMgr.getPtCurves()) {
            boolean isIntersecting = isCurveInPath(curve, dragPath2D);
            if (isIntersecting && (curve.getSelectState() ==
                notePtCurve.SelectState.ERASE_SELECTED))
                curvesToRemove.add(curve);
        }
        
        // 제거
        for (notePtCurve curve : curvesToRemove) {
            curveMgr.removeCurve(curve);
        }
        return true;
    }

    // Drag Point -> Path2D
    private Path2D createPathFromDragPoints(ArrayList<Point2D> dragPoints) {
        Path2D path = new Path2D.Double();
        if (!dragPoints.isEmpty()) {
            path.moveTo(dragPoints.get(0).getX(),
                dragPoints.get(0).getY());
            for (int i = 1; i < dragPoints.size(); i++) {
                path.lineTo(dragPoints.get(i).getX(),
                    dragPoints.get(i).getY());
            }
        }
        return path;
    }
    
    // 곡선이 경로에 포함되는지
    private boolean isCurveInPath(notePtCurve curve, Path2D path) {
        for (Point2D pt : curve.getPts()) {
            if (path.contains(pt)) {
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
