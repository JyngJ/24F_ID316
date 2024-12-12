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
import note.noteFormula;
import note.noteFormulaMgr;
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

        private long pressStartTime;  // 프레스 시작 시간
        private Point2D.Double pressStartPoint;  // 프레스 시작 위치
        private static final double MAX_MOVE_DISTANCE = 5.0; // 롱프레스 중 허용되는 최대 이동 거리

        @Override
        public void handleMousePress(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point pt = e.getPoint();

            // 롱프레스 시작 정보 저장
            pressStartTime = System.currentTimeMillis();
            pressStartPoint = new Point2D.Double(pt.x, pt.y);

            // ColorChooser 영역 체크
            if (pt.x >= 22 && pt.x < 105 && pt.y <= 60 && pt.y > 40) {
            } else {
                // Formula 터치 체크
                boolean touchedFormula = false;
                for (noteFormula formula : note.getFormulaMgr().getFormulas()) {
                    if (formula.isTouchedBy(pressStartPoint)) {
                        touchedFormula = true;
                        break;
                    }
                }

                if (!touchedFormula) {
                    // Formula를 터치하지 않은 경우 기존 PtCurve 생성 로직 실행
                    noteCmdToCreatePtCurve.execute(note, pt);
                    XCmdToChangeScene.execute(note,
                            noteDrawScenario.DrawScene.getSingleton(), this);
                }
            }
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            Point2D.Double currentPoint = new Point2D.Double(e.getX(), e.getY());
            noteApp note = (noteApp) this.mScenario.getApp();

            // 드래그 거리가 허용 범위를 넘어가면 롱프레스 취소
            if (pressStartPoint != null
                    && pressStartPoint.distance(currentPoint) > MAX_MOVE_DISTANCE) {
                pressStartPoint = null;
            }

            // 롱프레스 체크
            if (pressStartPoint != null) {
                long pressDuration = System.currentTimeMillis() - pressStartTime;
                if (pressDuration >= noteFormulaMgr.LONG_PRESS_DURATION) {
                    // Formula 터치 체크
                    for (noteFormula formula : note.getFormulaMgr().getFormulas()) {
                        if (formula.isTouchedBy(pressStartPoint)) {
                            // Formula를 편집 모드로 설정하고 Edit 씬으로 전환
                            note.getFormulaMgr().setEditingFormula(formula);
                            XCmdToChangeScene.execute(note,
                                    noteFormulaEditScenario.noteFormulaEditReadyScene.getSingleton(), this);
                            pressStartPoint = null;  // 롱프레스 정보 초기화
                            return;
                        }
                    }
                }
            }
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            Point pt = e.getPoint();

            // 기존 마우스 릴리즈 처리
            Color c = note.getColorChooser().calcColor(pt,
                    note.getCanvas2D().getWidth(),
                    note.getCanvas2D().getHeight());
            if (c != null) {
                note.getCanvas2D().setCurrColorForPtCurve(c);
            }
            note.getPtCurveMgr().setCurrPtCurve(null);

            // 롱프레스 정보 초기화
            pressStartPoint = null;
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
