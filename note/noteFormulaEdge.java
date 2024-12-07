package note;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class noteFormulaEdge {
    // 시작 Atom
    private noteFormulaAtom startAtom = null;

    // 끝 Atom
    private noteFormulaAtom endAtom = null;

    // 방향 벡터
    private Point2D.Double direction = null;

    // Edge의 터치 영역 (Rectangle2D)
    private Rectangle2D.Double touchArea = null;

    // 생성자
    public noteFormulaEdge(noteFormulaAtom startAtom, 
        noteFormulaAtom endAtom) {
        this.startAtom = startAtom;
        this.endAtom = endAtom;

        // 방향 벡터 계산
        this.direction 
            = calculateDirection(startAtom.getPosition(), 
                endAtom.getPosition());

        // 터치 영역 계산
        this.touchArea 
            = calculateTouchArea(startAtom.getPosition(), 
                endAtom.getPosition());
    }
    
    // 방향 벡터 계산 메서드
    private Point2D.Double calculateDirection(Point2D.Double start, Point2D.Double end) {
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        return new Point2D.Double(dx, dy);
    }

    // 터치 영역 계산 메서드
    private Rectangle2D.Double calculateTouchArea(Point2D.Double start, Point2D.Double end) {
        double centerX = (start.x + end.x) / 2;
        double centerY = (start.y + end.y) / 2;
        double length = start.distance(end);

        return new Rectangle2D.Double(
            centerX - length / 2,
            centerY - noteCanvas2D.EDGE_TOUCH_AREA_WIDTH / 2,
            length,
            noteCanvas2D.EDGE_TOUCH_AREA_WIDTH
        );
    }
    
    public noteFormulaAtom getStartAtom() {
        return startAtom;
    }

    public void setStartAtom(noteFormulaAtom startAtom) {
        this.startAtom = startAtom;
    }

    public noteFormulaAtom getEndAtom() {
        return endAtom;
    }

    public void setEndAtom(noteFormulaAtom endAtom) {
        this.endAtom = endAtom;
    }

    public Point2D.Double getDirection() {
        return direction;
    }

    public Rectangle2D.Double getTouchArea() {
        return touchArea;
    }
}
