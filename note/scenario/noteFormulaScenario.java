package note.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import note.noteApp;
import note.noteCanvas2D;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaEdge;
import note.noteFormulaMgr;
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
            noteFormulaMgr formulaMgr = note.getFormulaMgr();

            // 클릭 위치 가져오기
            Point pt = e.getPoint();

            // Formula 리스트에서 클릭 위치가 Atom의 터치 영역에 있는지 확인
            for (noteFormula formula : formulaMgr.getFormulas()) {
                for (noteFormulaAtom atom : formula.getAtoms()) {
                    if (atom.getTouchArea().contains(pt)) {
                        // 클릭 위치가 Atom의 터치 영역 안에 있음
                        formulaMgr.setCurrAtom(atom);
                        formulaMgr.setCurrFormula(formula);

                        System.out.println("Clicked on existing Atom at: " + atom.getPosition());

                        // 씬 전환
                        XCmdToChangeScene.execute(note,
                            noteFormulaScenario.FormulaDrawScene.getSingleton(), this.mReturnScene);
                        return;
                    }
                }
            }

            // 클릭 위치에 Atom이 없으면 새로운 Atom 생성
            noteFormulaAtom newAtom = new noteFormulaAtom(
                "C",
                new Point2D.Double(pt.x, pt.y));

            // 새로운 Formula 생성
            noteFormula newFormula = new noteFormula();
            newFormula.addAtom(newAtom);

            formulaMgr.setCurrAtom(newAtom);
            formulaMgr.setCurrFormula(newFormula);

            System.out.println("Created new Atom at (" + pt.x + ", " + pt.y + ") and added to a new Formula.");

            // 씬 전환
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
            
            int mouseX = e.getX();
            int mouseY = e.getY();

            // Atom 생성 (타입: C)
            noteFormulaAtom newAtom = new noteFormulaAtom(
                "C",
                new Point2D.Double(mouseX, mouseY));
            
            noteFormulaAtom currAtom = note.getFormulaMgr().getCurrAtom();
            
            noteFormulaEdge newEdge = new noteFormulaEdge(currAtom , newAtom);
            
            noteFormula currFormula = note.getFormulaMgr().getCurrFormula();
            
            currFormula.addAtom(newAtom);
            currFormula.addEdge(newEdge);
            
            note.getFormulaMgr().getFormulas().add(currFormula);
            note.getFormulaMgr().setCurrFormula(null);
            note.getFormulaMgr().setCurrAtom(null);
            
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
