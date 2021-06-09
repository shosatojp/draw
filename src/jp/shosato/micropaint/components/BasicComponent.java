package jp.shosato.micropaint.components;

import static org.lwjgl.opengl.GL15.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;

import org.joml.Vector2d;

import jp.shosato.micropaint.Window;
import jp.shosato.micropaint.controllers.Controller;
import jp.shosato.micropaint.events.Event;
import jp.shosato.micropaint.utils.BoundingBox;
import jp.shosato.micropaint.utils.Hoverable;
import jp.shosato.micropaint.utils.IBoundingBox;
import jp.shosato.micropaint.utils.IDrawable2D;
import jp.shosato.micropaint.utils.Utility;

public abstract class BasicComponent extends Hoverable implements IDrawable2D, IBoundingBox {
    /**
     * 縦横
     */
    public Vector2d dimension = new Vector2d(0, 0);
    /**
     * 移動
     */
    public Vector2d translate = new Vector2d(0, 0);
    /**
     * 拡大縮小
     */
    public Vector2d scale = new Vector2d(1, 1);
    /**
     * 回転（Z軸）
     */
    public double rotate = 0;

    protected Window window;
    protected Controller controller;
    /**
     * 親要素
     */
    protected BasicComponent parent;
    /**
     * 子要素
     */
    protected LinkedList<BasicComponent> children = new LinkedList<>();

    /**
     * 子要素を追加。親要素やウィンドウの情報を設定
     */
    public void addChildComponent(BasicComponent child) {
        child.parent = this;
        child.controller = this.controller;
        child.window = this.window;

        this.children.add(child);
    }

    public Window getWindow() {
        return window;
    }

    public Controller getController() {
        return controller;
    }

    public LinkedList<BasicComponent> getChildren() {
        return children;
    }

    public void removeChildren() {
        children.clear();
    }

    public void removeChild(BasicComponent child) {
        children.remove(child);
    }

    public BasicComponent getParent() {
        return parent;
    }

    /**
     * 中央の座標
     */
    public abstract Vector2d getCenter();

    /**
     * 変形を適用してから子要素を再帰的に描画
     */
    @Override
    public void draw() {
        glPushMatrix();
        Utility.glTransform(dimension, translate, scale, rotate);
        for (BasicComponent child : children) {
            child.draw();
        }
        glPopMatrix();
    }
}
