package note;


import java.util.ArrayList;

public class notePtCurveMgr {
    // fields
    private notePtCurve mCurrPtCurve = null;
    public notePtCurve getCurrPtCurve() {
        return this.mCurrPtCurve;
    }
    public void setCurrPtCurve(notePtCurve ptCurve) {
        this.mCurrPtCurve = ptCurve;
    }
    
    private ArrayList<notePtCurve> mPtCurves = null;
    public ArrayList<notePtCurve> getPtCurves() {
        return this.mPtCurves;
    }
    
    private ArrayList<notePtCurve> mSelectedPtCurves = null;
    public ArrayList<notePtCurve> getSelectedPtCurves(){
        return this.mSelectedPtCurves;
    }
    
    // constructor
    public notePtCurveMgr() {
        this.mPtCurves = new ArrayList<>();
        this.mSelectedPtCurves = new ArrayList<>();
    }
    
    // erase 기능을 위해 추가
    
    
    
    public void removeCurve(notePtCurve curve) {
    if (curve != null && this.mPtCurves.contains(curve)) {
        this.mPtCurves.remove(curve);
    }
}

    
}
