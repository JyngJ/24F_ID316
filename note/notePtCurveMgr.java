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

    public ArrayList<notePtCurve> getSelectedPtCurves() {
        return this.mSelectedPtCurves;
    }

    // constructor
    public notePtCurveMgr() {
        this.mPtCurves = new ArrayList<>();
    }

    // Remove curve
    // erase 기능을 위해 추가
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

    public void translateSelectedCurves(double dx, double dy) {
        for (notePtCurve curve : this.mSelectedPtCurves) {
            curve.translateTo(dx, dy);
        }
    }

}
