package note;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class notePtCurve {
    public enum SelectState {
        DEFAULT,          // 일반 펜
        SELECTED,      // SelectScene에서 선택된 상태
        ERASE_SELECTED // EraseScene에서 선택된 상태
    }

    private ArrayList<Point2D.Double> mPts = null;

    public ArrayList<Point2D.Double> getPts() {
        return this.mPts;
    }

    public Stroke mStroke = null;
    public Stroke getStroke() {
        return this.mStroke;
    }

    public Color mColor = null;
    public Color getColor() {
        return this.mColor;
    }

    public static final float MIN_DIST_BTWN_PTS = 1;

    // SelectState 추가
    private SelectState selectState = SelectState.DEFAULT;

    public SelectState getSelectState() {
        return this.selectState;
    }

    public void setSelectState(SelectState state) {
        this.selectState = state;
    }

    // Constructor for notePtCurve
    public notePtCurve(Point2D.Double pt, Color c, Stroke s) {
        this.mPts = new ArrayList<>();
        this.mPts.add(pt);
        this.mColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());

        BasicStroke bs = (BasicStroke) s;
        this.mStroke = new BasicStroke(bs.getLineWidth(), bs.getEndCap(), bs.getLineJoin());
    }

    public void addPt(Point2D.Double pt) {
        this.mPts.add(pt);
    }
}
