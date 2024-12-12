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
            noteCmdToSelectObject.execute((XApp) this.mScenario.getApp(), this.dragPath);
            this.dragPath.clear();
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
                      
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    XCmdToChangeScene.execute(this.mScenario.getApp(),
                    noteDefaultScenario.ReadyScene.getSingleton(), null);
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
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
        
        }
    }
    
    
    public static class SelectedScene extends noteScene {
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
        
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
        
        }
    }
}
