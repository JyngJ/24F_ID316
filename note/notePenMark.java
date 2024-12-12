package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class notePenMark {

    private ArrayList<Point2D.Double> mPoints;  // 펜의 모든 점들
    private long mStartTime;  // 시작 시간
    private long mEndTime;    // 종료 시간

    public notePenMark() {
        this.mPoints = new ArrayList<>();
        this.mStartTime = System.currentTimeMillis();
        this.mEndTime = 0;
    }

    public void addPoint(Point2D.Double pt) {
        mPoints.add(pt);
    }

    public void end() {
        mEndTime = System.currentTimeMillis();
    }

    public Point2D.Double getStartPoint() {
        return mPoints.isEmpty() ? null : mPoints.get(0);
    }

    public Point2D.Double getEndPoint() {
        return mPoints.isEmpty() ? null : mPoints.get(mPoints.size() - 1);
    }

    public long getDuration() {
        return mEndTime - mStartTime;
    }

    public double getTotalDistance() {
        if (mPoints.size() < 2) {
            return 0.0;
        }

        double totalDist = 0.0;
        for (int i = 1; i < mPoints.size(); i++) {
            Point2D.Double p1 = mPoints.get(i - 1);
            Point2D.Double p2 = mPoints.get(i);
            totalDist += p1.distance(p2);
        }
        return totalDist;
    }

    public ArrayList<Point2D.Double> getPoints() {
        return mPoints;
    }

    public long getStartTime() {
        return mStartTime;
    }
}
