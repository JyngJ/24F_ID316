package note;

import java.util.ArrayList;

public class noteFormula {

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
}
