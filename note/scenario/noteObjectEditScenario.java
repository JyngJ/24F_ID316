package note.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToScaleObject;
import note.noteApp;
import note.noteObject;
import note.notePenMarkMgr;
import note.notePtCurve;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteObjectEditScenario extends XScenario {
    // singleton
    private static noteObjectEditScenario mSingleton = null;
    public static noteObjectEditScenario getSingleton() {
        assert(noteObjectEditScenario.mSingleton != null);
        return noteObjectEditScenario.mSingleton;
    }
    public static noteObjectEditScenario createSingleton(XApp app) {
        assert(noteObjectEditScenario.mSingleton == null);
        noteObjectEditScenario.mSingleton = new noteObjectEditScenario(app);
        return noteObjectEditScenario.mSingleton;
    }
    private noteObjectEditScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteObjectEditScenario.TranslateScene.createSingleton(this));
        this.addScene(noteObjectEditScenario.ScaleScene.createSingleton(this));
    }

    public static class TranslateScene extends noteScene {
        
        private Point2D.Double prevPt = null;
        
        // singleton pattern
        private static TranslateScene mSingleton = null;
        public static TranslateScene getSingleton() {
            assert(TranslateScene.mSingleton != null);
            return TranslateScene.mSingleton;
        }
        public static TranslateScene createSingleton(XScenario scenario) {
            assert(TranslateScene.mSingleton == null);
            TranslateScene.mSingleton = new TranslateScene(scenario);
            return TranslateScene.mSingleton;
        }
        private TranslateScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            prevPt = null;
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D.Double currPt = new Point2D.Double(e.getX(), e.getY());

            // Calculate relative distance
            if (this.prevPt == null) {
                this.prevPt = currPt;
            }
            double dx = currPt.x - this.prevPt.x;
            double dy = currPt.y - this.prevPt.y;
            this.prevPt = currPt;

            System.out.println("Dragging: dx=" + dx + ", dy=" + dy);

            // Move selected objects
            ArrayList<notePtCurve> selectedCurves =
                note.getPtCurveMgr().getSelectedPtCurves();
            for (notePtCurve curve : selectedCurves) {
                curve.translateTo(dx, dy);
            }
            note.getBoundingBox().translateTo(dx, dy);

            // Update BoundingBox
            note.getCanvas2D().updateBoundingBox();
            note.getCanvas2D().repaint();
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            prevPt = null;
            // End the pen mark
//            note.getPenMarkMgr().endMark();
//            System.out.println("Mouse Released. Pen mark ended.");
            XCmdToChangeScene.execute(note,
                        noteSelectScenario.SelectedScene.getSingleton(), null);
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
            note.getCanvas2D().drawBoundingBox(g2);
            note.getCanvas2D().repaint();
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        
        }
    }
    
    /////////////////////////////////////////////////////////
    
    public static class ScaleScene extends noteScene {
        private Point2D.Double prevPt = null;
        
        // singleton pattern
        private static ScaleScene mSingleton = null;
        public static ScaleScene getSingleton() {
            assert(ScaleScene.mSingleton != null);
            return ScaleScene.mSingleton;
        }
        public static ScaleScene createSingleton(XScenario scenario) {
            assert(ScaleScene.mSingleton == null);
            ScaleScene.mSingleton = new ScaleScene(scenario);
            return ScaleScene.mSingleton;
        }
        private ScaleScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
           
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D.Double currPt = new Point2D.Double(e.getX(), e.getY());
//            if (this.prevPt == null) {
//                this.prevPt = currPt;
//            }
            double dx = currPt.x - this.prevPt.x;
            double dy = currPt.y - this.prevPt.y;
            if (note.getBoundingBox().getWidth() >= 20 &&
                note.getBoundingBox().getHeight() >= 20) {
                noteCmdToScaleObject.execute(note, dx, dy);
            } else {
                if (dx > 0 ){
                noteCmdToScaleObject.execute(note, dx, dy);
            }
            }
            this.prevPt = currPt;
            System.out.println(dx+", "+dy+", and prevPt is "+ prevPt);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            prevPt = null;
            XCmdToChangeScene.execute(note,
                        noteSelectScenario.SelectedScene.getSingleton(), null);
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
            note.getCanvas2D().drawBoundingBox(g2);
            note.getCanvas2D().repaint();
        }

        @Override
        public void getReady() {
        noteApp note = (noteApp) this.mScenario.getApp();
        if (note.getPenMarkMgr().getCurrPenMark() != null) {
            this.prevPt = note.getPenMarkMgr().getCurrPenMark().getStartPoint();
            System.out.println("Set prev Pt: " +this.prevPt);
        } else {
            System.out.println("tlqkf");
        }
        }

        @Override
        public void wrapUp() {
        
        }
    }
    
}
