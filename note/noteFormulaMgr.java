package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class noteFormulaMgr {

    // constants
    public static final double ANGLE_SNAP = 120.0; // 스냅 각도 (120도)
    public static final long LONG_PRESS_DURATION = 500; // 0.5초 (밀리초)

    // fields
    private noteFormula mCurrFormula = null;

    // 현재 작업 중인 엣지와 아톰들을 추적하기 위한 리스트
    private ArrayList<noteFormulaEdge> mPrevEdges = null;
    private ArrayList<noteFormulaAtom> mPrevAtoms = null;

    public ArrayList<noteFormulaEdge> getPrevEdges() {
        return this.mPrevEdges;
    }

    public ArrayList<noteFormulaAtom> getPrevAtoms() {
        return this.mPrevAtoms;
    }

    public noteFormula getCurrFormula() {
        return this.mCurrFormula;
    }

    public void setCurrFormula(noteFormula formula) {
        this.mCurrFormula = formula;
    }

    private noteFormulaAtomTemp mTempAtom = null;

    public noteFormulaAtomTemp getAtopTemp() {
        return this.mTempAtom;
    }

    public void setAtomTemp(noteFormulaAtomTemp tempAtom) {
        this.mTempAtom = tempAtom;
    }

    private noteFormulaEdgeTemp mTempEdge = null;

    public noteFormulaEdgeTemp getEdgeTemp() {
        return this.mTempEdge;
    }

    public void setEdgeTemp(noteFormulaEdgeTemp tempEdge) {
        this.mTempEdge = tempEdge;
    }

    // 현재 엣지가 시작되고 있는 atom
    private noteFormulaAtom mCurrAtom = null;

    public noteFormulaAtom getCurrAtom() {
        return this.mCurrAtom;
    }

    public void setCurrAtom(noteFormulaAtom atom) {
        this.mCurrAtom = atom;
    }

    private ArrayList<noteFormula> mFormulas = null;

    public ArrayList<noteFormula> getFormulas() {
        return this.mFormulas;
    }

    private noteFormula mSelectedFormula = null;

    public noteFormula getSelectedFormula() {
        return this.mSelectedFormula;
    }

    private noteFormula mEditingFormula = null;

    public noteFormula getEditingFormula() {
        return this.mEditingFormula;
    }

    public void setEditingFormula(noteFormula formula) {
        this.mEditingFormula = formula;
    }

    // constructor
    public noteFormulaMgr() {
        this.mFormulas = new ArrayList<>();
        this.mPrevEdges = new ArrayList<>();
        this.mPrevAtoms = new ArrayList<>();
    }

    // 현재 작업 중인 엣지/아톰 추가
    public void addPrevEdge(noteFormulaEdge edge) {
        this.mPrevEdges.add(edge);
    }

    public void addPrevAtom(noteFormulaAtom atom) {
        this.mPrevAtoms.add(atom);
    }

    // 현재 작업 리스트 초기화
    public void clearPrevElements() {
        this.mPrevEdges.clear();
        this.mPrevAtoms.clear();
    }

    // 메서드 
    // 특정 Atom이 속한 Formula를 찾는 메서드
    public noteFormula findFormulaForAtom(noteFormulaAtom atom) {
        for (noteFormula formula : mFormulas) {
            if (formula.getAtoms().contains(atom)) {
                return formula; // Atom이 포함된 Formula 반환
            }
        }
        return null; // Atom이 어떤 Formula에도 속하지 않을 경우 null 반환
    }

    public void arrangeFormulas() {
        for (noteFormula formula : this.mFormulas) {
            ArrayList<noteFormulaEdge> edges = (ArrayList<noteFormulaEdge>) formula.getEdges();
            ArrayList<noteFormulaAtom> atoms = (ArrayList<noteFormulaAtom>) formula.getAtoms();

            // 1. 따로 있는 atom 제거 (edge가 없는 atom)
            edges.removeIf(edge -> {
                return edge.getStartAtom() == null || edge.getEndAtom() == null;
            });

            // 2. 중복 edge 제거 (같은 atom 쌍을 연결하는 edge)
            for (int i = edges.size() - 1; i >= 0; i--) {
                noteFormulaEdge edge1 = edges.get(i);
                for (int j = i - 1; j >= 0; j--) {
                    noteFormulaEdge edge2 = edges.get(j);
                    if (hasSameAtoms(edge1, edge2)) {
                        edges.remove(i);
                        break;
                    }
                }
            }

            // 3. 양쪽 atom이 같은 위치에 있는 edge 제거
            edges.removeIf(edge -> {
                Point2D.Double pos1 = edge.getStartAtom().getPosition();
                Point2D.Double pos2 = edge.getEndAtom().getPosition();
                return pos1.distance(pos2) < 1.0;  // 1픽셀 이내면 같은 위치로 간주
            });

            // 4. 같은 위치의 atom 병합
            for (int i = atoms.size() - 1; i >= 0; i--) {
                noteFormulaAtom atom1 = atoms.get(i);
                for (int j = i - 1; j >= 0; j--) {
                    noteFormulaAtom atom2 = atoms.get(j);
                    if (isSamePosition(atom1, atom2)) {
                        // atom2에 연결된 모든 edge를 atom1으로 연결
                        redirectEdges(edges, atom2, atom1);
                        atoms.remove(j);
                        i--; // 인덱스 조정
                    }
                }
            }
        }
    }

    private boolean hasSameAtoms(noteFormulaEdge edge1, noteFormulaEdge edge2) {
        return (edge1.getStartAtom() == edge2.getStartAtom()
                && edge1.getEndAtom() == edge2.getEndAtom())
                || (edge1.getStartAtom() == edge2.getEndAtom()
                && edge1.getEndAtom() == edge2.getStartAtom());
    }

    private boolean isSamePosition(noteFormulaAtom atom1, noteFormulaAtom atom2) {
        Point2D.Double pos1 = atom1.getPosition();
        Point2D.Double pos2 = atom2.getPosition();
        return pos1.distance(pos2) < 1.0; // 1픽셀 이내면 같은 위치로 간주
    }

    private void redirectEdges(ArrayList<noteFormulaEdge> edges,
            noteFormulaAtom oldAtom, noteFormulaAtom newAtom) {
        for (noteFormulaEdge edge : edges) {
            if (edge.getStartAtom() == oldAtom) {
                edge.setStartAtom(newAtom);
            }
            if (edge.getEndAtom() == oldAtom) {
                edge.setEndAtom(newAtom);
            }
        }
    }

    // Atom을 완전히 제거하는 메서드
    public void removeAtom(noteFormulaAtom atom) {
        // prevAtoms에서 제거
        this.mPrevAtoms.remove(atom);

        // 현재 Formula에서도 제거
        if (this.mCurrFormula != null) {
            this.mCurrFormula.getAtoms().remove(atom);
        }

        // 모든 Formula에서 해당 atom 찾아서 제거
        for (noteFormula formula : this.mFormulas) {
            formula.getAtoms().remove(atom);
        }
    }

    public void translateSelectedFormula(double dx, double dy) {
        if (this.mSelectedFormula != null) {
            this.mSelectedFormula.translateTo(dx, dy);
        }
    }

}
