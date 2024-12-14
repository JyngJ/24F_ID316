package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class notePenMarkMgr {

    private static final int MAX_MARKS = 5;  // 최대 기록 수
    public static final long SHORT_TAB_DURATION = 100; // 0.5초 (밀리초)
    public static final long LONG_PRESS_DURATION = 500; // 0.5초 (밀리초)
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
        System.out.println("checking");
        System.out.println(lastPM);
        System.out.println(lastPM.getDuration());
        if (lastPM.getDuration() < SHORT_TAB_DURATION) {
            System.out.println("shorttab");
            return true;
        } else {
            return false;
        }
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
