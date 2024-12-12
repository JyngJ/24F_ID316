package note.scenario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToCreatePtCurve;
import note.cmd.noteCmdToErasePtCurve;
import note.cmd.noteCmdToFreeDrawing;
import note.noteApp;
import note.notePtCurve;
import note.notePtCurveMgr;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteDrawScenario extends XScenario {
    // singleton
    private static noteDrawScenario mSingleton = null;
    public static noteDrawScenario getSingleton() {
        assert(noteDrawScenario.mSingleton != null);
        return noteDrawScenario.mSingleton;
    }
    public static noteDrawScenario createSingleton(XApp app) {
        assert(noteDrawScenario.mSingleton == null);
        noteDrawScenario.mSingleton = new noteDrawScenario(app);
        return noteDrawScenario.mSingleton;
    }
    private noteDrawScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteDrawScenario.DrawScene.createSingleton(this));
        this.addScene(noteDrawScenario.EraseScene.createSingleton(this));
    }
    
    // 일반 필기
    public static class DrawScene extends noteScene {
        // singleton pattern
        private static DrawScene mSingleton = null;
        public static DrawScene getSingleton() {
            assert(DrawScene.mSingleton != null);
            return DrawScene.mSingleton;
        }
        public static DrawScene createSingleton(XScenario scenario) {
            assert(DrawScene.mSingleton == null);
            DrawScene.mSingleton = new DrawScene(scenario);
            return DrawScene.mSingleton;
        }
        private DrawScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
           
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            noteCmdToFreeDrawing.execute(note, pt);
            note.getCanvas2D().repaint();
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            notePtCurve currPtCurve = note.getPtCurveMgr().getCurrPtCurve();
            if (currPtCurve != null && 
                    currPtCurve.getPts().size() > 1){
                note.getPtCurveMgr().getPtCurves().add(currPtCurve);
            }
            note.getPtCurveMgr().setCurrPtCurve(null);
            XCmdToChangeScene.execute(note, 
                        noteDefaultScenario.ReadyScene.getSingleton(), this);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
                      
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
        ArrayList<Point2D> currDrawing = noteDefaultScenario.ReadyScene.getSingleton().points;
        for (int i = 1; i < currDrawing.size(); i++) {
            Point2D prev = currDrawing.get(i - 1);
            Point2D curr = currDrawing.get(i);
            g2.drawLine((int) prev.getX(), (int) prev.getY(), (int) curr.getX(), (int) curr.getY());
        }
        currDrawing = null;
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
            noteApp note = (noteApp) this.mScenario.getApp();
            note.getPtCurveMgr().setCurrPtCurve(null);
        }
    }

// EraseScene 추가
    
    public static class EraseScene extends noteScene {
        // singleton pattern
        private static EraseScene mSingleton = null;
        public static EraseScene getSingleton() {
            assert(EraseScene.mSingleton != null);
            return EraseScene.mSingleton;
        }
        public static EraseScene createSingleton(XScenario scenario) {
            assert(EraseScene.mSingleton == null);
            EraseScene.mSingleton = new EraseScene(scenario);
            return EraseScene.mSingleton;
        }
        private EraseScene(XScenario scenario) {
            super(scenario);
        }
        private ArrayList<Point2D> dragPath = new ArrayList<>();

        @Override
        public void handleMousePress(MouseEvent e) {
            this.dragPath.clear();
            this.dragPath.add(e.getPoint());
        }

//        @Override
//        public void handleMouseDrag(MouseEvent e) {
//            this.dragPath.add(e.getPoint());
//            
//        }
//
//        @Override
//        public void handleMouseRelease(MouseEvent e) {
//            noteApp note = (noteApp) this.mScenario.getApp();
//            this.dragPath.add(e.getPoint());
//            noteCmdToErasePtCurve.execute(note, this.dragPath);
//            this.dragPath.clear();
//            XCmdToChangeScene.execute(note, noteDefaultScenario.ReadyScene.getSingleton(), null);
//        }
        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D dragPoint = e.getPoint();

            // 드래그 경로와 겹치면 ERASE_SELECTED로 설정
            for (notePtCurve curve : note.getPtCurveMgr().getPtCurves()) {
                if (isPointNearCurve(curve, dragPoint)) {
                    curve.setSelectState(notePtCurve.SelectState.ERASE_SELECTED);
                }
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            // ERASE_SELECTED 상태의 곡선 제거
            ArrayList<notePtCurve> curvesToRemove = new ArrayList<>();
            for (notePtCurve curve : note.getPtCurveMgr().getPtCurves()) {
                if (curve.getSelectState() == notePtCurve.SelectState.ERASE_SELECTED) {
                    curvesToRemove.add(curve);
                }
            }
            for (notePtCurve curve : curvesToRemove) {
                note.getPtCurveMgr().removeCurve(curve);
            }

            // 상태 초기화
            for (notePtCurve curve : note.getPtCurveMgr().getPtCurves()) {
                curve.setSelectState(notePtCurve.SelectState.DEFAULT);
            }
        }

        private boolean isPointNearCurve(notePtCurve curve, Point2D dragPoint) {
            final double THRESHOLD = 10.0; // 드래그 포인트와 곡선 점 간의 최대 허용 거리

            for (Point2D.Double pt : curve.getPts()) {
                if (pt.distance(dragPoint) <= THRESHOLD) {
                    return true;
                }
            }
            return false;
        }

        
        @Override
        public void handleKeyUp(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_E) {
                XCmdToChangeScene.execute(this.mScenario.getApp(),
                    noteDefaultScenario.ReadyScene.getSingleton(), null);
        }
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
        }

        @Override
        public void updateSupportObjects() {
        }

        @Override
        public void renderWorldObjects(Graphics2D g2) {
        }

        @Override
        public void renderScreenObjects(Graphics2D g2) {
        }

        @Override
        public void getReady() {
            this.dragPath.clear();
        }

        @Override
        public void wrapUp() {
            this.dragPath.clear();
        }
    }
}
    
