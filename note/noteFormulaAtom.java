package note;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class noteFormulaAtom {

    // Atom 종류 (C, O, N 등)
    private String atomType;

    // Atom 위치 (Point2D)
    private Point2D.Double position;

    // Atom의 터치 영역 (Ellipse2D)
    private Ellipse2D.Double touchArea;

    // 생성자
    public noteFormulaAtom(String type, Point2D.Double position) {
        this.atomType = type;
        this.position = position;
        float tr = noteCanvas2D.ATOM_TOUCH_AREA_RADIUS;

        // 터치 영역을 원형으로 정의
        this.touchArea = new Ellipse2D.Double(
                position.x - tr,
                position.y - tr,
                tr * 2,
                tr * 2
        );
    }

    // Getter
    public String getType() {
        return atomType;
    }

    public void setType(String type) {
        this.atomType = type;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public void setPosition(Point2D.Double position) {
        this.position = position;
        updateTouchArea();
    }

    public Ellipse2D.Double getTouchArea() {
        return touchArea;
    }

    // 터치 영역 갱신 메서드
    private void updateTouchArea() {
        float tr = noteCanvas2D.ATOM_TOUCH_AREA_RADIUS;
        this.touchArea.setFrame(
                position.x - tr,
                position.y - tr,
                tr * 2,
                tr * 2
        );
    }

    public boolean isTouchedBy(Point2D.Double pt) {
        return this.touchArea.contains(pt);
    }

}
