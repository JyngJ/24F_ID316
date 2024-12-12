package note;

import note.scenario.noteDefaultScenario;
import note.scenario.noteDrawScenario;
import note.scenario.noteFormulaScenario;
import note.scenario.noteSelectScenario;
import x.XScenarioMgr;

public class noteScenarioMgr extends XScenarioMgr {
    // constructor
    public noteScenarioMgr(noteApp note) {
        super(note);
    }

    @Override
    protected void addScenarios() {
        this.addScenario(noteDefaultScenario.createSingleton(this.mApp));
        this.addScenario(noteFormulaScenario.createSingleton(this.mApp));
        this.addScenario(noteDrawScenario.createSingleton(this.mApp));
        this.addScenario(noteSelectScenario.createSingleton(this.mApp));
//        this.addScenario(JSIGestureScenario.createSingleton(this.mApp));
//        this.addScenario(JSINavigateScenario.createSingleton(this.mApp));
//        this.addScenario(JSIColorScenario.createSingleton(this.mApp));
    }

    @Override
    protected void setInitCurrScene() {
        this.setCurrScene(noteDefaultScenario.ReadyScene.getSingleton());
    }
    
}
