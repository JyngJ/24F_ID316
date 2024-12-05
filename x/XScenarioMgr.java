package x;

import java.util.ArrayList;

public abstract class XScenarioMgr {
    // fields
    protected XApp mApp = null;
    protected ArrayList<XScenario> mScenarios = null;
    protected XScene mCurrScene = null;
    public XScene getCurrScene() {
        return this.mCurrScene;
    }
    public void setCurrScene(XScene scene) {
        if (this.mCurrScene != null){
            this.mCurrScene.wrapUp();
        }
        scene.getReady();
        this.mCurrScene = scene;
    }
    
    // constructor 
    protected XScenarioMgr(XApp app) {
        this.mApp = app;
        this.mScenarios = new ArrayList<XScenario>();
        this.addScenarios();
        this.setInitCurrScene();
    }
    
    //abstract methods
    protected abstract void addScenarios();

    protected abstract void setInitCurrScene();
    
    protected void addScenario(XScenario scenario) {
        this.mScenarios.add(scenario);
    }
    
}
