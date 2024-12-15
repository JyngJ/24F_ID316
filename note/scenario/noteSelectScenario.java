package note.scenario;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.cmd.noteCmdToSelectObject;
import note.noteApp;
import note.noteBoundingBox;
import note.noteCanvas2D;
import note.noteFormula;
import static note.noteFormula.SelectState.DEFAULT;
import static note.noteFormula.SelectState.SELECTED;
import note.notePtCurve;
import note.notePtCurve.SelectState;
import note.notePtCurveMgr;
import note.noteScene;
import x.XApp;
import x.XCmdToChangeScene;
import x.XScenario;

public class noteSelectScenario extends XScenario {
    // singleton
    private static noteSelectScenario mSingleton = null;
    public static noteSelectScenario getSingleton() {
        assert(noteSelectScenario.mSingleton != null);
        return noteSelectScenario.mSingleton;
    }
    public static noteSelectScenario createSingleton(XApp app) {
        assert(noteSelectScenario.mSingleton == null);
        noteSelectScenario.mSingleton = new noteSelectScenario(app);
        return noteSelectScenario.mSingleton;
    }
    private noteSelectScenario(XApp app) {
        super(app);
    }

    @Override
    protected void addScenes() {
        this.addScene(noteSelectScenario.SelectScene.createSingleton(this));
        this.addScene(noteSelectScenario.SelectedScene.createSingleton(this));
    }

    public static class SelectScene extends noteScene {
        
        private ArrayList<Point2D.Double> dragPath = new ArrayList<>();
        private static final Stroke SelectArea =
            new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        private noteBoundingBox boundingBox = new noteBoundingBox();
        
        // singleton pattern
        private static SelectScene mSingleton = null;
        public static SelectScene getSingleton() {
            assert(SelectScene.mSingleton != null);
            return SelectScene.mSingleton;
        }
        public static SelectScene createSingleton(XScenario scenario) {
            assert(SelectScene.mSingleton == null);
            SelectScene.mSingleton = new SelectScene(scenario);
            return SelectScene.mSingleton;
        }
        private SelectScene(XScenario scenario) {
            super(scenario);
        }
        
        @Override
        public void handleMousePress(MouseEvent e) {
            this.dragPath.clear();
            Point2D.Double startPt = new Point2D.Double(e.getX(), e.getY());
            this.dragPath.add(startPt);
        }

        @Override
        public void handleMouseDrag(MouseEvent e) {
            Point2D.Double pts = new Point2D.Double(e.getX(), e.getY());
            this.dragPath.add(pts);
        }

        @Override
        public void handleMouseRelease(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteCmdToSelectObject.execute((XApp) this.mScenario.getApp(), this.dragPath);
            this.dragPath.clear();
            note.getCanvas2D().updateBoundingBox();
            System.out.println(boundingBox.getMinX() + boundingBox.getMaxY());
        }

        @Override
        public void handleKeyDown(KeyEvent e) {
                      
        }

        @Override
        public void handleKeyUp(KeyEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            int code = e.getKeyCode();
            switch (code) {
                case KeyEvent.VK_SHIFT:
                    this.dragPath.clear();
                    if (note.getPtCurveMgr().getSelectedPtCurves().isEmpty() &&
                        note.getFormulaMgr().getSelectedFormulas_d().isEmpty()) {
                        XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(),null);
                    } else {
                        XCmdToChangeScene.execute(note,
                            noteSelectScenario.SelectedScene.getSingleton(), null);
                    }
                }
        }

        @Override
        public void updateSupportObjects() {
            noteApp note = (noteApp) this.mScenario.getApp();
            note.getCanvas2D().updateBoundingBox();
        }

        @Override
        public void renderWorldObjects(Graphics2D g2) {
        
        }

        @Override
        public void renderScreenObjects(Graphics2D g2) {
            // Draw the selection path
            noteApp note = (noteApp) this.mScenario.getApp();
            noteBoundingBox boundingBox = note.getBoundingBox();
            g2.setColor(Color.GRAY);
            g2.setStroke(note.getCanvas2D().DASHED_STROKE);
            Path2D.Double path = new Path2D.Double();
            if (!dragPath.isEmpty()) {
                Point2D.Double pt0 = dragPath.get(0);
                path.moveTo(pt0.x, pt0.y);
                for (Point2D.Double pt : dragPath) {
                    path.lineTo(pt.x, pt.y);
                }
                g2.draw(path);
            }
        }

        @Override
        public void getReady() {
        
        }

