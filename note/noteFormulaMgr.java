package note;

import java.util.ArrayList;

public class noteFormulaMgr {

    // constants
    public static final double ANGLE_SNAP = 120.0; // 스냅 각도 (120도)

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

}
