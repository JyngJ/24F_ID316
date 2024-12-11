package note;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

public class noteColorChooser {
    
    private Color[] mColors = null;
    public float mSaturation = Float.NaN;
    public float mOpaqueness = Float.NaN;
    
    public noteColorChooser() {
        this.mColors = new Color[4];
        this.mColors[0] = Color.BLACK;
        this.mColors[1] = Color.RED;
        this.mColors[2] = Color.BLUE;
        this.mColors[3] = new Color(170, 0, 200); // 보라색
    }

    public void drawCells(Graphics2D g2, int w, int h) {
        double dx = 20;
        double dy = 20;
        
        for (int i = 0 ; i < 4 ; i++) {
            double y = 20;
            double x = 650 + dx * i;
            Rectangle2D rect = new Rectangle2D.Double(x, y, dx, dy);
            g2.setColor(mColors[i]);
            g2.fill(rect);
            }
        }
    
    public Color calcColor(Point pt, int w, int h) {
        double dx = 20;
        int i = (int)((double)(pt.x - 650) / dx);
        if (i >= 0 && i < 4) {
            return this.mColors[i];
        } else {
            return null;
        }
    }
}

    

