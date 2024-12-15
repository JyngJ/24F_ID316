package note;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class noteFormulaAtom extends noteObject {

    // Atom 종류 (C, O, N 등)
    private String atomType;

    // Atom 위치 (Point2D)
    private Point2D.Double position;

    // Atom의 터치 영역 (Ellipse2D)
    private Ellipse2D.Double touchArea;
    
    public static final float ATOM_RADIUS = 15;
    public static final float ATOM_TOUCH_AREA_RADIUS = (float) (1.5 * ATOM_RADIUS);

    // 생성자
    public noteFormulaAtom(String type, Point2D.Double position) {
        this.atomType = type;
        this.position = position;
        float tr = ATOM_TOUCH_AREA_RADIUS;

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
        float tr = ATOM_TOUCH_AREA_RADIUS;
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

    public void translateTouchAreaTo(double dx, double dy) {
        if (this.touchArea != null) {
            // 현재 터치 영역의 위치와 크기 가져오기
            double x = touchArea.getX();
            double y = touchArea.getY();
            double width = touchArea.getWidth();
            double height = touchArea.getHeight();

            // 새로운 위치로 이동
            touchArea.setFrame(x + dx, y + dy, width, height);
        }
    }

    @Override
    public void translateTo(double dx, double dy) {
        // FormulaMgr에서 구현
    }

    @Override
    public void scaleTo(double sf, Point2D.Double topLeft) {
        // 크기 변화 구현 X 
    }

}
