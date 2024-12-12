package note.cmd;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.noteApp;
import note.notePtCurve;
import note.notePtCurveMgr;
import note.noteScenarioMgr;
import note.scenario.noteDrawScenario.EraseScene;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToSelectForErase extends XLoggableCmd {
    // fields
    private ArrayList<Point2D> dragPath;  // 드래그 경로
    
    // private constructor
    private noteCmdToSelectForErase(XApp app, ArrayList<Point2D> dragPath) {
        super(app);
        this.dragPath = dragPath;
    }

    public static boolean execute(XApp app, ArrayList<Point2D> dragPath) {
        noteCmdToSelectForErase cmd = new noteCmdToSelectForErase(app, dragPath);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        notePtCurveMgr curveMgr = note.getPtCurveMgr();
        noteScenarioMgr scenarioMgr = (noteScenarioMgr) note.getScenarioMgr();

        // 드래그 경로를 Path2D로 변환
        Path2D dragPath2D = createPathFromDragPoints(this.dragPath);

        // 현재 Scene이 EraseScene인지 SelectScene인지 확인
        boolean isEraseScene = scenarioMgr.getCurrScene() instanceof EraseScene;

        // 드래그 중 선택 상태 업데이트
        for (notePtCurve curve : curveMgr.getPtCurves()) {
            boolean isIntersecting = isCurveInPath(curve, dragPath2D);

            // EraseScene이라면 ERASE_SELECTED, SelectScene이라면 SELECTED로 변경
            if (isIntersecting) {
                if (isEraseScene) {
                    curve.setSelectState(notePtCurve.SelectState.ERASE_SELECTED);  // EraseScene
                } else {
                    curve.setSelectState(notePtCurve.SelectState.SELECTED);  // SelectScene
                }
            } else {
                curve.setSelectState(notePtCurve.SelectState.DEFAULT);  // 선택되지 않은 곡선
            }
        }

        // 화면에서 업데이트는 `drawPtCurves`가 처리
        return true;
    }

    // 드래그 포인트로 Path2D 객체 생성
    private Path2D createPathFromDragPoints(ArrayList<Point2D> dragPoints) {
        Path2D path = new Path2D.Double();
        if (!dragPoints.isEmpty()) {
            path.moveTo(dragPoints.get(0).getX(), dragPoints.get(0).getY());
            for (int i = 1; i < dragPoints.size(); i++) {
                path.lineTo(dragPoints.get(i).getX(), dragPoints.get(i).getY());
            }
        }
        return path;
    }

    // 곡선이 경로 안에 포함되어 있는지 확인
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