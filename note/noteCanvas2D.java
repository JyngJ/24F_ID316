package note;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import note.noteBoundingBox;
import note.noteFormula;
import note.noteFormulaAtom;
import note.notePtCurve;
import note.notePtCurve.SelectState;
import static note.notePtCurve.SelectState.DEFAULT;
import static note.notePtCurve.SelectState.ERASE_SELECTED;
import static note.notePtCurve.SelectState.SELECTED;

public class noteCanvas2D extends JPanel {

    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 192);

    private static final Stroke STROKE_PT_CURVE_DEFAULT
            = new BasicStroke(2f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);

    public static final Stroke DASHED_STROKE = new BasicStroke(
            2f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND,
            10f,
            new float[]{3f, 5f},
            0f);

    private static final Font FONT_INFO
            = new Font("Monospaced", Font.PLAIN, 24);
    private static final Color COLOR_INFO = Color.black;
    private static final float INFO_TOP_ALIGNMENT_X = 20;
    private static final float INFO_TOP_ALIGNMENT_Y = 30;

    public static final float EDGE_TOUCH_AREA_WIDTH = 30;
    public static final float ATOM_RADIUS = 15;
    public static final float ATOM_TOUCH_AREA_RADIUS = 2 * ATOM_RADIUS;

    public static final Color COLOR_HIGHLIGHT = new Color(255, 180, 50); // 주황색 하이라이트 (불투명)

    private noteApp mNote = null;

    private Color mCurrColorForCurve = null;

    public void setCurrColorForPtCurve(Color c) {
        this.mCurrColorForCurve = c;
    }

    public Color getCurrColorForCurve() {
        return this.mCurrColorForCurve;
    }

    private Stroke mCurrStrokeForPtCurve = null;

    public Stroke getCurrStrokeForPtCurve() {
        return mCurrStrokeForPtCurve;
    }


    public noteBoundingBox boundingBox = new noteBoundingBox();
    private Color ScaleHandleColor = Color.ORANGE;

    private noteFormulaRenderer mRenderer;

    public noteFormulaRenderer getRenderer() {
        if (mRenderer == null) {
            mRenderer = new noteFormulaRenderer(this.mNote, this);
        }
        return mRenderer;
    }

    // Constructor for noteCanvas2D
    public noteCanvas2D(noteApp note) {
        this.mNote = note;
        this.mCurrStrokeForPtCurve = noteCanvas2D.STROKE_PT_CURVE_DEFAULT;
        this.mCurrColorForCurve = noteCanvas2D.COLOR_PT_CURVE_DEFAULT;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        this.drawInfo(g2);
        this.drawFormulas(g2);
        this.drawPtCurves(g2);
        this.drawCurrPtCurve(g2);
        this.drawPenMarks(g2);

        // draw pen tip
        mNote.getColorChooser().drawCells(g2, this.getWidth(), this.getHeight());
        Ellipse2D.Double e
                = new Ellipse2D.Double(125, 55 - 2.5f, 5f, 5f);
        g2.setColor(this.mCurrColorForCurve);
        g2.fill(e);

        noteScene currScene = (noteScene) mNote.getScenarioMgr().getCurrScene();
        currScene.renderWorldObjects(g2);
        currScene.renderScreenObjects(g2);
    }

    // Display the current mode on the screen
    private void drawInfo(Graphics2D g2) {
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        String str = currScene.getClass().getSimpleName();
        g2.setColor(noteCanvas2D.COLOR_INFO);
        g2.setFont(noteCanvas2D.FONT_INFO);
        g2.drawString(str, noteCanvas2D.INFO_TOP_ALIGNMENT_X,
                noteCanvas2D.INFO_TOP_ALIGNMENT_Y);
    }

    private void drawFormulas(Graphics2D g2) {
        noteFormulaRenderer renderer = new noteFormulaRenderer(this.mNote, this);
        renderer.renderFormulas(this.mNote, g2);
    }

    // Draw the in-progress curve
    private void drawCurrPtCurve(Graphics2D g2) {
        notePtCurve ptCurve = this.mNote.getPtCurveMgr().getCurrPtCurve();
        if (ptCurve != null) {
            this.drawPtCurve(g2,
                    ptCurve,
                    ptCurve.mColor,
                    STROKE_PT_CURVE_DEFAULT);
        }
    }

    private void drawPtCurve(Graphics2D g2,
            notePtCurve ptCurve, Color c, Stroke s) {
        Path2D.Double path = new Path2D.Double();
        ArrayList<Point2D.Double> pts = ptCurve.getPts();
        if (pts.size() < 2) {
            // Don't draw if it's just a point
            return;
        }
        Point2D.Double pt0 = pts.get(0);
        path.moveTo(pt0.x, pt0.y);
        for (int i = 1; i < pts.size(); i++) {
            Point2D.Double pt = pts.get(i);
            path.lineTo(pt.x, pt.y);
        }
        g2.setColor(c);
        g2.setStroke(s);
        g2.draw(path);
    }

    private void drawPtCurves(Graphics2D g2) {
        for (notePtCurve ptCurve : this.mNote.getPtCurveMgr().getPtCurves()) {
            Color color = ptCurve.getColor();
            Stroke stroke = ptCurve.getStroke();

            switch (ptCurve.getSelectState()) {
                case SELECTED: // Select mode에서 선택
                    g2.setColor(COLOR_HIGHLIGHT); // 새로운 상수 사용
                    g2.setStroke(new BasicStroke(6f));
                    this.drawPtCurveOutline(g2, ptCurve); // 테두리(외곽선)만 그리기
                    break;
                case ERASE_SELECTED:
                    color = new Color(color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            50); // 불투명도 낮게
                    break;
                case DEFAULT: // 일반 펜
                    break;
            }

            this.drawPtCurve(g2, ptCurve, color, stroke);
        }
    }

    // 외곽선 그리기 method
    private void drawPtCurveOutline(Graphics2D g2, notePtCurve ptCurve) {
        Path2D.Double path = new Path2D.Double();
        ArrayList<Point2D.Double> pts = ptCurve.getPts();
        if (pts.size() < 2) {
            // 곡선이 하나의 점으로 구성된 경우 안 그림
            return;
        }
        Point2D.Double pt0 = pts.get(0);
        path.moveTo(pt0.x, pt0.y);
        for (int i = 1; i < pts.size(); i++) {
            Point2D.Double pt = pts.get(i);
            path.lineTo(pt.x, pt.y);
        }
        g2.draw(path);
    }

    // For Drawing BoundingBox
    
    public void updateBoundingBox() {
        if (boundingBox == null) {
            boundingBox = new noteBoundingBox();
        }
        ArrayList<notePtCurve> selectedCurves = new ArrayList<>();
        ArrayList<noteFormula> selectedMolecules = new ArrayList<>();
        selectedCurves = this.mNote.getPtCurveMgr().getSelectedPtCurves();
        selectedMolecules = this.mNote.getFormulaMgr().getSelectedFormulas_d();
        
        if (!selectedCurves.isEmpty() || !selectedMolecules.isEmpty()) {
            // BoundingBox 계산
            double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
            double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

            for (notePtCurve curve : selectedCurves) {
                for (Point2D.Double pt : curve.getPts()) {
                    minX = Math.min(minX, pt.x);
                    maxX = Math.max(maxX, pt.x);
                    minY = Math.min(minY, pt.y);
                    maxY = Math.max(maxY, pt.y);
                }
            }
            for (noteFormula molecule : selectedMolecules) {
                for (noteFormulaAtom atom : molecule.getAtoms()) {
                    Point2D.Double pt = atom.getPosition();
                    minX = Math.min(minX, pt.x);
                    maxX = Math.max(maxX, pt.x);
                    minY = Math.min(minY, pt.y);
                    maxY = Math.max(maxY, pt.y);
                    }
                }
            // BoundingBox 설정
            boundingBox.setBoundingBox(minX, minY, maxX, maxY);
            this.mNote.getBoundingBox().setBoundingBox(minX, minY, maxX, maxY);
            System.out.println(minX + ", " + minY + ", " + maxX + ", " + maxY);
        } else {
            this.clearBoundingBox();
        }
    }
    
    public void drawBoundingBox(Graphics2D g2) {
        final int GAP = 5;
        boundingBox.draw(g2);
        double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();
        drawScaleHandles(g2, minX - GAP, minY - GAP, maxX + GAP, maxY + GAP);
    }

    // BoundingBox 지우기
    public void clearBoundingBox() {
        boundingBox.setBoundingBox( -5, -5, -5, -5); // 화면 밖에 그려지도록
        repaint();  // 화면 새로 고침
    }

    public void drawScaleHandles(Graphics2D g2, double minX, double minY, double maxX, double maxY) {
        final int HANDLE_SIZE = 6;
        g2.setColor(Color.ORANGE);
        // BoundingBox 네 꼭짓점에 동그라미
        g2.fill(new Ellipse2D.Double(minX - HANDLE_SIZE / 2,
            minY - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE));
        g2.fill(new Ellipse2D.Double(maxX - HANDLE_SIZE / 2,
            minY - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE));
        g2.fill(new Ellipse2D.Double(minX - HANDLE_SIZE / 2,
            maxY - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE));
        g2.setColor(ScaleHandleColor);
        g2.fill(new Ellipse2D.Double(maxX - HANDLE_SIZE / 2,
            maxY - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE));
    }

    private void drawPenMarks(Graphics2D g2) {
        for (notePenMark penMark : this.mNote.getPenMarkMgr().getMarks()) {
            if (!penMark.isShown()) {
                break; // isShown이 false인 경우 그리지 않음
            }

            // PenMark의 점들을 그리는 로직 추가
            ArrayList<Point2D.Double> points = penMark.getPoints();
            if (points.size() < 2) {
                continue; // 점이 2개 미만이면 그리지 않음
            }

            for (int i = 1; i < points.size(); i++) {
                Point2D.Double p1 = points.get(i - 1);
                Point2D.Double p2 = points.get(i);
                g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            }
        }
    }
}
