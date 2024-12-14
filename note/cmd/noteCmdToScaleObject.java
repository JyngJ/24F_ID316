package note.cmd;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import note.noteApp;
import note.notePtCurve;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToScaleObject extends XLoggableCmd {
    
    // fields 
    private Double dx = null;
    private Double dy = null;
    private Double scaleFactor = null;
    private Point2D.Double topLeft = null;
    
    // private constructor
    private noteCmdToScaleObject(XApp app, Double dx, Double dy) {
        super(app);
        this.dx = dx;
        this.dy = dy;
    }
    
    public static boolean execute(XApp app, Double dx, Double dy) {
        noteCmdToScaleObject cmd = new noteCmdToScaleObject(app, dx, dy);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        
        // Update BoundingBox
        note.getCanvas2D().updateBoundingBox();
        double width = note.getBoundingBox().getWidth();
        double height = note.getBoundingBox().getHeight();
        double minX = note.getBoundingBox().getMinX();
        double minY = note.getBoundingBox().getMinY();

        // Calculate the immediate scale factor
        if (this.dx > 0) {
            if (this.dy > 0) {
                // Both dx and dy are positive, use the smaller relative change
                this.scaleFactor = 1 + Math.min(this.dx / (width + this.dx), this.dy / (height + this.dy));
            } else {
                // Only dx is positive, scale based on dx
                this.scaleFactor = 1 + (this.dx / (width + this.dx));
            }
        } else {
            if (this.dy > 0) {
                // Only dy is positive, scale based on dy
                this.scaleFactor = 1 + (this.dy / (height + this.dx));
            } else {
                // Both dx and dy are negative, use the smaller relative change
                this.scaleFactor = 1 + Math.min(this.dx / (width + this.dx), this.dy / (height+ this.dy));
            }
        }

        // Define topLeft as the scaling origin
        this.topLeft = new Point2D.Double(minX, minY);

        // Scale each selected PtCurve
        ArrayList<notePtCurve> selectedCurves = note.getPtCurveMgr().getSelectedPtCurves();
        for (notePtCurve curve : selectedCurves) {
            curve.scaleTo(this.scaleFactor, this.topLeft);
        }

        // Update bounding box
        note.getCanvas2D().updateBoundingBox();

        // Repaint canvas
        note.getCanvas2D().repaint();

        // Debug output
        System.out.println("ScaleFactor: " + this.scaleFactor + ", TopLeft: " + this.topLeft);

        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
