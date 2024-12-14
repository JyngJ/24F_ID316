package note.scenario;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaEdge;
import note.noteFormulaMgr;
import note.notePenMarkMgr;
import note.noteScene;
import note.cmd.noteCmdToPromoteEdge;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteFormulaEditScenario extends XScenario {

    // singleton
    private static noteFormulaEditScenario mSingleton = null;

    public static noteFormulaEditScenario getSingleton() {
        assert (noteFormulaEditScenario.mSingleton != null);
        return noteFormulaEditScenario.mSingleton;
    }

    public static noteFormulaEditScenario createSingleton(XApp app) {
        assert (noteFormulaEditScenario.mSingleton == null);
        noteFormulaEditScenario.mSingleton = new noteFormulaEditScenario(app);
        return noteFormulaEditScenario.mSingleton;
    }

    private noteFormulaEditScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteFormulaEditScenario.noteFormulaEditReadyScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaMoveScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaAtomEditScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaEdgeEditScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaGestureScene.createSingleton(this));
    }

    // formula가 선택된 상태
    public static class noteFormulaEditReadyScene extends noteScene {

        // singleton pattern
        private static noteFormulaEditReadyScene mSingleton = null;

        public static noteFormulaEditReadyScene getSingleton() {
            assert (noteFormulaEditReadyScene.mSingleton != null);
            return noteFormulaEditReadyScene.mSingleton;
        }

        public static noteFormulaEditReadyScene createSingleton(XScenario scenario) {
            assert (noteFormulaEditReadyScene.mSingleton == null);
            noteFormulaEditReadyScene.mSingleton = new noteFormulaEditReadyScene(scenario);
            return noteFormulaEditReadyScene.mSingleton;
        }

        private noteFormulaEditReadyScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
            noteFormulaMgr formulaMgr = note.getFormulaMgr();

            // 편집 중인 formula의 edge 터치 체크
            noteFormula editingFormula = note.getFormulaMgr().getEditingFormula();
            if (editingFormula != null) {
                for (noteFormulaEdge edge : editingFormula.getEdges()) {
                    if (edge.isTouchedBy(pt)) {
                        // Edge가 터치되면 Scene으로 전환
                        formulaMgr.setEditingEdge(edge);
                        XCmdToChangeScene.execute(note,
                            noteFormulaEditScenario.noteFormulaEdgeEditScene.getSingleton(), this.mReturnScene);
                        return;
                    }
                }
                for (noteFormulaAtom atom : editingFormula.getAtoms()) {
                    if (atom.isTouchedBy(pt)) {
                        // Atom이 터치되면 AtomEditScene으로 전환 
                        formulaMgr.setEditingAtom(atom);
                        XCmdToChangeScene.execute(note,
                            noteFormulaEditScenario.noteFormulaAtomEditScene.getSingleton(), this.mReturnScene);        //여기 
                        return;
                    }
                }
            }
            XCmdToChangeScene.execute(note,
                noteFormulaEditScenario.noteFormulaGestureScene.getSingleton(), this.mReturnScene);
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
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            
            switch (code) {
                case KeyEvent.VK_ESCAPE:
                    // 편집 모드 해제
                    formulaMgr.setEditingFormula(null);

                    // 이전 씬으로 돌아가기
                    XCmdToChangeScene.execute(note, this.mReturnScene, null);
                    break;
                case KeyEvent.VK_E:
                    formulaMgr.removeFormula(formulaMgr.getEditingFormula());
                    formulaMgr.setEditingFormula(null);
                    XCmdToChangeScene.execute(note, this.mReturnScene, null);
            }
            note.getCanvas2D().repaint();
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
    
    // double, triple edge로 변환하는 Scene
    public static class noteFormulaEdgeEditScene extends noteScene {
        // singleton pattern
        private static noteFormulaEdgeEditScene mSingleton = null;

        public static noteFormulaEdgeEditScene getSingleton() {
            assert (noteFormulaEdgeEditScene.mSingleton != null);
            return noteFormulaEdgeEditScene.mSingleton;
        }

        public static noteFormulaEdgeEditScene createSingleton(XScenario scenario) {
            assert (noteFormulaEdgeEditScene.mSingleton == null);
            noteFormulaEdgeEditScene.mSingleton = new noteFormulaEdgeEditScene(scenario);
            return noteFormulaEdgeEditScene.mSingleton;
        }

        private noteFormulaEdgeEditScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            
            // 드래그 하는 범위가 touchArea를 벗어나면 이동으로 간주, moveScene으로 이동 
            if (formulaMgr.getEditingEdge().isTouchedBy(pt) != true) {
                formulaMgr.setEditingEdge(null);
                XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.noteFormulaMoveScene.getSingleton(), this.mReturnScene);
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            notePenMarkMgr penMarkMgr = note.getPenMarkMgr();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            
            if (penMarkMgr.wasLastMarkStraight() == true) {
                noteCmdToPromoteEdge.execute(note, formulaMgr.getEditingEdge());
                XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this.mReturnScene);
            }
            XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this.mReturnScene);
            
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
    
    // 선택된 formula를 이동하는 상태
    // formula의 엣지를 잡고 터치에리어 밖까지 드래그했을때 여기로 들어와야 함 
    public static class noteFormulaMoveScene extends noteScene {

        // singleton pattern
        private static noteFormulaMoveScene mSingleton = null;

        public static noteFormulaMoveScene getSingleton() {
            assert (noteFormulaMoveScene.mSingleton != null);
            return noteFormulaMoveScene.mSingleton;
        }

        public static noteFormulaMoveScene createSingleton(XScenario scenario) {
            assert (noteFormulaMoveScene.mSingleton == null);
            noteFormulaMoveScene.mSingleton = new noteFormulaMoveScene(scenario);
            return noteFormulaMoveScene.mSingleton;
        }

        private noteFormulaMoveScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();

            // formula 이동
            noteFormula editingFormula = note.getFormulaMgr().getEditingFormula();
            if (editingFormula != null && note.getPenMarkMgr().isDragging()) {
                Point2D.Double relativeDistance = note.getPenMarkMgr().getRelativeDistance();
                if (relativeDistance != null) {
                    editingFormula.translateTo(relativeDistance.x, relativeDistance.y);
                    note.getCanvas2D().repaint();
                }
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();

            XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this.mReturnScene);

            note.getCanvas2D().repaint();
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
            // getReady에서 초기화하지 않음
        }

        @Override
        public void wrapUp() {

        }
    }

    // 선택된 formula의 한 Atom을 이동시키는 상태 / Atom을 바꿀 때  
    public static class noteFormulaAtomEditScene extends noteScene {

        // singleton pattern
        private static noteFormulaAtomEditScene mSingleton = null;

        public static noteFormulaAtomEditScene getSingleton() {
            assert (noteFormulaAtomEditScene.mSingleton != null);
            return noteFormulaAtomEditScene.mSingleton;
        }

        public static noteFormulaAtomEditScene createSingleton(XScenario scenario) {
            assert (noteFormulaAtomEditScene.mSingleton == null);
            noteFormulaAtomEditScene.mSingleton = new noteFormulaAtomEditScene(scenario);
            return noteFormulaAtomEditScene.mSingleton;
        }

        private noteFormulaAtomEditScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {

        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            
            // 드래그 하는 범위가 touchArea를 벗어나면 이동으로 간주, moveScene으로 이동 
            if (formulaMgr.getEditingAtom().isTouchedBy(pt) != true) {
                formulaMgr.setEditingAtom(null);
                XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.noteFormulaMoveScene.getSingleton(), this.mReturnScene);
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            notePenMarkMgr penMarkMgr = note.getPenMarkMgr();
            Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            noteFormula editingFormula = formulaMgr.getEditingFormula();

            // 제스처가 짧으면 해당 Atom 타입 변경 
            if (penMarkMgr.wasShortTab() == true) {
                for (noteFormulaAtom atom : editingFormula.getAtoms()) {
                    if (atom.isTouchedBy(pt)) {
                        
                        // atom 타입에 따라 순환 
                        String at = atom.getType();
                        switch (at){
                            case "C":
                                atom.setType("O");
                                break;
                            case "O":
                                atom.setType("N");
                                break;
                            case "N":
                                atom.setType("C");
                                break;
                        }
                        formulaMgr.setEditingAtom(null);
                        XCmdToChangeScene.execute(note,
                            noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this.mReturnScene);
                        return;
                    }
                }
                return;
            }
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

    // 나가기 / 엣지 삭제가 일어날 씬 
    // 엣지도, 아톰도 아닌 다른 곳을 클릭했을때 이곳으로 들어옴
    public static class noteFormulaGestureScene extends noteScene {

        // singleton pattern
        private static noteFormulaGestureScene mSingleton = null;
        private Point2D.Double mStartPt = null;

        public static noteFormulaGestureScene getSingleton() {
            assert (noteFormulaGestureScene.mSingleton != null);
            return noteFormulaGestureScene.mSingleton;
        }

        public static noteFormulaGestureScene createSingleton(XScenario scenario) {
            assert (noteFormulaGestureScene.mSingleton == null);
            noteFormulaGestureScene.mSingleton = new noteFormulaGestureScene(scenario);
            return noteFormulaGestureScene.mSingleton;
        }

        private noteFormulaGestureScene(XScenario scenario) {
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
            notePenMarkMgr penMarkMgr = note.getPenMarkMgr();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();

            // 제스처가 짧으면 (탭) 바로 리턴
            if (penMarkMgr.wasShortTab() == true) {
                note.getFormulaMgr().setEditingFormula(null);
                XCmdToChangeScene.execute(note, this.mReturnScene, null);
                return;
            }

//            // 현재 작업 중인 formula의 엣지 삭제 로직
//            noteFormula editingFormula = formulaMgr.getEditingFormula();
//            if (editingFormula != null) {
//                noteFormulaEdge edgeToRemove = formulaMgr.findIntersectingEdge(editingFormula, penMarkMgr.getCurrPenMark());
//                if (edgeToRemove != null) {
//                    editingFormula.removeEdge(edgeToRemove);
//                    note.getCanvas2D().repaint();
//                }
//            }

           
            // 씬 복귀
            XCmdToChangeScene.execute(note, noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this.mReturnScene);
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