        @Override
        public void wrapUp() {
        
        }
    }
    
    
    // SelectedScene class updated to ensure boundingBox synchronization with noteApp

    public static class SelectedScene extends noteScene {
        private static SelectedScene mSingleton = null;

        public static SelectedScene getSingleton() {
            assert(SelectedScene.mSingleton != null);
            return SelectedScene.mSingleton;
        }

        public static SelectedScene createSingleton(XScenario scenario) {
            assert(SelectedScene.mSingleton == null);
            SelectedScene.mSingleton = new SelectedScene(scenario);
            return SelectedScene.mSingleton;
        }

        private SelectedScene(XScenario scenario) {
            super(scenario);
        }

        @Override
        public void handleMousePress(MouseEvent e) {
            noteApp note = (noteApp) this.mScenario.getApp();
            noteBoundingBox boundingBox = note.getBoundingBox();
            Point pt = e.getPoint();
            int HANDLE_RANGE = 10;
            double minX = boundingBox.getMinX();
            double minY = boundingBox.getMinY();
            double maxX = boundingBox.getMaxX();
            double maxY = boundingBox.getMaxY();

            note.getPenMarkMgr().startMark(new Point2D.Double(e.getX(), e.getY()));
            ArrayList<notePtCurve> selectedCurves =
                    note.getPtCurveMgr().getPtCurves();
            ArrayList<noteFormula> selectedMolecules =
                    note.getFormulaMgr().getSelectedFormulas_d();
            
            if (pt.x > maxX - HANDLE_RANGE && pt.x < maxX + HANDLE_RANGE &&
                pt.y > maxY - HANDLE_RANGE && pt.y < maxY +HANDLE_RANGE) {
                XCmdToChangeScene.execute(note,
                        noteObjectEditScenario.ScaleScene.getSingleton(), this);
            }
            if (pt.x > minX && pt.x < maxX && pt.y > minY && pt.y < maxY) {
                XCmdToChangeScene.execute(note,
                        noteObjectEditScenario.TranslateScene.getSingleton(), this);
            } else {
                if (note.getPenMarkMgr().wasShortTab()) {
                    for (notePtCurve ptCurve : selectedCurves) {
                        ptCurve.setSelectState(SelectState.DEFAULT);
                    }
                    note.getPtCurveMgr().resetSelectedCurves();
                    for (noteFormula molecule : selectedMolecules) {
                        molecule.setSelectState(DEFAULT);
                    note.getFormulaMgr().resetSelectedFormulas_d();
                    }
                    note.getCanvas2D().updateBoundingBox();
                    note.getCanvas2D().repaint();
                    XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(), null);
            }
            }
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
            notePtCurveMgr curveMgr = note.getPtCurveMgr();
            int code = e.getKeyCode();

            ArrayList<notePtCurve> selectedCurves =
                    note.getPtCurveMgr().getSelectedPtCurves();
            ArrayList<noteFormula> selectedMolecules =
                    note.getFormulaMgr().getSelectedFormulas_d();

            switch (code) {
                case KeyEvent.VK_ESCAPE:
                    for (notePtCurve ptCurve : selectedCurves) {
                        ptCurve.setSelectState(SelectState.DEFAULT);
                    }
                    note.getPtCurveMgr().resetSelectedCurves();
                    for (noteFormula molecule : selectedMolecules) {
                        molecule.setSelectState(DEFAULT);
                    note.getFormulaMgr().resetSelectedFormulas_d();
                    }
                    note.getCanvas2D().updateBoundingBox();
                    note.getCanvas2D().repaint();
                    XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(), null);
                    break;

                case KeyEvent.VK_E:
                    ArrayList<notePtCurve> curvesToRemove = new ArrayList<>();
                    for (notePtCurve ptCurve : selectedCurves) {
                        if (ptCurve.getSelectState() == SelectState.SELECTED) {
                            curvesToRemove.add(ptCurve);
                        }
                    }
                    for (notePtCurve ptCurve : curvesToRemove) {
                        curveMgr.removeCurve(ptCurve);
                    }
                    note.getPtCurveMgr().resetSelectedCurves();
                    
                    ArrayList<noteFormula> formulasToRemove = new ArrayList<>();
                    for (noteFormula formula : selectedMolecules) {
                        if (formula.getSelectState() == SELECTED) {
                            formulasToRemove.add(formula);
                        }
                    }
                    for (noteFormula formula : formulasToRemove) {
                        note.getFormulaMgr().removeFormula(formula);
                    }
                    note.getFormulaMgr().resetSelectedFormulas_d();
                    
                    note.getCanvas2D().updateBoundingBox();
                    note.getCanvas2D().repaint();
                    XCmdToChangeScene.execute(note,
                            noteDefaultScenario.ReadyScene.getSingleton(), null);
                    break;

                    case KeyEvent.VK_SHIFT:
                        XCmdToChangeScene.execute(note,
                            noteSelectScenario.SelectScene.getSingleton(), this);

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
            noteApp note = (noteApp) this.mScenario.getApp();
            note.getCanvas2D().drawBoundingBox(g2);
        }
        
        @Override
        public void getReady() {
            noteApp note = (noteApp) this.mScenario.getApp();
            note.getCanvas2D().updateBoundingBox();   
        }

        @Override
        public void wrapUp() {
        }
    }
}
