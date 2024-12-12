package note.scenario;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToSelectObject;
import note.noteApp;
import note.noteBoundingBox;
import note.noteCanvas2D;
import note.notePtCurve;
import note.notePtCurve.SelectState;
import note.notePtCurveMgr;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteSelectScenario extends XScenario {
    // singleton
    private static noteSelectScenario mSingleton = null;
    public static noteSelectScenario getSingleton() {
        assert(noteSelectScenario.mSingleton != null);
        return noteSelectScenario.mSingleton;
    }
    public static noteSelectScenario createSingleton(XApp app) {
        assert(noteSelectScenario.mSingleton == null);
        noteSelectScenario.mSingleton = new noteSelectScenario(app);
        return noteSelectScenario.mSingleton;
    }
    private noteSelectScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteSelectScenario.SelectScene.createSingleton(this));
        this.addScene(noteSelectScenario.SelectedScene.createSingleton(this));
    }

    public static class SelectScene extends noteScene {
        
        private ArrayList<Point2D.Double> dragPath = new ArrayList<>();
        private static final Stroke SelectArea =
            new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        
        // singleton pattern
        private static SelectScene mSingleton = null;
        public static SelectScene getSingleton() {
            assert(SelectScene.mSingleton != null);
            return SelectScene.mSingleton;
        }
        public static SelectScene createSingleton(XScenario scenario) {
            assert(SelectScene.mSingleton == null);
            SelectScene.mSingleton = new SelectScene(scenario);
            return SelectScene.mSingleton;
        }
        private SelectScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            this.dragPath.clear();
            Point2D.Double startPt = new Point2D.Double(e.getX(), e.getY());
            this.dragPath.add(startPt);
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            Point2D.Double pts = new Point2D.Double(e.getX(), e.getY());
            this.dragPath.add(pts);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteCmdToSelectObject.execute((XApp) this.mScenario.getApp(), this.dragPath);
            this.dragPath.clear();
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
                      
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_SHIFT:
                    notePtCurve ptCurve = note.getPtCurveMgr().getCurrPtCurve();
                    if (ptCurve != null && ptCurve.getPts().size() > 1) {
                        note.getPtCurveMgr().getPtCurves().add(ptCurve);
                    }
                    note.getPtCurveMgr().setCurrPtCurve(null);

                    boolean hasSelectedCurve = false;
                    for (notePtCurve curve : note.getPtCurveMgr().getPtCurves()) {
                        if (curve.getSelectState() == SelectState.SELECTED) {
                            hasSelectedCurve = true;
                            break;
                        }
                    }
                if (hasSelectedCurve) {
                    XCmdToChangeScene.execute(note,
                        noteSelectScenario.SelectedScene.getSingleton(), null);
                } else {
                    // SELECTED 상태의 곡선이 없으면 ReadyScene으로 변경
                    XCmdToChangeScene.execute(note,
                        noteDefaultScenario.ReadyScene.getSingleton(),null);
                }
            }
        }

        @Override
        public void updateSupportObjects() {
        
        }

        @Override
        public void renderWorldObjects(Graphics2D g2) {
        
        }

        @Override
        public void renderScreenObjects(Graphics2D g2) {
            // Draw the selection path
            noteApp note = (noteApp) this.mScenario.getApp();
            g2.setColor(Color.GRAY);
            g2.setStroke(note.getCanvas2D().DASHED_STROKE);
            Path2D.Double path = new Path2D.Double();
            if (!dragPath.isEmpty()) {
                Point2D.Double pt0 = dragPath.get(0);
                path.moveTo(pt0.x, pt0.y);
                for (Point2D.Double pt : dragPath) {
                    path.lineTo(pt.x, pt.y);
                }
                g2.draw(path);
            }
            note.getCanvas2D().drawBoundingBoxForSelectedCurves(g2);
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
        
        }
    }
    
    
    public static class SelectedScene extends noteScene {
        
        private noteBoundingBox boundingBox;
        
        // singleton pattern
        private static SelectedScene mSingleton = null;
        public static SelectedScene getSingleton() {
            assert(SelectedScene.mSingleton != null);
            return SelectedScene.mSingleton;
        }
        public static SelectedScene createSingleton(XScenario scenario) {
            assert(SelectedScene.mSingleton == null);
            SelectedScene.mSingleton = new SelectedScene(scenario);
            return SelectedScene.mSingleton;
        }
        private SelectedScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
           
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
        
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
        
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
    notePtCurveMgr curveMgr = note.getPtCurveMgr();
    int code = e.getKeyCode();
    
    switch (code) {
        case KeyEvent.VK_ESCAPE: // 선택 취소
            // 선택된 곡선들만 따로 저장
            ArrayList<notePtCurve> selectedCurves = new ArrayList<>();
            for (notePtCurve ptCurve : note.getPtCurveMgr().getPtCurves()) {
                if (ptCurve.getSelectState() == SelectState.SELECTED) {
                    selectedCurves.add(ptCurve);
                }
            }
            for (notePtCurve ptCurve : selectedCurves) {
                ptCurve.setSelectState(SelectState.DEFAULT);
            }

            note.getCanvas2D().repaint();
            break;

        case KeyEvent.VK_E: // 선택된 곡선 지우기
            ArrayList<notePtCurve> curvesToRemove = new ArrayList<>();
            for (notePtCurve ptCurve : note.getPtCurveMgr().getPtCurves()) {
                if (ptCurve.getSelectState() == SelectState.SELECTED) {
                    curvesToRemove.add(ptCurve);  // 삭제할 곡선 추가
                }
            }

            for (notePtCurve ptCurve : curvesToRemove) {
                curveMgr.removeCurve(ptCurve);
            }
            break;
    }
            boundingBox = null;  // BoundingBox 지우기
            note.getCanvas2D().repaint();
            XCmdToChangeScene.execute(note,
                        noteDefaultScenario.ReadyScene.getSingleton(), null);
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
           
        }

        @Override
        public void updateSupportObjects() {
        
        }

        @Override
        public void renderWorldObjects(Graphics2D g2) {
        
        }

        @Override
        public void renderScreenObjects(Graphics2D g2) {
            noteApp note = (noteApp) this.mScenario.getApp();
            note.getCanvas2D().drawBoundingBoxForSelectedCurves(g2);
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
        
        }
    }
}
