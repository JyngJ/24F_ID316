package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class notePenMarkMgr {

    private static final int MAX_MARKS = 5;  // 최대 기록 수
    public static final long SHORT_TAB_DURATION = 100; // 0.5초 (밀리초)
    public static final long LONG_PRESS_DURATION = 500; // 0.5초 (밀리초)
    
    // 허용 오차 상수 (직선 판단 기준)
    private static final double STRAIGHT_LINE_MAX_DEVIATION = 5;
    
    private ArrayList<notePenMark> mMarks;   // 최근 펜 마크들
    // 마크 목록 가져오기
    public ArrayList<notePenMark> getMarks() {
        return mMarks;
    }

    private notePenMark mCurrPenMark;          // 현재 진행 중인 펜 마크
    public notePenMark getCurrPenMark() {
        return mCurrPenMark;
    }

    public notePenMarkMgr() {
        this.mMarks = new ArrayList<>();
        this.mCurrPenMark = null;
    }

    // 새로운 펜 마크 시작
    public void startMark(Point2D.Double pt) {
        mCurrPenMark = new notePenMark();
        mCurrPenMark.addPoint(pt);
    }

    // 펜 마크에 점 추가
    public void addPoint(Point2D.Double pt) {
        if (mCurrPenMark != null) {
            mCurrPenMark.addPoint(pt);
        }
    }

    // 펜 마크 종료
    public void endMark() {
        if (mCurrPenMark != null) {
            mCurrPenMark.end();
            mMarks.add(mCurrPenMark);

            // 최대 개수 유지
            while (mMarks.size() > MAX_MARKS) {
                mMarks.remove(0);
            }
            mCurrPenMark = null;
        }
    }

    // 현재 드래그 중인지 판단
    public boolean isDragging() {
        if (mCurrPenMark == null) {
            return false;
        }
        double dist = mCurrPenMark.getTotalDistance();
        return dist > 3.0;  // 3.0 이상 움직였으면 드래그로 판단
    }

    // 현재 롱탭 중인지 판단
    public boolean isLongPress() {
        if (mCurrPenMark == null) {
            return false;
        }
        long pressDuration = System.currentTimeMillis() - mCurrPenMark.getStartTime();
        return pressDuration >= LONG_PRESS_DURATION
                && mCurrPenMark.getTotalDistance() <= 3.0;  // 이동 거리가 작고 오래 눌렀을 때
    }
    
    public boolean wasShortTab() {
        notePenMark lastPM = mMarks.getLast();
        if (lastPM.getDuration() < SHORT_TAB_DURATION) {
            return true;
        } else {
            return false;
        }
    }
    
    // 이전 펜 마크가 대략적인 직선인지 확인
    public boolean wasLastMarkStraight() {
        if (mMarks.isEmpty()) {
            return false; // 이전 마크가 없으면 직선 여부를 판단할 수 없음
        }

        notePenMark lastMark = mMarks.get(mMarks.size() - 1); // 가장 최근 펜 마크 가져오기
        ArrayList<Point2D.Double> points = lastMark.getPoints();

        if (points.size() < 2) {
            return false; // 점이 2개 미만이면 직선 판단 불가
        }

        // 첫 번째 점과 마지막 점 가져오기
        Point2D.Double start = points.get(0);
        Point2D.Double end = points.get(points.size() - 1);

        // 직선의 기울기 (dy/dx) 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;

        // 모든 점과 직선 간의 최대 거리 확인
        for (Point2D.Double point : points) {
            double distance = calculatePointToLineDistance(point, start, dx, dy);
            if (distance > STRAIGHT_LINE_MAX_DEVIATION) {
                return false; // 허용 오차를 초과하면 직선이 아님
            }
        }

        return true; // 모든 점이 허용 오차 이내라면 직선으로 판단
    }

    // 점과 직선 사이의 수직 거리 계산
    private double calculatePointToLineDistance(Point2D.Double point, Point2D.Double start, double dx, double dy) {
        // 직선의 방정식: (dy/dx)x - y + c = 0
        double numerator = Math.abs(dy * point.x - dx * point.y + (dx * start.y - dy * start.x));
        double denominator = Math.sqrt(dx * dx + dy * dy);
        return numerator / denominator;
    }

    // 현재 마크의 시작점과 현재점 사이의 거리 계산
    public Point2D.Double getDragDistance() {
        if (mCurrPenMark == null) {
            return null;
        }
        Point2D.Double start = mCurrPenMark.getStartPoint();
        Point2D.Double end = mCurrPenMark.getEndPoint();
        if (start == null || end == null) {
            return null;
        }
        return new Point2D.Double(end.x - start.x, end.y - start.y);
    }

    // 마지막 위치에서 현재 위치까지의 상대적 이동량 계산
    public Point2D.Double getRelativeDistance() {
        if (mCurrPenMark == null || mCurrPenMark.getPoints().size() < 2) {
            return null;
        }
        ArrayList<Point2D.Double> points = mCurrPenMark.getPoints();
        Point2D.Double prevPt = points.get(points.size() - 2);
        Point2D.Double currPt = points.get(points.size() - 1);

        return new Point2D.Double(
                currPt.x - prevPt.x,
                currPt.y - prevPt.y
        );
    }

}
