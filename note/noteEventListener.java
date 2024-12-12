package note;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

public class noteEventListener implements MouseListener, MouseMotionListener,
        KeyListener {

    // fields
    private noteApp mNote = null;

    //constructor
    public noteEventListener(noteApp note) {
        this.mNote = note;
    }

    @Override
    public void mousePressed(MouseEvent e) {        
        // 펜 마크 시작
        Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
        mNote.getPenMarkMgr().startMark(pt);

        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        currScene.handleMousePress(e);
        this.mNote.getCanvas2D().repaint();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 펜 마크 업데이트
        Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
        mNote.getPenMarkMgr().addPoint(pt);

        // 씬의 이벤트 처리
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        currScene.handleMouseDrag(e);
        this.mNote.getCanvas2D().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // 펜 마크 업데이트
        mNote.getPenMarkMgr().endMark();

        // 씬의 이벤트 처리
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        currScene.handleMouseRelease(e);
        this.mNote.getCanvas2D().repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // need to be fixed
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        currScene.handleKeyDown(e);
        this.mNote.getCanvas2D().repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // need to be fixed
        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
        currScene.handleKeyUp(e);
        this.mNote.getCanvas2D().repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
