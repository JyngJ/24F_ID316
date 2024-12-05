package note;

import java.util.ArrayList;

public class noteFormulaMgr {
    // fields
    private noteFormula mCurrFormula = null;
    public noteFormula getCurrFormula() {
        return this.mCurrFormula;
    }
    public void setCurrFormula (noteFormula formula) {
        this.mCurrFormula = formula;
    }
    
    // 현재 엣지가 시작되고 있는 atom
    private noteFormulaAtom mCurrAtom = null;
    public noteFormulaAtom getCurrAtom() {
        return this.mCurrAtom;
    }
    public void setCurrAtom (noteFormulaAtom atom) {
        this.mCurrAtom = atom;
    }
    
    private ArrayList <noteFormula> mFormulas = null;
    public ArrayList <noteFormula> getFormulas() {
        return this.mFormulas;
    }
    
    private noteFormula mSelectedFormula = null;
    public noteFormula getSelectedFormula(){
        return this.mSelectedFormula;
    }
    
    // constructor
    public noteFormulaMgr() {
        this.mFormulas = new ArrayList<>();
    }
}
