package note;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Point2D;

public class noteBoundingBox extends noteObject {
    private double minX, minY, maxX, maxY;
    private static final Stroke DASHED_STROKE_B = new BasicStroke(
            2f,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND,
            10f,
            new float[]{3f, 5f},
            0f
    );
    private static final int GAP = 5;

    // BoundingBox를 그리는 메서드
    public void draw(Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        g2.setStroke(DASHED_STROKE_B);
        g2.drawRect((int) minX - GAP, (int) minY - GAP,
                (int) (maxX - minX + 2 * GAP), (int) (maxY - minY) + 2 * GAP);
    }

    // BoundingBox를 설정하는 메서드
    public void setBoundingBox(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public double getMinX() {
        return this.minX;
    }
    public double getMinY() {
        return this.minY;
    }
    public double getMaxX() {
        return this.maxX;
    }
    public double getMaxY() {
        return this.maxY;
    }
    
    public double getWidth() {
        return this.maxX - this.minX;
    }
    
    public double getHeight() {
        return this.maxY - this.minY;
    }

    @Override
    public void translateTo(double dx, double dy) {
        this.setBoundingBox(
        this.getMinX() + dx,
        this.getMinY() + dy,
        this.getMaxX() + dx,
        this.getMaxY() + dy);
    }

    @Override
    public void scaleTo(double sf, Point2D.Double topLeft) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}