package note;

import java.awt.Polygon;
import java.awt.geom.Point2D;

public abstract class noteFormulaEdge {

    // 시작 Atom
    private noteFormulaAtom startAtom = null;

    // 끝 Atom
    private noteFormulaAtom endAtom = null;

    // 방향 벡터
    private Point2D.Double direction = null;

    // Edge의 터치 영역 (Polygon)
    private Polygon touchArea = null;

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
    private Polygon calculateTouchArea(Point2D.Double start, Point2D.Double end) {
        // 엣지의 방향 벡터 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);

        // 방향에 수직인 벡터 계산
        double perpX = -dy / length * (noteCanvas2D.EDGE_TOUCH_AREA_WIDTH / 2);
        double perpY = dx / length * (noteCanvas2D.EDGE_TOUCH_AREA_WIDTH / 2);

        // 사각형의 네 꼭지점 계산
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = (int) (start.x + perpX);
        yPoints[0] = (int) (start.y + perpY);
        xPoints[1] = (int) (start.x - perpX);
        yPoints[1] = (int) (start.y - perpY);
        xPoints[2] = (int) (end.x - perpX);
        yPoints[2] = (int) (end.y - perpY);
        xPoints[3] = (int) (end.x + perpX);
        yPoints[3] = (int) (end.y + perpY);

        return new Polygon(xPoints, yPoints, 4);
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

    public boolean containsPoint(Point2D.Double point) {
        Point2D.Double start = startAtom.getPosition();
        Point2D.Double end = endAtom.getPosition();

        // 선분과 점 사이의 거리 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);

        if (length == 0) {
            return false;
        }

        // 점과 선분 사이의 수직 거리 계산
        double distance = Math.abs((point.x - start.x) * dy - (point.y - start.y) * dx) / length;

        // 점이 선분의 범위 내에 있는지 확인
        double dotProduct = ((point.x - start.x) * dx + (point.y - start.y) * dy) / length;

        // 엣지 두께의 절반 정도를 터치 허용 범위로 사용
        return distance <= noteFormulaRenderer.EDGE_STROKE_WIDTH / 2
                && dotProduct >= 0 && dotProduct <= length;
    }

    // getTouchArea는 시각화 용도로만 사용
    public Polygon getTouchArea() {
        Point2D.Double start = startAtom.getPosition();
        Point2D.Double end = endAtom.getPosition();
        return calculateTouchArea(start, end);
    }

    public boolean isTouchedBy(Point2D.Double pt) {
        // 터치 영역(Polygon)으로 검사
        return this.touchArea.contains(pt.x, pt.y);
    }
}
