package note;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

// pan/rotate 기능 넣을 수도 있을 것 같아서 JSI 형식을 들고오긴 했는데
// 일단은 쓰는 기능 빼고는 지워뒀어요

public class noteXform {
    // Fields
    private AffineTransform mCurrXformFromWorldToScreen = null;
    public AffineTransform getCurrXformFromWorldToScreen() {
        return this.mCurrXformFromWorldToScreen;
    }
    private AffineTransform mCurrXformFromScreenToWorld = null;
    public AffineTransform getCurrXformFromScreenToWorld() {
        return this.mCurrXformFromScreenToWorld;
    }
    
    private AffineTransform mStartXformFromWorldToScreen = null;
    
    // Constructor for JSIXform
    public noteXform() {
        // Initialize all transformation matrices to identity,
        this.mCurrXformFromWorldToScreen = new AffineTransform();
        this.mCurrXformFromScreenToWorld = new AffineTransform();
        this.mStartXformFromWorldToScreen = new AffineTransform();
    }
    
    // Update the inverse transformation (mCurrXformFromScreenToWorld)
    // Call this whenever mCurrXformFromWorldToScreen is modified 
    public void updateCurrXformFromScreenToWorld() {
        try {
            this.mCurrXformFromScreenToWorld = 
                    this.mCurrXformFromWorldToScreen.createInverse();
        } catch (NoninvertibleTransformException ex) {
            System.out.print("Error!");
        }
    }
    
    // Convert a point from world coordinates to screen coordinates
    public Point calcPtFromWorldToScreen(Point2D.Double worldPt) {
        Point screenPt = new Point();
        this.mCurrXformFromWorldToScreen.transform(worldPt, screenPt);
        return screenPt;
    }
    
    // Convert a point from screen coordinates to world coordinates
    public Point2D.Double calcPtFromScreenToWorld(Point screenPt) {
        Point2D.Double worldPt = new Point2D.Double();
        this.mCurrXformFromScreenToWorld.transform(screenPt, worldPt);
        return worldPt;
    }
    
    // Reset the transformation to the identity matrix
    public void home () {
        this.mCurrXformFromWorldToScreen.setToIdentity(); 
        this.updateCurrXformFromScreenToWorld();
    }
}
