package note.cmd;

import note.noteApp;
import note.noteFormulaEdge;
import note.noteFormulaEdgeDouble;
import note.noteFormulaEdgeTriple;
import note.noteFormulaMgr;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToPromoteEdge extends XLoggableCmd {

    // fields 
    noteFormulaEdge mEdge = null;

    // private constructor
    private noteCmdToPromoteEdge(XApp app, noteFormulaEdge edge) {
        super(app);
        this.mEdge = edge;
    }

    public static boolean execute(XApp app, noteFormulaEdge edge) {
        noteCmdToPromoteEdge cmd = new noteCmdToPromoteEdge(app, edge);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        noteFormulaMgr formulaMgr = note.getFormulaMgr();

        // 엣지 타입에 따라 변경
        String edgeType = mEdge.getClass().getSimpleName();
        switch (edgeType) {
            case "noteFormulaEdgeSingle":
                // Single -> Double
                noteFormulaEdgeDouble newEdgeDouble = new noteFormulaEdgeDouble(mEdge.getStartAtom(), mEdge.getEndAtom());
                formulaMgr.findFormulaFor(mEdge).addEdge(newEdgeDouble);
                break;
            case "noteFormulaEdgeDouble":
                // Double -> Triple
                noteFormulaEdgeTriple newEdgeTriple = new noteFormulaEdgeTriple(mEdge.getStartAtom(), mEdge.getEndAtom());
                formulaMgr.findFormulaFor(mEdge).addEdge(newEdgeTriple);
                break;
            case "noteFormulaEdgeTriple":
                // Triple은 더 이상 변환할 필요 없음
                return false;
        }

        // 기존 엣지 삭제
        note.getFormulaMgr().removeEdge(mEdge);

        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
}
