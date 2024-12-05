package note;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class noteEventListener implements MouseListener, MouseMotionListener, 
        KeyListener{

    // fields
    private noteApp mNote = null;
    
    //constructor
    public noteEventListener(noteApp note) {
        this.mNote = note;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // need to be fixed
//        if(this.mNote.getPenMarkMgr().handleMousePress(e)){
//            noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
//            currScene.handleMousePress(e);
//            this.mNote.getCanvas2D().repaint();
//        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        // need to be fixed
//        if(this.mNote.getPenMarkMgr().handleMouseDrag(e)){
//            noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
//            currScene.handleMouseDrag(e);
//            this.mNote.getCanvas2D().repaint();
//        }
    }
     

    @Override
    public void mouseReleased(MouseEvent e) {
        // need to be fixed
//        if(this.mNote.getPenMarkMgr().handleMouseRelease(e)){
//            noteScene currScene = 
//                (noteScene) this.mNote.getScenarioMgr().getCurrScene();
//            currScene.handleMouseRelease(e);
//            this.mNote.getCanvas2D().repaint();
//        }
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
//        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
//        currScene.handleKeyDown(e);
//        this.mNote.getCanvas2D().repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // need to be fixed
//        noteScene currScene = (noteScene) this.mNote.getScenarioMgr().getCurrScene();
//        currScene.handleKeyUp(e);
//        this.mNote.getCanvas2D().repaint();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    
}
