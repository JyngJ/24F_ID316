package note.cmd;

import java.awt.geom.Point2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.notePtCurve;
import note.notePtCurve.SelectState;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToSelectObject extends XLoggableCmd {
    private ArrayList<Point2D.Double> dragPath;

    private noteCmdToSelectObject(XApp app, ArrayList<Point2D.Double> dragPath) {
        super(app);
        this.dragPath = dragPath;
    }

    public static boolean execute(XApp app, ArrayList<Point2D.Double> dragPath) {
        noteCmdToSelectObject cmd = new noteCmdToSelectObject(app, dragPath);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;

        // Convert dragPath to closed Path2D
        Path2D.Double selectionPath = new Path2D.Double();
        if (!dragPath.isEmpty()) {
            Point2D.Double pt0 = dragPath.get(0);
            selectionPath.moveTo(pt0.x, pt0.y);
            for (Point2D.Double pt : dragPath) {
                selectionPath.lineTo(pt.x, pt.y);
            }
            selectionPath.closePath();
        }

        // Check and select curves inside selection path
        ArrayList<notePtCurve> ptCurves = note.getPtCurveMgr().getPtCurves();
        for (notePtCurve curve : ptCurves) {
            boolean isInside = curve.getPts().stream().allMatch(selectionPath::contains);
            if (isInside) {
                curve.setSelectState(SelectState.SELECTED);
                note.getPtCurveMgr().getSelectedPtCurves().add(curve);
            }
        }
        ArrayList<noteFormula> molecules = note.getFormulaMgr().getFormulas();
        for (noteFormula molecule : molecules) {
            boolean isInside = true;
            for (noteFormulaAtom atom : molecule.getAtoms()) {
                if (selectionPath.contains(atom.getPosition())) {
                    //pass
                } else {
                    isInside = false;
                }
            if (isInside) {
                molecule.setSelectState(noteFormula.SelectState.SELECTED);
                note.getFormulaMgr().getSelectedFormulas_d().add(molecule);
            }
            }
        }
        return true;
        }

    @Override
    protected String createLog() {
        return "noteCmdToSelectObject executed";
    }
}
