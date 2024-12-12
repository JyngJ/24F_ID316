package note.scenario;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import note.noteApp;
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
        this.addScene(noteFormulaEditScenario.noteFormulaEditReadyScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaMoveScene.createSingleton(this));
        this.addScene(noteFormulaEditScenario.noteFormulaAtomMoveScene.createSingleton(this));
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
                case KeyEvent.VK_ESCAPE:
                    // 편집 모드 해제
                    note.getFormulaMgr().setEditingFormula(null);

                    // 이전 씬으로 돌아가기
                    XCmdToChangeScene.execute(note, this.mReturnScene, null);
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

    // 선택된 formula를 이동하는 상태
    // formula의 엣지를 잡고 드래그했을 때 여기로 들어와야 함 
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

    // 선택된 formula의 한 Atom을 이동시키는 상태
    // formula의 Atom을 잡고 드래그했을 때 여기로 들어와야 함 
    public static class noteFormulaAtomMoveScene extends noteScene {

        // singleton pattern
        private static noteFormulaAtomMoveScene mSingleton = null;

        public static noteFormulaAtomMoveScene getSingleton() {
            assert (noteFormulaAtomMoveScene.mSingleton != null);
            return noteFormulaAtomMoveScene.mSingleton;
        }

        public static noteFormulaAtomMoveScene createSingleton(XScenario scenario) {
            assert (noteFormulaAtomMoveScene.mSingleton == null);
            noteFormulaAtomMoveScene.mSingleton = new noteFormulaAtomMoveScene(scenario);
            return noteFormulaAtomMoveScene.mSingleton;
        }

        private noteFormulaAtomMoveScene(XScenario scenario) {
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

    // 엣지 변환, 삭제가 일어날 씬 
    // 엣지도, 아톰도 아닌 다른 곳을 클릭했을때 이곳으로 들어옴
    public static class noteFormulaGestureScene extends noteScene {

        // singleton pattern
        private static noteFormulaGestureScene mSingleton = null;

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
