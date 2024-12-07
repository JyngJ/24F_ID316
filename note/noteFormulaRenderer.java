package note;

import java.awt.*;
import java.awt.geom.Point2D;

public class noteFormulaRenderer {
    private noteCanvas2D mCanvas2D = null;
    private noteApp mNote = null;
    private static final Color COLOR_FORMULA_DEFAULT = new Color(0, 0, 0, 192);
    private static final Color COLOR_FORMULA_TEMP = new Color(128, 128, 128, 128);

    // Constructor
    public noteFormulaRenderer(noteCanvas2D canvas) {
        this.mCanvas2D = canvas;
    }
    
    // 모든 Formulas를 렌더링
    public void renderFormulas(noteApp note, Graphics2D g2) {
        this.mNote = note;
        for (noteFormula formula : note.getFormulaMgr().getFormulas()) {
            renderFormula(g2, formula);
        }
    }

    // Formula를 그리는 메서드
    public void renderFormula(Graphics2D g2, noteFormula formula) {
        // Atom 그리기
        for (noteFormulaAtom atom : formula.getAtoms()) {
            renderAtom(g2, atom);
        }

        // Edge 그리기
        for (noteFormulaEdge edges : formula.getEdges()) {
            renderEdges(g2, edges);
        }
    }

    // Atom 그리기
    private void renderAtom(Graphics2D g2, noteFormulaAtom atom) {
        g2.setColor(Color.BLACK); // Atom 기본 색상
//        g2.fill(atom.getTouchArea()); // Atom 영역을 채움

        // Atom 타입 텍스트 렌더링
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        Point2D.Double position = atom.getPosition();
//        g2.drawString(atom.getType(), (float) position.x - 4, (float) position.y + 4);
    }

    // Edge 그리기
    private void renderEdges(Graphics2D g2, noteFormulaEdge edge) {
        Point2D.Double start = edge.getStartAtom().getPosition();
        Point2D.Double end = edge.getEndAtom().getPosition();

        // Edge 타입에 따른 렌더링 방식
        switch (edge.getClass().getSimpleName()) {
            case "noteFormulaEdgeSingle" ->
                drawEdgeSingle(g2, start, end);
            case "noteFormulaEdgeDouble" ->
                drawEdgeDouble(g2, start, end);
            case "noteFormulaEdgeTriple" ->
                drawEdgeTriple(g2, start, end);
        }
        
        drawEdgeTemp(g2);
    }

    // 단일 결합 Edge
    private void drawEdgeSingle(Graphics2D g2, Point2D.Double start, Point2D.Double end) {
        g2.setColor(noteFormulaRenderer.COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(2)); // Edge 굵기
        g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }

    // 이중 결합 Edge
    private void drawEdgeDouble(Graphics2D g2, Point2D.Double start, Point2D.Double end) {
        g2.setColor(noteFormulaRenderer.COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(2)); // Edge 굵기

        // 수직 벡터 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        double offsetX = -dy / length * 10; // 수직 벡터 x
        double offsetY = dx / length * 10;  // 수직 벡터 y

        // 두 선을 평행하게 그림
        g2.drawLine(
            (int) (start.x - offsetX), (int) (start.y - offsetY),
            (int) (end.x - offsetX), (int) (end.y - offsetY)
        );
        g2.drawLine(
            (int) (start.x + offsetX), (int) (start.y + offsetY),
            (int) (end.x + offsetX), (int) (end.y + offsetY)
        );
    }

    // 삼중 결합 Edge
    private void drawEdgeTriple(Graphics2D g2, Point2D.Double start, Point2D.Double end) {
        g2.setColor(noteFormulaRenderer.COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(2)); // Edge 굵기

        // 수직 벡터 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        double offsetX = -dy / length * 10; // 수직 벡터 x
        double offsetY = dx / length * 10;  // 수직 벡터 y

        // 세 선을 평행하게 그림
        g2.drawLine(
            (int) (start.x - 2 * offsetX), (int) (start.y - 2 * offsetY),
            (int) (end.x - 2 * offsetX), (int) (end.y - 2 * offsetY)
        );
        g2.drawLine(
            (int) start.x, (int) start.y,
            (int) end.x, (int) end.y
        );
        g2.drawLine(
            (int) (start.x + 2 * offsetX), (int) (start.y + 2 * offsetY),
            (int) (end.x + 2 * offsetX), (int) (end.y + 2 * offsetY)
        );
    }
    
    // Temp 결합 Edge
    private void drawEdgeTemp(Graphics2D g2) {
        noteFormulaEdge tempEdge = mNote.getFormulaMgr().getEdgeTemp();
        if (tempEdge != null) {
            Point2D.Double start = tempEdge.getStartAtom().getPosition();
            Point2D.Double end = tempEdge.getEndAtom().getPosition();
            g2.setColor(noteFormulaRenderer.COLOR_FORMULA_TEMP); // Edge 색상
            g2.setStroke(new BasicStroke(2)); // Edge 굵기
            g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        }
    }


//    // Feedforward 방향 표시
//    public void renderSnapDirection(Graphics2D g2, Point2D.Double startPoint, Point2D.Double currentPoint) {
//        // 스냅된 지점 계산
//        Point2D.Double snapPoint = calculateSnapPoint(startPoint, currentPoint);
//
//        // 새로운 임시 Edge 생성
//        noteFormulaAtom tempStartAtom = new noteFormulaAtom("Temp", startPoint);
//        noteFormulaEdgeTemp tempEdge = new noteFormulaEdgeTemp(tempStartAtom, snapPoint);
//
//    }


    // 스냅 로직
    public Point2D.Double calculateSnapPoint(Point2D.Double start, Point2D.Double current) {
        // 시작점에서 현재점까지의 벡터
        double dx = current.x - start.x;
        double dy = current.y - start.y;
        double angle = Math.atan2(dy, dx); // 현재 각도 (라디안)

        // 허용 각도 목록 (라디안)
        double[] allowedAngles = {
            -Math.PI,
            -2 * Math.PI / 3,
            -Math.PI / 3,
            0, 
            Math.PI / 3,
            2 * Math.PI / 3}; 

        // 가장 가까운 허용 각도 찾기
        double closestAngle = allowedAngles[0];
        double minDifference = Double.MAX_VALUE;
        for (double allowedAngle : allowedAngles) {
            double diff = Math.abs(angle - allowedAngle);
            if (diff < minDifference) {
                minDifference = diff;
                closestAngle = allowedAngle;
            }
        }

        // 스냅된 방향 계산
        double length = Math.hypot(dx, dy); // 벡터 길이 유지
        double snappedDx = length * Math.cos(closestAngle);
        double snappedDy = length * Math.sin(closestAngle);

        return new Point2D.Double(start.x + snappedDx, start.y + snappedDy);
    }
}
