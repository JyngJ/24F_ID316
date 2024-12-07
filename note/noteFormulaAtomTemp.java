package note;

import java.awt.geom.Point2D;

public class noteFormulaAtomTemp extends noteFormulaAtom {
    
    // Atom 위치 (Point2D)
    private String atomType;
    private Point2D.Double position;
    
    // 생성자
    public noteFormulaAtomTemp(String type, Point2D.Double position) {
        super(type, position);
        this.atomType = type;
        this.position = position;
    }
    
    public Point2D.Double getPosition() {
        return position;
    }
    
    public void setPosition(Point2D.Double position) {
        this.position = position;
    }

}
