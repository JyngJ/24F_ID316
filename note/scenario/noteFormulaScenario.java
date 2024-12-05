package note.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import note.noteApp;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteFormulaScenario extends XScenario {
    // singleton
    private static noteFormulaScenario mSingleton = null;
    public static noteFormulaScenario getSingleton() {
        assert(noteFormulaScenario.mSingleton != null);
        return noteFormulaScenario.mSingleton;
    }
    public static noteFormulaScenario createSingleton(XApp app) {
        assert(noteFormulaScenario.mSingleton == null);
        noteFormulaScenario.mSingleton = new noteFormulaScenario(app);
        return noteFormulaScenario.mSingleton;
    }
    private noteFormulaScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteFormulaScenario.FormulaReadyScene.createSingleton(this));
        this.addScene(noteFormulaScenario.FormulaDrawScene.createSingleton(this));
    }

    public static class FormulaReadyScene extends noteScene {
        // singleton pattern
        private static FormulaReadyScene mSingleton = null;
        public static FormulaReadyScene getSingleton() {
            assert(FormulaReadyScene.mSingleton != null);
            return FormulaReadyScene.mSingleton;
        }
        public static FormulaReadyScene createSingleton(XScenario scenario) {
            assert(FormulaReadyScene.mSingleton == null);
            FormulaReadyScene.mSingleton = new FormulaReadyScene(scenario);
            return FormulaReadyScene.mSingleton;
        }
        private FormulaReadyScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
           noteApp note = (noteApp) this.mScenario.getApp();
//           Point pt = e.getPoint();
           
           XCmdToChangeScene.execute(note,
                noteFormulaScenario.FormulaDrawScene.getSingleton(), this.mReturnScene);
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
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_F:             // will be changed
                    XCmdToChangeScene.execute(note, 
                        mReturnScene, null);
                    break;
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
    
    public static class FormulaDrawScene extends noteScene {
        // singleton pattern
        private static FormulaDrawScene mSingleton = null;
        public static FormulaDrawScene getSingleton() {
            assert(FormulaDrawScene.mSingleton != null);
            return FormulaDrawScene.mSingleton;
        }
        public static FormulaDrawScene createSingleton(XScenario scenario) {
            assert(FormulaDrawScene.mSingleton == null);
            FormulaDrawScene.mSingleton = new FormulaDrawScene(scenario);
            return FormulaDrawScene.mSingleton;
        }
        private FormulaDrawScene(XScenario scenario) {
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
            noteApp note = (noteApp) this.mScenario.getApp();
            XCmdToChangeScene.execute(note,
                noteFormulaScenario.FormulaReadyScene.getSingleton(), this.mReturnScene);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
            
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_F:             // will be changed
                    XCmdToChangeScene.execute(note, 
                        mReturnScene, null);
                    break;
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
