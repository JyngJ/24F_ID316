package note;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JPanel;

public class noteCanvas2D extends JPanel {

    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 192);

    private static final Stroke STROKE_PT_CURVE_DEFAULT
            = new BasicStroke(5f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND);

    private static final Font FONT_INFO
            = new Font("Monospaced", Font.PLAIN, 24);
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
}
