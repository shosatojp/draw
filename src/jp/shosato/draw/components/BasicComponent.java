package jp.shosato.draw.components;

import static org.lwjgl.opengl.GL15.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedList;

import org.joml.Vector2d;

import jp.shosato.draw.Controller;
import jp.shosato.draw.Window;
import jp.shosato.draw.events.Event;
import jp.shosato.draw.utils.BoundingBox;
import jp.shosato.draw.utils.Hoverable;
import jp.shosato.draw.utils.IBoundingBox;
import jp.shosato.draw.utils.IDrawable2D;
import jp.shosato.draw.utils.Utility;

public abstract class BasicComponent extends Hoverable implements IDrawable2D, IBoundingBox {
    public Vector2d dimension = new Vector2d(0, 0);

    public Vector2d translate = new Vector2d(0, 0);
    public Vector2d scale = new Vector2d(1, 1);
    public double rotate = 0;

    // parent/children
    protected Window window;
    protected Controller controller;
    protected BasicComponent parent;
    protected LinkedList<BasicComponent> children = new LinkedList<>();

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

    public abstract Vector2d getCenter();

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
