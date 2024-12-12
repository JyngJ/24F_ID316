package note.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToCreatePtCurve;
import note.noteApp;
import note.noteScene;
import x.*;

public class noteDefaultScenario extends XScenario {

    // singleton
    private static noteDefaultScenario mSingleton = null;

    public static noteDefaultScenario getSingleton() {
        assert (noteDefaultScenario.mSingleton != null);
        return noteDefaultScenario.mSingleton;
    }

    public static noteDefaultScenario createSingleton(XApp app) {
        assert (noteDefaultScenario.mSingleton == null);
        noteDefaultScenario.mSingleton = new noteDefaultScenario(app);
        return noteDefaultScenario.mSingleton;
    }

    protected noteDefaultScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteDefaultScenario.ReadyScene.createSingleton(this));
    }

    public static class ReadyScene extends noteScene {

        public ArrayList<Point2D> points;

        // singleton pattern
        private static ReadyScene mSingleton = null;

        public static ReadyScene getSingleton() {
            assert (ReadyScene.mSingleton != null);
            return ReadyScene.mSingleton;
        }

        public static ReadyScene createSingleton(XScenario scenario) {
            assert (ReadyScene.mSingleton == null);
            ReadyScene.mSingleton = new ReadyScene(scenario);
            return ReadyScene.mSingleton;
        }

        private ReadyScene(XScenario scenario) {
            super(scenario);
            points = new ArrayList<>();
        }

        public void handleMousePress(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            // Is on ColorChooser?
            if (pt.x >= 22 && pt.x < 105 && pt.y <= 60 && pt.y > 40) {
            } else {
                noteCmdToCreatePtCurve.execute(note, pt);
                XCmdToChangeScene.execute(note,
                        noteDrawScenario.DrawScene.getSingleton(), this);
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {

        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point pt = e.getPoint();
            Color c = note.getColorChooser().calcColor(pt,
                    note.getCanvas2D().getWidth(),
                    note.getCanvas2D().getHeight());
            if (c != null) { // On ColorChooser
                note.getCanvas2D().setCurrColorForPtCurve(c);
            }
            note.getPtCurveMgr().setCurrPtCurve(null);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_F:             // will be changed
                    XCmdToChangeScene.execute(note,
                            noteFormulaDrawScenario.FormulaReadyScene.getSingleton(), this);
                    break;
                case KeyEvent.VK_E:
                    XCmdToChangeScene.execute(note,
                            noteDrawScenario.EraseScene.getSingleton(), this);
                    break;
                case KeyEvent.VK_SHIFT:
                    XCmdToChangeScene.execute(note,
                            noteSelectScenario.SelectScene.getSingleton(), this);
                    break;
            }
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
