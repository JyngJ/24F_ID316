package note.scenario;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import note.cmd.noteCmdToCreateAtom;
import note.cmd.noteCmdToMergeFormula;
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
            Point pt = e.getPoint(); // MouseEvent에서 가져온 Point
            Point2D.Double point2D 
                = new Point2D.Double(pt.x, pt.y);    //실수형으로 변환 


            // Formula 리스트에서 클릭 위치가 Atom의 터치 영역에 있는지 확인
            for (noteFormula formula : formulaMgr.getFormulas()) {
                for (noteFormulaAtom atom : formula.getAtoms()) {
                    if (atom.getTouchArea().contains(pt)) {
                        // 클릭 위치가 Atom의 터치 영역 안에 있음
                        formulaMgr.setCurrAtom(atom);
                        formulaMgr.setCurrFormula(formula);

                        // 씬 전환
                        XCmdToChangeScene.execute
                            (note, 
                            noteFormulaScenario.FormulaDrawScene.getSingleton(), 
                            this.mReturnScene);
                        return;
                    }
                }
            }

            noteFormulaAtom newAtom 
                = noteCmdToCreateAtom.execute(note, point2D);

            formulaMgr.setCurrAtom(newAtom);
            formulaMgr.setCurrFormula(
                formulaMgr.findFormulaForAtom(newAtom));

            // 씬 전환
            XCmdToChangeScene.execute(note,
                noteFormulaScenario.FormulaDrawScene.getSingleton(), 
                this.mReturnScene);
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
            noteFormulaMgr formulaMgr = note.getFormulaMgr();

            int mouseX = e.getX();
            int mouseY = e.getY();
            Point2D.Double releasePoint = new Point2D.Double(mouseX, mouseY);

            noteFormulaAtom currAtom = formulaMgr.getCurrAtom();
            noteFormula currFormula = formulaMgr.getCurrFormula();

            for (noteFormula formula : formulaMgr.getFormulas()) {
                for (noteFormulaAtom atom : formula.getAtoms()) {
                    if (atom.getTouchArea().contains(releasePoint)) {
                        // 두 Atom을 Edge로 연결
                        noteFormulaEdge newEdge = new noteFormulaEdge(currAtom, atom);
                        currFormula.addEdge(newEdge);

                        // 두 Formula를 병합
                        noteCmdToMergeFormula.execute(note, currFormula, formula);

                        // 씬 전환
                        formulaMgr.setCurrFormula(null);
                        formulaMgr.setCurrAtom(null);

                        XCmdToChangeScene.execute(note,
                            noteFormulaScenario.FormulaReadyScene.getSingleton(), this.mReturnScene);
                        return;
                    }
                }
            }

            // 릴리즈 위치가 기존 Atom의 터치 영역에 없으면 명령어를 사용해 새 Atom 생성
            noteFormulaAtom newAtom = noteCmdToCreateAtom.execute(note, releasePoint);

            // 현재 Atom과 새 Atom을 Edge로 연결
            noteFormulaEdge newEdge = new noteFormulaEdge(currAtom, newAtom);
            currFormula.addEdge(newEdge);

            formulaMgr.setCurrFormula(null);
            formulaMgr.setCurrAtom(null);

            System.out.println("Created new Atom and connected it.");

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
