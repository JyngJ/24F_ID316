package note;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JPanel;

public class noteCanvas2D extends JPanel{
    private static final Color COLOR_PT_CURVE_DEFAULT = new Color(0, 0, 0, 192);

    private static final Stroke STROKE_PT_CURVE_DEFAULT = 
            new BasicStroke(5f, 
                    BasicStroke.CAP_ROUND, 
                    BasicStroke.JOIN_ROUND);
    
    
    private static final Font FONT_INFO = 
            new Font("Monospaced", Font.PLAIN, 24);
    private static final Color COLOR_INFO = Color.black;
    private static final float INFO_TOP_ALIGNMENT_X = 20;
    private static final float INFO_TOP_ALIGNMENT_Y = 30;
    
    private noteApp mNote = null;
    
    // Constructor for noteCanvas2D
    public noteCanvas2D(noteApp note){
        this.mNote = note;
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        this.drawInfo(g2);
    }
    
    // Display the current mode on the screen
    private void drawInfo(Graphics2D g2) {
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        String str = currScene.getClass().getSimpleName();
        g2.setColor(noteCanvas2D.COLOR_INFO);
        g2.setFont(noteCanvas2D.FONT_INFO);
        g2.drawString(str,noteCanvas2D.INFO_TOP_ALIGNMENT_X, 
                noteCanvas2D.INFO_TOP_ALIGNMENT_Y);
    }
}
