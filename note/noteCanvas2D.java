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
import static note.notePtCurve.SelectState.DEFAULT;
import static note.notePtCurve.SelectState.ERASE_SELECTED;
import static note.notePtCurve.SelectState.SELECTED;

public class noteCanvas2D extends JPanel {

    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 192);
    
    private static final Stroke STROKE_PT_CURVE_DEFAULT = 
            new BasicStroke(2f, 
                    BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND);
    
    public static final Stroke DASHED_STROKE = new BasicStroke(
        2f,                      
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10f,
        new float[]{3f, 5f},
        0f);  
    
    private static final Font FONT_INFO = 
            new Font("Monospaced", Font.PLAIN, 24);
    private static final Color COLOR_INFO = Color.black;
    private static final float INFO_TOP_ALIGNMENT_X = 20;
    private static final float INFO_TOP_ALIGNMENT_Y = 30;

    public static final float EDGE_TOUCH_AREA_WIDTH = 30;
    public static final float ATOM_RADIUS = 15;
    public static final float ATOM_TOUCH_AREA_RADIUS = 2 * ATOM_RADIUS;

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
        
        // draw pen tip
        mNote.getColorChooser().drawCells(g2,this.getWidth(),this.getHeight());
        Ellipse2D.Double e = 
                new Ellipse2D.Double(125, 55 - 2.5f, 5f, 5f);
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

//    private void drawFormula(Graphics2D g2, noteFormula formula) {
//        // Edge
//        for (noteFormulaEdge edge : formula.getEdges()) {
//            this.drawEdge(g2, edge);
//        }
//        
//        // Atom
//        for (noteFormulaAtom atom : formula.getAtoms()) {
//            this.drawAtom(g2, atom);
//        }
//    }
//
//    private void drawAtom(Graphics2D g2, noteFormulaAtom atom) {
//        // Atom 위치
//        Point2D.Double position = atom.getPosition();
//
//        // Atom 터치 영역(원)
//        g2.setColor(Color.LIGHT_GRAY); // 터치 영역은 연한 회색
//        g2.fillOval(
//            (int) (position.x - ATOM_TOUCH_AREA_RADIUS / 2),
//            (int) (position.y - ATOM_TOUCH_AREA_RADIUS / 2),
//            (int) ATOM_TOUCH_AREA_RADIUS,
//            (int) ATOM_TOUCH_AREA_RADIUS
//        );
//
//        // Atom 중심 (실제 원)
//        g2.setColor(Color.BLACK); // Atom 기본 색상
//        g2.fillOval(
//            (int) (position.x - ATOM_RADIUS),
//            (int) (position.y - ATOM_RADIUS),
//            (int) (ATOM_RADIUS * 2),
//            (int) (ATOM_RADIUS * 2)
//        );
//
//        // Atom 타입 (텍스트)
//        g2.setFont(new Font("Monospaced", Font.PLAIN, 16));
//        g2.setColor(Color.WHITE); // 텍스트 색상
//        g2.drawString(
//            atom.getType(),
//            (int) (position.x - 5), // 텍스트 위치 약간 중앙으로 조정
//            (int) (position.y + 5)
//        );
//    }
//
//    private void drawEdge(Graphics2D g2, noteFormulaEdge edge) {
//        // Edge 시작점과 끝점
//        Point2D.Double start = edge.getStartAtom().getPosition();
//        Point2D.Double end = edge.getEndAtom().getPosition();
//
//        // Edge 라인 그리기
//        g2.setColor(Color.BLACK); // Edge 색상
//        g2.setStroke(STROKE_PT_CURVE_DEFAULT); // Edge 기본 Stroke
//        g2.drawLine(
//            (int) start.x,
//            (int) start.y,
//            (int) end.x,
//            (int) end.y
//        );
//    }

    // Draw the in-progress curve
    private void drawCurrPtCurve(Graphics2D g2) {
        notePtCurve ptCurve = this.mNote.getPtCurveMgr().getCurrPtCurve();
        if (ptCurve != null){
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
        if (pts.size() < 2){
            // Don't draw if it's just a point
            return;
        }
        Point2D.Double pt0 = pts.get(0);
        path.moveTo(pt0.x, pt0.y);
        for (int i = 1 ; i < pts.size() ; i++) {
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
                g2.setColor(new Color(255, 255, 50, 100)); // 노란색 헤일로(임시)
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
}

