package note.cmd;

import note.noteApp;
import x.XApp;
import x.XLoggableCmd;

public class noteCmdToDoSomething extends XLoggableCmd {
    // fields 
    // ...
    
    // private constructor
    private noteCmdToDoSomething(XApp app) {
        super(app);
        // ...
    }
    
    public static boolean execute(XApp app) {
        noteCmdToDoSomething cmd = new noteCmdToDoSomething(app);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        noteApp note = (noteApp) this.mApp;
        // ...
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        return sb.toString();
    }
    
}
