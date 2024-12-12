package note;

import java.util.ArrayList;

public class notePtCurveMgr {
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

    public notePtCurveMgr() {
        this.mPtCurves = new ArrayList<>();
    }

    // Remove curve
    public void removeCurve(notePtCurve curve) {
        if (curve != null && this.mPtCurves.contains(curve)) {
            this.mPtCurves.remove(curve);
        }
    }

    // Set selection state for multiple curves
    public void setSelectedState(ArrayList<notePtCurve> curves,
        notePtCurve.SelectState state) {
        for (notePtCurve curve : curves) {
            curve.setSelectState(state);
        }
    }
}
