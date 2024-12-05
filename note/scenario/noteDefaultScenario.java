package note.scenario;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import note.noteApp;
import x.*;
import note.noteScene;

public class noteDefaultScenario extends XScenario {
    // singleton
    private static noteDefaultScenario mSingleton = null;
    public static noteDefaultScenario getSingleton() {
        assert(noteDefaultScenario.mSingleton != null);
        return noteDefaultScenario.mSingleton;
    }
    public static noteDefaultScenario createSingleton(XApp app) {
        assert(noteDefaultScenario.mSingleton == null);
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
        // singleton pattern
        private static ReadyScene mSingleton = null;
        public static ReadyScene getSingleton() {
            assert(ReadyScene.mSingleton != null);
            return ReadyScene.mSingleton;
        }
        public static ReadyScene createSingleton(XScenario scenario) {
            assert(ReadyScene.mSingleton == null);
            ReadyScene.mSingleton = new ReadyScene(scenario);
            return ReadyScene.mSingleton;
        }
        private ReadyScene(XScenario scenario) {
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
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_F:             // will be changed
                    XCmdToChangeScene.execute(note, 
                        noteFormulaScenario.FormulaReadyScene.getSingleton(), this);
                    break;
            }
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
//                case KeyEvent.VK_F:             // will be changed
//                    XCmdToChangeScene.execute(note, 
//                        noteFormulaScenario.FormulaReadyScene.getSingleton(), this);
//                    break;
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
        }

        @Override
        public void getReady() {
        }

        @Override
        public void wrapUp() {
        }
    
    }
}
