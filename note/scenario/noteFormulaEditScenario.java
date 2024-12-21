package note.scenario;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToDemoteEdge;
import note.cmd.noteCmdToPromoteEdge;
import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaEdge;
import note.noteFormulaMgr;
import note.notePenMarkMgr;
import note.noteScene;
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
        this.addScene(noteFormulaEditScenario.FormulaEditReadyScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.FormulaMoveScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.FormulaAtomEditScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.FormulaEdgeEditScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.FormulaGestureScene.createSingleton(this));
    }

    // formula가 선택된 상태
    public static class FormulaEditReadyScene extends noteScene {

        // singleton pattern
        private static FormulaEditReadyScene mSingleton = null;

        public static FormulaEditReadyScene getSingleton() {
            assert (FormulaEditReadyScene.mSingleton != null);
            return FormulaEditReadyScene.mSingleton;
        }

        public static FormulaEditReadyScene createSingleton(XScenario scenario) {
            assert (FormulaEditReadyScene.mSingleton == null);
            FormulaEditReadyScene.mSingleton = new FormulaEditReadyScene(scenario);
            return FormulaEditReadyScene.mSingleton;
        }

        private FormulaEditReadyScene(XScenario scenario) {
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
                                noteFormulaEditScenario.FormulaEdgeEditScene.getSingleton(), this);
                        return;
                    }
                }
                for (noteFormulaAtom atom : editingFormula.getAtoms()) {
                    if (atom.isTouchedBy(pt)) {
                        // Atom이 터치되면 AtomEditScene으로 전환 
                        formulaMgr.setEditingAtom(atom);
                        XCmdToChangeScene.execute(note,
                                noteFormulaEditScenario.FormulaAtomEditScene.getSingleton(), this);        //여기 
                        return;
                    }
                }
            }
            XCmdToChangeScene.execute(note,
                    noteFormulaEditScenario.FormulaGestureScene.getSingleton(), this);
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
                    XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(),
                            null);
                    break;
                case KeyEvent.VK_E:
                    formulaMgr.removeFormula(formulaMgr.getEditingFormula());
                    formulaMgr.setEditingFormula(null);

                    XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(),
                            null);
                    break;
                case KeyEvent.VK_F:
                    XCmdToChangeScene.execute(note,
                            noteFormulaDrawScenario.FormulaAppendReadyScene.getSingleton(),
                            this);
                    break;
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
    public static class FormulaEdgeEditScene extends noteScene {

        // singleton pattern
        private static FormulaEdgeEditScene mSingleton = null;

        public static FormulaEdgeEditScene getSingleton() {
            assert (FormulaEdgeEditScene.mSingleton != null);
            return FormulaEdgeEditScene.mSingleton;
        }

        public static FormulaEdgeEditScene createSingleton(XScenario scenario) {
            assert (FormulaEdgeEditScene.mSingleton == null);
            FormulaEdgeEditScene.mSingleton = new FormulaEdgeEditScene(scenario);
            return FormulaEdgeEditScene.mSingleton;
        }

        private FormulaEdgeEditScene(XScenario scenario) {
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
                        noteFormulaEditScenario.FormulaMoveScene.getSingleton(), this.mReturnScene);
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
                        this.mReturnScene, null);
            }
            XCmdToChangeScene.execute(note,
                    this.mReturnScene, null);

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
    public static class FormulaMoveScene extends noteScene {

        // singleton pattern
        private static FormulaMoveScene mSingleton = null;

        public static FormulaMoveScene getSingleton() {
            assert (FormulaMoveScene.mSingleton != null);
            return FormulaMoveScene.mSingleton;
        }

        public static FormulaMoveScene createSingleton(XScenario scenario) {
            assert (FormulaMoveScene.mSingleton == null);
            FormulaMoveScene.mSingleton = new FormulaMoveScene(scenario);
            return FormulaMoveScene.mSingleton;
        }

        private FormulaMoveScene(XScenario scenario) {
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
                    this.mReturnScene, null);

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
    public static class FormulaAtomEditScene extends noteScene {

        // singleton pattern
        private static FormulaAtomEditScene mSingleton = null;

        public static FormulaAtomEditScene getSingleton() {
            assert (FormulaAtomEditScene.mSingleton != null);
            return FormulaAtomEditScene.mSingleton;
        }

        public static FormulaAtomEditScene createSingleton(XScenario scenario) {
            assert (FormulaAtomEditScene.mSingleton == null);
            FormulaAtomEditScene.mSingleton = new FormulaAtomEditScene(scenario);
            return FormulaAtomEditScene.mSingleton;
        }

        private FormulaAtomEditScene(XScenario scenario) {
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
            noteFormulaAtom editingAtom = formulaMgr.getEditingAtom();

            // 드래그 하는 범위가 touchArea를 벗어나면 이동으로 간주
            if (editingAtom != null) {
                if (editingAtom.isTouchedBy(pt) != true) {
                    editingAtom.setPosition(pt);
                    note.getCanvas2D().repaint();
                }
            } else {
                XCmdToChangeScene.execute(note,
                                this.mReturnScene, null);
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
                        switch (at) {
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
                        return;
                    }
                }
                return;
            }
            XCmdToChangeScene.execute(note,
                                this.mReturnScene, null);
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
    public static class FormulaGestureScene extends noteScene {

        // singleton pattern
        private static FormulaGestureScene mSingleton = null;
        private Point2D.Double mStartPt = null;

        public static FormulaGestureScene getSingleton() {
            assert (FormulaGestureScene.mSingleton != null);
            return FormulaGestureScene.mSingleton;
        }

        public static FormulaGestureScene createSingleton(XScenario scenario) {
            assert (FormulaGestureScene.mSingleton == null);
            FormulaGestureScene.mSingleton = new FormulaGestureScene(scenario);
            return FormulaGestureScene.mSingleton;
        }

        private FormulaGestureScene(XScenario scenario) {
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
            noteFormula editingFormula = formulaMgr.getEditingFormula();

            // 제스처가 짧으면 (탭) 바로 리턴
            if (penMarkMgr.wasShortTab()) {
                note.getFormulaMgr().setEditingFormula(null);
                XCmdToChangeScene.execute(note, noteDefaultScenario.ReadyScene.getSingleton(), null);
                return;
            }

            // 마지막 PenMark가 직선인지 확인
            if (penMarkMgr.wasLastMarkStraight()) {
                // 현재 작업 중인 formula의 엣지 삭제 로직
                // 인터섹트하는 엣지 찾기
                ArrayList<noteFormulaEdge> edgesToRemove = formulaMgr.findIntersectingEdge(editingFormula, penMarkMgr.getLastPenMark());
                System.out.println(edgesToRemove);      //debug
                if (edgesToRemove.size() == 1) {
                    // 인터섹트하는 엣지가 하나일 경우
                    noteCmdToDemoteEdge.execute(note, edgesToRemove.get(0));
                    note.getCanvas2D().repaint();
                }
            }

            // editFormula 확인 후 씬 복귀
            if(formulaMgr.getEditingFormula() == null){
                XCmdToChangeScene.execute(note, noteDefaultScenario.ReadyScene.getSingleton(), null);
            } else {
                XCmdToChangeScene.execute(note, this.mReturnScene, null);
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

}
