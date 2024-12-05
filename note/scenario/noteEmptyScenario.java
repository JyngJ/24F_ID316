package note.scenario;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import note.noteScene;
import x.XApp;
import x.XScenario;

public class noteEmptyScenario extends XScenario {
    // singleton
    private static noteEmptyScenario mSingleton = null;
    public static noteEmptyScenario getSingleton() {
        assert(noteEmptyScenario.mSingleton != null);
        return noteEmptyScenario.mSingleton;
    }
    public static noteEmptyScenario createSingleton(XApp app) {
        assert(noteEmptyScenario.mSingleton == null);
        noteEmptyScenario.mSingleton = new noteEmptyScenario(app);
        return noteEmptyScenario.mSingleton;
    }
    private noteEmptyScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteEmptyScenario.EmptyScene.createSingleton(this));
    }

    public static class EmptyScene extends noteScene {
        // singleton pattern
        private static EmptyScene mSingleton = null;
        public static EmptyScene getSingleton() {
            assert(EmptyScene.mSingleton != null);
            return EmptyScene.mSingleton;
        }
        public static EmptyScene createSingleton(XScenario scenario) {
            assert(EmptyScene.mSingleton == null);
            EmptyScene.mSingleton = new EmptyScene(scenario);
            return EmptyScene.mSingleton;
        }
        private EmptyScene(XScenario scenario) {
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
