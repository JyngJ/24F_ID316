package note.cmd;

import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaEdge;
import note.noteFormulaMgr;
import x.XApp;
import x.XLoggableCmd;


public class noteCmdToMergeFormula extends XLoggableCmd {
    // 병합할 Formula
    private noteFormula sourceFormula;
    private noteFormula targetFormula;

    // private constructor
    private noteCmdToMergeFormula(XApp app, noteFormula sourceFormula, noteFormula targetFormula) {
        super(app);
        this.sourceFormula = sourceFormula;
        this.targetFormula = targetFormula;
    }

    // 실행 메서드
    public static boolean execute(XApp app, noteFormula sourceFormula, noteFormula targetFormula) {
        noteCmdToMergeFormula cmd = new noteCmdToMergeFormula(app, sourceFormula, targetFormula);
        return cmd.execute();
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        noteFormulaMgr formulaMgr = note.getFormulaMgr();

        // 같은 Formula라면 병합하지 않음
        if (sourceFormula == targetFormula) {
            System.out.println("Source and Target Formulas are the same. No merge performed.");
            return true;
        }

        // 병합 수행
        mergeFormulas(sourceFormula, targetFormula, formulaMgr);
        return true;
    }

    private void mergeFormulas(noteFormula source, noteFormula target, noteFormulaMgr formulaMgr) {
        // Source Formula의 Atom과 Edge를 Target Formula로 이동
        for (noteFormulaAtom atom : source.getAtoms()) {
            target.addAtom(atom);
        }
        for (noteFormulaEdge edge : source.getEdges()) {
            target.addEdge(edge);
        }
        
        if (source == formulaMgr.getEditingFormula() || 
            target == formulaMgr.getEditingFormula()) {
            formulaMgr.setEditingFormula(target);
        }

        // Source Formula를 Formula List에서 제거
        formulaMgr.getFormulas().remove(source);

        System.out.println("Merged Source Formula into Target Formula.");
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append("Merged ").append(sourceFormula).append(" into ").append(targetFormula);
        return sb.toString();
    }
}
