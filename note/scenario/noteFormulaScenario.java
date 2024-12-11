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
import note.noteFormulaAtomTemp;
import note.noteFormulaEdgeSingle;
import note.noteFormulaEdgeTemp;
import note.noteFormulaMgr;
import note.noteFormulaRenderer;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteFormulaScenario extends XScenario {

    // singleton
    private static noteFormulaScenario mSingleton = null;

    public static noteFormulaScenario getSingleton() {
        assert (noteFormulaScenario.mSingleton != null);
        return noteFormulaScenario.mSingleton;
    }

    public static noteFormulaScenario createSingleton(XApp app) {
        assert (noteFormulaScenario.mSingleton == null);
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
            assert (FormulaReadyScene.mSingleton != null);
            return FormulaReadyScene.mSingleton;
        }

        public static FormulaReadyScene createSingleton(XScenario scenario) {
            assert (FormulaReadyScene.mSingleton == null);
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

                        noteFormulaAtomTemp tempAtom = new noteFormulaAtomTemp("temp", point2D);
                        formulaMgr.setAtomTemp(tempAtom);
                        noteFormulaEdgeTemp tempEdge = new noteFormulaEdgeTemp(atom, tempAtom);
                        formulaMgr.setEdgeTemp(tempEdge);

                        // 씬 전환
                        XCmdToChangeScene.execute(note,
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

            noteFormulaAtomTemp tempAtom = new noteFormulaAtomTemp("temp", point2D);
            formulaMgr.setAtomTemp(tempAtom);
            noteFormulaEdgeTemp tempEdge = new noteFormulaEdgeTemp(newAtom, tempAtom);
            formulaMgr.setEdgeTemp(tempEdge);

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
            assert (FormulaDrawScene.mSingleton != null);
            return FormulaDrawScene.mSingleton;
        }

        public static FormulaDrawScene createSingleton(XScenario scenario) {
            assert (FormulaDrawScene.mSingleton == null);
            FormulaDrawScene.mSingleton = new FormulaDrawScene(scenario);
            return FormulaDrawScene.mSingleton;
        }

        private FormulaDrawScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();

            // 새로운 작업 시작시 현재 작업 리스트 초기화
            formulaMgr.clearPrevElements();

            // 시작 아톰 추가
            noteFormulaAtom startAtom = formulaMgr.getCurrAtom();
            if (startAtom != null) {
                formulaMgr.addPrevAtom(startAtom);
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            noteCanvas2D canvas = note.getCanvas2D();

            // 현재 마우스 위치
            Point2D.Double currentPoint = new Point2D.Double(e.getX(), e.getY());
            noteFormulaAtom currAtom = formulaMgr.getCurrAtom();

            // EdgeTemp 터치 확인 및 거리 체크
            noteFormulaEdgeTemp edgeTemp = formulaMgr.getEdgeTemp();
            if (edgeTemp != null && edgeTemp.containsPoint(currentPoint)) {
                // 현재 마우스 위치와 currAtom과의 거리 계산
                Point2D.Double startPos = currAtom.getPosition();
                double dx = currentPoint.x - startPos.x;
                double dy = currentPoint.y - startPos.y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // 거리가 충분할 경우 새 Atom과 Edge 생성
                if (distance >= noteFormulaRenderer.LENGTH_EDGE_DEFAULT / 4) {
                    // AtomTemp 위치에 새 Atom 생성
                    Point2D.Double tempPos = formulaMgr.getAtopTemp().getPosition();
                    noteFormulaAtom newAtom = noteCmdToCreateAtom.execute(note, tempPos);

                    // 현재 Formula 가져오기
                    noteFormula currFormula = formulaMgr.getCurrFormula();

                    // 새 Edge 생성 및 Formula에 추가
                    noteFormulaEdgeSingle newEdge = new noteFormulaEdgeSingle(currAtom, newAtom);
                    currFormula.addEdge(newEdge);

                    // 이전 작업 리스트에 추가
                    formulaMgr.addPrevEdge(newEdge);
                    formulaMgr.addPrevAtom(newAtom);

                    // currAtom을 새로 만든 Atom으로 변경
                    formulaMgr.setCurrAtom(newAtom);

                    // EdgeTemp 업데이트
                    noteFormulaAtomTemp tempAtom = formulaMgr.getAtopTemp();
                    formulaMgr.setEdgeTemp(new noteFormulaEdgeTemp(newAtom, tempAtom));

                    canvas.repaint();
                }
            }

            // 기존 코드 계속 실행
            boolean isOverAtom = false;
            for (noteFormula formula : formulaMgr.getFormulas()) {
                for (noteFormulaAtom atom : formula.getAtoms()) {
                    if (atom != currAtom && atom.getTouchArea().contains(currentPoint)) {
                        // 다른 Atom 위에 있으면 해당 Atom으로 직접 연결
                        formulaMgr.getAtopTemp().setPosition(atom.getPosition());
                        isOverAtom = true;
                        break;
                    }
                }
                if (isOverAtom) {
                    break;
                }
            }

            if (!isOverAtom) {
                formulaMgr.getAtopTemp().setPosition(currentPoint);
                noteFormulaRenderer renderer = canvas.getRenderer();
                Point2D.Double startPos = currAtom.getPosition();
                Point2D.Double direction = renderer.calculateSnapPoint(
                        startPos,
                        formulaMgr.getAtopTemp().getPosition()
                );

                double dx = direction.x - startPos.x;
                double dy = direction.y - startPos.y;
                double length = Math.sqrt(dx * dx + dy * dy);
                if (length > 0) {
                    double scale = noteFormulaRenderer.LENGTH_EDGE_DEFAULT / length;
                    Point2D.Double snapPoint = new Point2D.Double(
                            startPos.x + dx * scale,
                            startPos.y + dy * scale
                    );
                    formulaMgr.getAtopTemp().setPosition(snapPoint);
                }
            }

            canvas.repaint();
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            noteCanvas2D canvas = note.getCanvas2D();

            Point2D.Double releasePoint = new Point2D.Double(e.getX(), e.getY());
            noteFormulaAtom currAtom = formulaMgr.getCurrAtom();
            noteFormula currFormula = formulaMgr.getCurrFormula();

            // 임시 객체들 정리
            noteFormulaAtomTemp tempAtom = formulaMgr.getAtopTemp();
            formulaMgr.setAtomTemp(null);
            formulaMgr.setEdgeTemp(null);

            // 다른 Atom 위에서 릴리즈된 경우
            for (noteFormula formula : formulaMgr.getFormulas()) {
                for (noteFormulaAtom atom : formula.getAtoms()) {
                    if (atom.getTouchArea().contains(releasePoint)) {
                        // 두 Atom을 Edge로 연결
                        noteFormulaEdgeSingle newEdge = new noteFormulaEdgeSingle(currAtom, atom);
                        currFormula.addEdge(newEdge);
                        formulaMgr.addPrevEdge(newEdge);  // 현재 엣지 리스트에 추가

                        // 두 Formula를 병합
                        noteCmdToMergeFormula.execute(note, currFormula, formula);

                        formulaMgr.setCurrFormula(null);
                        formulaMgr.setCurrAtom(null);
                        formulaMgr.clearPrevElements();  // 현재 작업 리스트 초기화

                        XCmdToChangeScene.execute(note,
                                noteFormulaScenario.FormulaReadyScene.getSingleton(),
                                this.mReturnScene);
                        return;
                    }
                }
            }

            // 빈 공간에서 릴리즈된 경우
            Point2D.Double snapPosition = tempAtom.getPosition();
            noteFormulaAtom newAtom = noteCmdToCreateAtom.execute(note, snapPosition);

            // 현재 Atom과 새 Atom을 Edge로 연결
            noteFormulaEdgeSingle newEdge = new noteFormulaEdgeSingle(currAtom, newAtom);
            currFormula.addEdge(newEdge);
            formulaMgr.addPrevEdge(newEdge);  // 현재 엣지 리스트에 추가

            formulaMgr.setCurrFormula(null);
            formulaMgr.setCurrAtom(null);
            formulaMgr.clearPrevElements();  // 현재 작업 리스트 초기화

            XCmdToChangeScene.execute(note,
                    noteFormulaScenario.FormulaReadyScene.getSingleton(),
                    this.mReturnScene);
        }

        @Override
        public void handleKeyDown(KeyEvent e) {

        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteFormulaMgr formulaMgr = note.getFormulaMgr();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_F:             // will be changed
                    formulaMgr.setAtomTemp(null);
                    formulaMgr.setEdgeTemp(null);
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
