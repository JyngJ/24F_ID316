package note;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class noteFormulaMgr {

    // constants
    public static final double ANGLE_SNAP = 120.0; // 스냅 각도 (120도)
    public static final long LONG_PRESS_DURATION = 500; // 0.5초 (밀리초)

    // fields
    
    private ArrayList<noteFormulaEdge> mPrevEdges = null;

    private ArrayList<noteFormulaAtom> mPrevAtoms = null;
    
    // for Select
    private ArrayList<noteFormula> mSelectedFormulas_d = new ArrayList<>();

    public ArrayList<noteFormula> getSelectedFormulas_d() {
        return this.mSelectedFormulas_d;
    }
    
    public void resetSelectedFormulas_d() {
        this.mSelectedFormulas_d = new ArrayList<>();
    }

    public ArrayList<noteFormulaEdge> getPrevEdges() {
        return this.mPrevEdges;
    }
    public void setPrevEdges(ArrayList<noteFormulaEdge> pv) {
        this.mPrevEdges = pv;
    }

    private ArrayList<noteFormulaAtom> mPrevAtoms = null;
    public ArrayList<noteFormulaAtom> getPrevAtoms() {
        return this.mPrevAtoms;
    }
    public void setPrevAtoms(ArrayList<noteFormulaAtom> at) {
        this.mPrevAtoms = at;
    }

    private noteFormula mCurrFormula = null;
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

    // 원자 수정시 대상이 되는 원자 
    private noteFormulaAtom mEditingAtom = null;
    public noteFormulaAtom getEditingAtom() {
        return this.mEditingAtom;
    }
    public void setEditingAtom(noteFormulaAtom atom) {
        this.mEditingAtom = atom;
    }

    // 엣지 수정시 대상이 되는 엣지 
    private noteFormulaEdge mEditingEdge = null;
    public noteFormulaEdge getEditingEdge() {
        return this.mEditingEdge;
    }
    public void setEditingEdge(noteFormulaEdge edge) {
        this.mEditingEdge = edge;
    }

    // constructor
    public noteFormulaMgr() {
        this.mFormulas = new ArrayList<>();
        this.mPrevEdges = new ArrayList<>();
        this.mPrevAtoms = new ArrayList<>();
    }

    // 메서드 
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

    // 특정 Atom이 속한 Formula를 찾는 메서드
    public noteFormula findFormulaFor(noteFormulaAtom atom) {
        for (noteFormula formula : mFormulas) {
            if (formula.getAtoms().contains(atom)) {
                return formula; // Atom이 포함된 Formula 반환
            }
        }
        return null; // Atom이 어떤 Formula에도 속하지 않을 경우 null 반환
    }

    // 특정 Edge가 속한 Formula를 찾는 메서드
    public noteFormula findFormulaFor(noteFormulaEdge edge) {
        for (noteFormula formula : mFormulas) {
            if (formula.getEdges().contains(edge)) {
                return formula; // 포함된 Formula 반환
            }
        }
        return null; //어떤 Formula에도 속하지 않을 경우 null 반환
    }

    // 현재 존재하는 formula들의 validity 확인 
    public void arrangeFormulas() {
        for (noteFormula formula : this.mFormulas) {
            ArrayList<noteFormulaEdge> edges = (ArrayList<noteFormulaEdge>) formula.getEdges();
            ArrayList<noteFormulaAtom> atoms = (ArrayList<noteFormulaAtom>) formula.getAtoms();

            // 1. 따로 있는 Edge 제거 (atom 없는 edge)
            edges.removeIf(edge -> {
                return edge.getStartAtom() == null || edge.getEndAtom() == null;
            });

//            // 2. atom 혼자 있는 경우 제거 
//            if (formula.getEdges().isEmpty()) {
//                removeFormula(formula);
//            }
            // 3. 중복 edge 제거 (같은 atom 쌍을 연결하는 edge)
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

            // 4. 양쪽 atom이 같은 위치에 있는 edge 제거
            edges.removeIf(edge -> {
                Point2D.Double pos1 = edge.getStartAtom().getPosition();
                Point2D.Double pos2 = edge.getEndAtom().getPosition();
                return pos1.distance(pos2) < 1.0;  // 1픽셀 이내면 같은 위치로 간주
            });

            // 5. 같은 위치의 atom 병합
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

    // 같은 원소인지 확인 
    private boolean hasSameAtoms(noteFormulaEdge edge1, noteFormulaEdge edge2) {
        return (edge1.getStartAtom() == edge2.getStartAtom()
                && edge1.getEndAtom() == edge2.getEndAtom())
                || (edge1.getStartAtom() == edge2.getEndAtom()
                && edge1.getEndAtom() == edge2.getStartAtom());
    }

    // 같은 위치인지 확인
    private boolean isSamePosition(noteFormulaAtom atom1, noteFormulaAtom atom2) {
        Point2D.Double pos1 = atom1.getPosition();
        Point2D.Double pos2 = atom2.getPosition();
        return pos1.distance(pos2) < 1.0; // 1픽셀 이내면 같은 위치로 간주
    }
    
    // 엣지를 바꿔서 연결
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

    // Formula를 제거하는 메서드
    public void removeFormula(noteFormula formula) {
        // 삭제할 Atom과 Edge 수집
        ArrayList<noteFormulaAtom> atomsToRemove = new ArrayList<>(formula.getAtoms());
        ArrayList<noteFormulaEdge> edgesToRemove = new ArrayList<>(formula.getEdges());

        // Atom과 Edge를 순차적으로 삭제
        for (noteFormulaAtom a : atomsToRemove) {
            removeAtom(a);
        }
        for (noteFormulaEdge e : edgesToRemove) {
            removeEdge(e);
        }

        // Formula 리스트에서 제거
        this.mFormulas.remove(formula);
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

    // Atom을 완전히 제거하는 메서드
    public void removeEdge(noteFormulaEdge edge) {
        // prevAtoms에서 제거
        this.mPrevEdges.remove(edge);

        // 현재 Formula에서도 제거
        if (this.mCurrFormula != null) {
            this.mCurrFormula.getEdges().remove(edge);
        }

        // 모든 Formula에서 해당 atom 찾아서 제거
        for (noteFormula formula : this.mFormulas) {
            formula.getEdges().remove(edge);
        }
    }

    public void translateSelectedFormula(double dx, double dy) {
        if (this.mSelectedFormula != null) {
            this.mSelectedFormula.translateTo(dx, dy);
        }
    }

    public ArrayList<noteFormulaEdge> findIntersectingEdge(noteFormula formula, notePenMark penMark) {
        ArrayList<noteFormulaEdge> intersectingEdges = new ArrayList<noteFormulaEdge>();

        // PenMark의 시작점과 끝점 가져오기
        Point2D.Double start = penMark.getStartPoint();
        Point2D.Double end = penMark.getEndPoint();

        // Formula의 모든 엣지를 순회
        for (noteFormulaEdge edge : formula.getEdges()) {
            // 엣지의 시작점과 끝점 가져오기
            Point2D.Double edgeStart = edge.getStartAtom().getPosition();
            Point2D.Double edgeEnd = edge.getEndAtom().getPosition();

            // 엣지와 PenMark가 겹치는지 확인
            if (isIntersecting(start, end, edgeStart, edgeEnd)) {
                intersectingEdges.add(edge);
            }
        }

        return intersectingEdges;
    }

    // 두 선분이 겹치는지 확인하는 메서드
    private boolean isIntersecting(Point2D.Double start1, Point2D.Double end1, Point2D.Double start2, Point2D.Double end2) {
        // d1: (start1->end1) 벡터와 (start1->start2) 벡터의 크로스 프로덕트 부호
        double d1 = (end1.x - start1.x) * (start2.y - start1.y) - (end1.y - start1.y) * (start2.x - start1.x);
        // d2: (start1->end1) 벡터와 (start1->end2) 벡터의 크로스 프로덕트 부호
        double d2 = (end1.x - start1.x) * (end2.y - start1.y) - (end1.y - start1.y) * (end2.x - start1.x);
        // d3: (start2->end2) 벡터와 (start2->start1) 벡터의 크로스 프로덕트 부호
        double d3 = (end2.x - start2.x) * (start1.y - start2.y) - (end2.y - start2.y) * (start1.x - start2.x);
        // d4: (start2->end2) 벡터와 (start2->end1) 벡터의 크로스 프로덕트 부호
        double d4 = (end2.x - start2.x) * (end1.y - start2.y) - (end2.y - start2.y) * (end1.x - start2.x);

        // 두 선분이 서로 다른 방향에 존재해야 교차
        if (d1 * d2 < 0 && d3 * d4 < 0) {
            return true;
        }

        return false;
    }

}
