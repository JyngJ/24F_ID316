
package x;

// SSD에서 모든 화살표 
public class XCmdToChangeScene extends XLoggableCmd {
    // fields
    private XScene mFromScene = null;
    private XScene mToScene = null;
    private XScene mReturnScene = null;
    
    // private constructor
    private XCmdToChangeScene(XApp app, XScene toScene, XScene returnScene) {
        super(app);
        this.mFromScene = this.mApp.getScenarioMgr().getCurrScene();
        this.mToScene = toScene;
        this.mReturnScene = returnScene;
    }
    
    // static method to construct and execute this command
    public static boolean execute(XApp app, XScene toScene, XScene returnScene) {
        XCmdToChangeScene cmd = new XCmdToChangeScene(app, toScene, returnScene);
        return cmd.execute();
    }
    
    @Override
    protected boolean defineCmd() {
        this.mToScene.setReturnScene(this.mReturnScene);
        this.mApp.getScenarioMgr().setCurrScene(this.mToScene);
        return true;
    }

    @Override
    protected String createLog() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append("\t");
        sb.append(this.mFromScene.getClass().getSimpleName()).append("\t");
        XScene currScene = this.mApp.getScenarioMgr().getCurrScene();
        sb.append(currScene.getClass().getSimpleName()).append("\t");
        if (this.mReturnScene == null) {
            sb.append("null");
        } else {
            sb.append(currScene.getReturnScene().getClass().getSimpleName());
        }
        return sb.toString();
    }
    
}
