package note.cmd;

import note.noteApp;
import note.noteFormulaEdge;
import note.noteFormulaEdgeDouble;
import note.noteFormulaEdgeSingle;
import note.noteFormulaEdgeTriple;
import note.noteFormulaMgr;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToDemoteEdge extends XLoggableCmd {

    // fields 
    noteFormulaEdge mEdge = null;
    
    // private constructor
    private noteCmdToDemoteEdge(XApp app, noteFormulaEdge edge) {
        super(app);
        this.mEdge = edge;
    }
    
    public static boolean execute(XApp app, noteFormulaEdge edge) {
        noteCmdToDemoteEdge cmd = new noteCmdToDemoteEdge(app, edge);
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
                // Single은 변환 안함 대신 두개로 나누기
                noteCmdToDeleteEdge.execute(note, mEdge);
                break;
            case "noteFormulaEdgeDouble":
                // Double -> Single
                noteFormulaEdgeSingle newEdgeSingle = new noteFormulaEdgeSingle(mEdge.getStartAtom(), mEdge.getEndAtom());
                formulaMgr.findFormulaFor(mEdge).addEdge(newEdgeSingle);
                break;
            case "noteFormulaEdgeTriple":
                // Triple -> Double
                noteFormulaEdgeDouble newEdgeDouble = new noteFormulaEdgeDouble(mEdge.getStartAtom(), mEdge.getEndAtom());
                formulaMgr.findFormulaFor(mEdge).addEdge(newEdgeDouble);
                break;
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
