package note;

import java.util.ArrayList;
import java.util.List;

public class noteFormula {
    // Atom과 Edge 리스트
    private ArrayList<noteFormulaAtom> atoms;
    private ArrayList<noteFormulaEdge> edges;

    // 생성자
    public noteFormula() {
        this.atoms = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    // Atom 추가
    public void addAtom(noteFormulaAtom atom) {
        this.atoms.add(atom);
    }

    // Atom 삭제
    public void removeAtom(noteFormulaAtom atom) {
        this.atoms.remove(atom);
    }

    // Edge 추가
    public void addEdge(noteFormulaEdge edge) {
        this.edges.add(edge);
    }

    // Edge 삭제
    public void removeEdge(noteFormulaEdge edge) {
        this.edges.remove(edge);
    }

    // Atom 리스트 반환
    public List<noteFormulaAtom> getAtoms() {
        return this.atoms;
    }

    // Edge 리스트 반환
    public List<noteFormulaEdge> getEdges() {
        return this.edges;
    }
}
