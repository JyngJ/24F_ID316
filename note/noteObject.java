package note;

import java.awt.geom.Point2D;

public abstract class noteObject {

    // 노트 위에 그릴 모든 오브젝트는 이동 메서드를 가져야 함
    public abstract void translateTo(double dx, double dy);
    
    public abstract void scaleTo(double sf, Point2D.Double topLeft);
}
