package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class noteFormula extends noteObject {

    // Atom과 Edge 리스트
    private ArrayList<noteFormulaAtom> mAtoms = new ArrayList<>();
    private ArrayList<noteFormulaEdge> mEdges = new ArrayList<>();

    // 생성자
    public noteFormula() {
        this.mAtoms = new ArrayList<>();
        this.mEdges = new ArrayList<>();
    }

    // Atom 추가
    public void addAtom(noteFormulaAtom atom) {
        this.mAtoms.add(atom);
    }

    // Atom 삭제
    public void removeAtom(noteFormulaAtom atom) {
        this.mAtoms.remove(atom);
    }

    // Edge 추가
    public void addEdge(noteFormulaEdge edge) {
        this.mEdges.add(edge);
    }

    // Edge 삭제
    public void removeEdge(noteFormulaEdge edge) {
        this.mEdges.remove(edge);
    }

    // Atom 리스트 반환
    public ArrayList<noteFormulaAtom> getAtoms() {
        return this.mAtoms;
    }

    // Edge 리스트 반환
    public ArrayList<noteFormulaEdge> getEdges() {
        return this.mEdges;
    }

    @Override
    public void translateTo(double dx, double dy) {
        // 모든 atom의 위치와 터치영역 이동
        for (noteFormulaAtom atom : this.mAtoms) {
            Point2D.Double pos = atom.getPosition();
            atom.setPosition(new Point2D.Double(pos.x + dx, pos.y + dy));
            atom.translateTouchAreaTo(dx, dy);
        }

        // 모든 edge의 터치영역 이동
        for (noteFormulaEdge edge : this.mEdges) {
            edge.translateTo(dx, dy);
            edge.translateTouchAreaTo(dx, dy);
        }
    }

    public boolean isTouchedBy(Point2D.Double pt) {
        // Atom 확인
        for (noteFormulaAtom atom : mAtoms) {
            if (atom.isTouchedBy(pt)) {
                return true;
            }
        }

        // Edge 확인
        for (noteFormulaEdge edge : mEdges) {
            if (edge.isTouchedBy(pt)) {
                return true;
            }
        }

        return false;
    }
}
