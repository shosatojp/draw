package jp.shosato.micropaint.models;

import java.util.HashMap;
import java.util.HashSet;

import jp.shosato.micropaint.tools.Tool;
import jp.shosato.micropaint.utils.SingleValueObservable;

public class ToolModel {

    public final SingleValueObservable<HashMap<Tool, Boolean>> tools = new SingleValueObservable<>(new HashMap<>());

    public void addTool(Tool tool) {
        this.tools.getValue().put(tool, false);
        this.tools.notifyChange();
    }

    /**
     * ツールの有効化
     */
    public void setTool(Tool tool) {
        for (Tool _tool : this.tools.getValue().keySet()) {
            this.tools.getValue().put(_tool, false);
        }
        this.tools.getValue().put(tool, true);
        this.tools.notifyChange();
    }
}
