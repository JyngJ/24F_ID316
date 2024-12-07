package note.cmd;

import java.awt.Point;
import java.awt.geom.Point2D;
import note.noteApp;
import note.noteFormula;
import note.noteFormulaAtom;
import note.noteFormulaMgr;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToCreateAtom extends XLoggableCmd {
    // fields
    private Point2D.Double position;
    private noteFormulaAtom createdAtom;

    private noteCmdToCreateAtom(XApp app, Point2D.Double position) {
        super(app);
        this.position = position;
    }

    // execute 메서드
    public static noteFormulaAtom execute(XApp app, Point2D.Double position) {
        noteCmdToCreateAtom cmd = new noteCmdToCreateAtom(app, position);
        cmd.execute();
        return cmd.createdAtom;
    }

    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        noteFormulaMgr formulaMgr = note.getFormulaMgr();

        // Atom 생성
        this.createdAtom = new noteFormulaAtom("C", position);

        // 새로운 Formula 생성
        noteFormula newFormula = new noteFormula();
        newFormula.addAtom(this.createdAtom);

        // Formula를 Formula 리스트에 추가
        formulaMgr.getFormulas().add(newFormula);

        System.out.println("Created Atom at " + position + " and added to a new Formula.");

        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append("Created Atom at ").append(position);
        return sb.toString();
    }
}
