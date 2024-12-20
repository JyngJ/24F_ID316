package note;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class noteFormulaRenderer {

    private noteCanvas2D mCanvas2D = null;
    private noteApp mNote = null;
    public static final Color COLOR_FORMULA_DEFAULT = Color.BLACK;
    public static final Color COLOR_FORMULA_TEMP = new Color(192, 192, 192, 192);
    public static final double LENGTH_EDGE_DEFAULT = 100.0;
    public static final float EDGE_STROKE_WIDTH = 4.0f;
    public static int FONT_SIZE = 24;
    public static final Font FONT_ATOM = new Font("Arial", Font.BOLD, FONT_SIZE);

    // Constructor
    public noteFormulaRenderer(noteApp note, noteCanvas2D canvas) {
        this.mNote = note;
        this.mCanvas2D = canvas;
    }

    // 모든 Formulas를 렌더링
    public void renderFormulas(noteApp note, Graphics2D g2) {
        this.mNote = note;

        // 기존 Formula 렌더링
        for (noteFormula formula : note.getFormulaMgr().getFormulas()) {
            renderFormula(g2, formula);
        }

        // 현재 작업 중인 prevEdge와 prevAtom 렌더링
        noteFormulaMgr formulaMgr = note.getFormulaMgr();
        for (noteFormulaEdge edge : formulaMgr.getPrevEdges()) {
            renderEdges(g2, edge);
        }
        for (noteFormulaAtom atom : formulaMgr.getPrevAtoms()) {
            renderAtom(g2, atom);
        }

        // 임시 Edge 렌더링
        drawEdgeTemp(g2);
    }

    // Formula를 그리는 메서드
    public void renderFormula(Graphics2D g2, noteFormula formula) {
        boolean isEditing = (formula == this.mNote.getFormulaMgr().getEditingFormula());
        
        // Default에서 선택된 상태 표시
        
        // Atom의 글씨에 ORANGE 테두리 추가
        for (noteFormulaAtom atom : formula.getAtoms()) {
            if (formula.getSelectState() == noteFormula.SelectState.SELECTED && !atom.getType().equals("C")) {
                Point2D.Double position = atom.getPosition();
                g2.setColor(Color.ORANGE);
                FontMetrics fm = g2.getFontMetrics(FONT_ATOM);
                String atomType = atom.getType();
                int textWidth = fm.stringWidth(atomType);
                int textHeight = fm.getAscent();
                int radius = Math.max(textWidth, textHeight) / 2 + 4;
                g2.fillOval((int) (position.x - radius),
                            (int) (position.y - radius),
                            radius * 2, radius * 2);
            }
        }
        
        if (formula.getSelectState() == noteFormula.SelectState.SELECTED) {
            g2.setColor(Color.ORANGE);
            g2.setStroke(new BasicStroke(EDGE_STROKE_WIDTH + 4)); // 더 두껍게
            for (noteFormulaEdge edge : formula.getEdges()) {
                Point2D.Double start = edge.getStartAtom().getPosition();
                Point2D.Double end = edge.getEndAtom().getPosition();
                g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
            }
        }
        
        // 편집 중인 formula의 터치 영역을 먼저 그림
        if (isEditing) {
            g2.setColor(noteCanvas2D.COLOR_HIGHLIGHT);
            // 모든 Edge의 터치 영역 먼저 그리기
            for (noteFormulaEdge edge : formula.getEdges()) {
                g2.fill(edge.getTouchArea());
            }
            // 모든 Atom의 터치 영역 그리기
            for (noteFormulaAtom atom : formula.getAtoms()) {
                g2.fill(atom.getTouchArea());
            }
        }

        // 그 위에 Edge와 Atom 그리기
        for (noteFormulaEdge edge : formula.getEdges()) {
            renderEdges(g2, edge);
        }

        for (noteFormulaAtom atom : formula.getAtoms()) {
            renderAtom(g2, atom);
        }
    }

    // Atom 그리기
    private void renderAtom(Graphics2D g2, noteFormulaAtom atom) {
        g2.setColor(Color.BLACK); // Atom 기본 색상
        Point2D.Double position = atom.getPosition();

        if (atom.getType() == "C") {
            // Debug: Atom 위치에 점 그리기
//            g2.fillOval((int) (position.x - 3), (int) (position.y - 3), 6, 6);
        } else {
            g2.setFont(FONT_ATOM);
            g2.drawString(atom.getType(), (float) position.x - 10, (float) position.y + 10);
        }

        // 디버깅용 라벨 "A" 표시
//        g2.setColor(Color.RED);
//        g2.setFont(new Font("Arial", Font.PLAIN, 12));
//        g2.drawString("A", (float) position.x + 5, (float) position.y - 5);
    }

    // Edge 그리기
    private void renderEdges(Graphics2D g2, noteFormulaEdge edge) {
        noteFormulaAtom start = edge.getStartAtom();
        noteFormulaAtom end = edge.getEndAtom();

        // Edge 타입에 따른 렌더링 방식
        switch (edge.getClass().getSimpleName()) {
            case "noteFormulaEdgeSingle" ->
                drawEdgeSingle(g2, start, end);
            case "noteFormulaEdgeDouble" ->
                drawEdgeDouble(g2, start, end);
            case "noteFormulaEdgeTriple" ->
                drawEdgeTriple(g2, start, end);
        }

//        // 디버깅용 라벨 "E" 표시
//        g2.setColor(Color.RED);
//        g2.setFont(new Font("Arial", Font.PLAIN, 12));
//        double midX = (start.getPosition().x + end.getPosition().x) / 2;
//        double midY = (start.getPosition().y + end.getPosition().y) / 2;
//        g2.drawString("E", (float) midX + 5, (float) midY - 5);
    }

    // 단일 결합 Edge
    private void drawEdgeSingle(Graphics2D g2, noteFormulaAtom startAtom, noteFormulaAtom endAtom) {
        Point2D.Double start;
        Point2D.Double end;

        if (startAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            start = startAtom.getPosition();
        } else {
            // 아니라면 선 더 짧게 그리기 
            start = shortenPosition(startAtom, endAtom, 20);
        }

        if (endAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            end = endAtom.getPosition();
        } else {
            // 아니라면 선 더 짧게 그리기 
            end = shortenPosition(endAtom, startAtom, 20);
        }

        g2.setColor(COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(EDGE_STROKE_WIDTH)); // Edge 굵기
        g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }

    // 이중 결합 Edge
    private void drawEdgeDouble(Graphics2D g2, noteFormulaAtom startAtom, noteFormulaAtom endAtom) {
        Point2D.Double start;
        Point2D.Double end;

        if (startAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            start = shortenPosition(startAtom, endAtom, 10);
        } else {
            // 아니라면 선 더 짧게 그리기 
            start = shortenPosition(startAtom, endAtom, 20);
        }

        if (endAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            end = shortenPosition(endAtom, startAtom, 10);
        } else {
            // 아니라면 선 더 짧게 그리기 
            end = shortenPosition(endAtom, startAtom, 20);
        }

        g2.setColor(COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(EDGE_STROKE_WIDTH)); // Edge 굵기

        // 수직 벡터 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        double offsetX = -dy / length * 5; // 수직 벡터 x
        double offsetY = dx / length * 5;  // 수직 벡터 y

        // 두 선을 평행하게 그림
        g2.drawLine(
                (int) (start.x - offsetX), (int) (start.y - offsetY),
                (int) (end.x - offsetX), (int) (end.y - offsetY)
        );
        g2.drawLine(
                (int) (start.x + offsetX), (int) (start.y + offsetY),
                (int) (end.x + offsetX), (int) (end.y + offsetY)
        );
    }

    // 삼중 결합 Edge
    private void drawEdgeTriple(Graphics2D g2, noteFormulaAtom startAtom, noteFormulaAtom endAtom) {
        Point2D.Double start;
        Point2D.Double end;

        if (startAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            start = shortenPosition(startAtom, endAtom, 10);
        } else {
            // 아니라면 선 더 짧게 그리기 
            start = shortenPosition(startAtom, endAtom, 20);
        }

        if (endAtom.getType().equals("C")) {
            // 탄소면 끝까지 선 그리기 
            end = shortenPosition(endAtom, startAtom, 10);
        } else {
            // 아니라면 선 더 짧게 그리기 
            end = shortenPosition(endAtom, startAtom, 20);
        }

        g2.setColor(COLOR_FORMULA_DEFAULT); // Edge 색상
        g2.setStroke(new BasicStroke(EDGE_STROKE_WIDTH)); // Edge 굵기

        // 수직 벡터 계산
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        double offsetX = -dy / length * 5; // 수직 벡터 x
        double offsetY = dx / length * 5;  // 수직 벡터 y

        // 세 선을 평행하게 그림
        g2.drawLine(
                (int) (start.x - 2 * offsetX), (int) (start.y - 2 * offsetY),
                (int) (end.x - 2 * offsetX), (int) (end.y - 2 * offsetY)
        );
        g2.drawLine(
                (int) start.x, (int) start.y,
                (int) end.x, (int) end.y
        );
        g2.drawLine(
                (int) (start.x + 2 * offsetX), (int) (start.y + 2 * offsetY),
                (int) (end.x + 2 * offsetX), (int) (end.y + 2 * offsetY)
        );
    }

    // 주어진 Atom의 위치를 기준으로 방향으로 길이를 줄이는 메서드
    private Point2D.Double shortenPosition(noteFormulaAtom atom, noteFormulaAtom otherAtom, double length) {
        Point2D.Double position = atom.getPosition();
        Point2D.Double otherPosition = otherAtom.getPosition();

        // 방향 벡터 계산
        double dx = otherPosition.x - position.x;
        double dy = otherPosition.y - position.y;
        double currentLength = Math.sqrt(dx * dx + dy * dy);

        // 방향 벡터 정규화
        if (currentLength != 0) {
            dx /= currentLength;
            dy /= currentLength;
        }

        // 새로운 위치 계산
        return new Point2D.Double(position.x + dx * length, position.y + dy * length);
    }

    // Temp 결합 Edge
    private void drawEdgeTemp(Graphics2D g2) {
        noteFormulaEdge tempEdge = mNote.getFormulaMgr().getEdgeTemp();
        if (tempEdge != null) {
            Point2D.Double start = tempEdge.getStartAtom().getPosition();
            Point2D.Double end = tempEdge.getEndAtom().getPosition();

            // temp 엣지 그리기 (반투명)
            g2.setColor(COLOR_FORMULA_TEMP);
            g2.setStroke(new BasicStroke(EDGE_STROKE_WIDTH));
            g2.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);

//            // 터치 영역 시각화
//            g2.setColor(new Color(255, 0, 0, 50));
//            g2.setStroke(new BasicStroke(1));
//            g2.draw(tempEdge.getTouchArea());
        }
    }

    // 스냅 로직
    public Point2D.Double calculateSnapPoint(Point2D.Double start, Point2D.Double current) {
        // 시작점에서 현재점까지의 벡터
        double dx = current.x - start.x;
        double dy = current.y - start.y;
        double angle = Math.atan2(dy, dx); // 현재 각도 (라디안)

        ArrayList<noteFormulaEdge> currEdges = mNote.getFormulaMgr().getPrevEdges();
        if (!currEdges.isEmpty() && !mNote.getPenMarkMgr().isRecentMovementMinimal()) {
            // 마지막 엣지 가져오기
            noteFormulaEdge lastEdge = currEdges.get(currEdges.size() - 1);

            // 마지막 엣지의 방향 계산
            Point2D.Double lastStart = lastEdge.getStartAtom().getPosition();
            Point2D.Double lastEnd = lastEdge.getEndAtom().getPosition();
            double lastDx = lastEnd.x - lastStart.x;
            double lastDy = lastEnd.y - lastStart.y;
            double lastAngle = Math.atan2(lastDy, lastDx);

            // 마지막 엣지 방향에서 ±120도 방향만 허용
            double angle1 = lastAngle + (Math.PI / 3);
            angle1 = (angle1 % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI);
            double angle2 = lastAngle - (Math.PI / 3);
            angle2 = (angle2 % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI);

            // 각도 차이를 0 ~ π로 정규화
            double diff1 = Math.min(Math.abs(angle - angle1), 2 * Math.PI - Math.abs(angle - angle1));
            double diff2 = Math.min(Math.abs(angle - angle2), 2 * Math.PI - Math.abs(angle - angle2));

            // 가까운 각도를 선택
            angle = (diff1 < diff2) ? angle1 : angle2;
            
        } else if (!currEdges.isEmpty() && mNote.getPenMarkMgr().isRecentMovementMinimal()){
            // 마지막 엣지 가져오기
            noteFormulaEdge lastEdge = currEdges.get(currEdges.size() - 1);

            // 마지막 엣지의 방향 계산
            Point2D.Double lastStart = lastEdge.getStartAtom().getPosition();
            Point2D.Double lastEnd = lastEdge.getEndAtom().getPosition();
            double lastDx = lastEnd.x - lastStart.x;
            double lastDy = lastEnd.y - lastStart.y;
            double lastAngle = Math.atan2(lastDy, lastDx);

            // 허용된 각도 차이 (라디안 단위)
            double[] angleDifferences = {
                Math.PI / 3,   // ±120도
                Math.PI / 2,   // ±90도
                2 * Math.PI / 3, // ±60도
                2 * Math.PI / 5  // ±72도
            };

            // 허용되는 각도 계산
            ArrayList<Double> allowedAngles = new ArrayList<>();
            for (double diff : angleDifferences) {
                allowedAngles.add(lastAngle + diff); // + 방향
                allowedAngles.add(lastAngle - diff); // - 방향
            }

            // 현재 각도와 허용 각도의 차이 계산
            double minDifference = Double.MAX_VALUE;
            double closestAngle = lastAngle;

            for (double allowedAngle : allowedAngles) {
                // 각도 차이를 계산 (음수와 양수 구분)
                double diff = angle - allowedAngle;
                diff = (diff + Math.PI) % (2 * Math.PI) - Math.PI; // [-π, π]로 정규화

                // 가장 작은 차이를 가진 각도 선택
                if (Math.abs(diff) < minDifference) {
                    minDifference = Math.abs(diff);
                    closestAngle = allowedAngle;
                }
            }

            // 최종 선택된 각도로 스냅
            angle = closestAngle;


        } else {
            // 엣지가 없는 경우 60도 간격 스냅
            double[] allowedAngles = {
                -Math.PI,
                -2 * Math.PI / 3,
                -Math.PI / 3,
                0,
                Math.PI / 3,
                2 * Math.PI / 3
            };

            double closestAngle = allowedAngles[0];
            double minDifference = Double.MAX_VALUE;
            for (double allowedAngle : allowedAngles) {
                double diff = Math.abs(angle - allowedAngle);
                if (diff < minDifference) {
                    minDifference = diff;
                    closestAngle = allowedAngle;
                }
            }

            angle = closestAngle;
        }

        // 스냅된 방향 계산
        double length = Math.hypot(dx, dy);
        double snappedDx = length * Math.cos(angle);
        double snappedDy = length * Math.sin(angle);

        return new Point2D.Double(start.x + snappedDx, start.y + snappedDy);
    }
}
