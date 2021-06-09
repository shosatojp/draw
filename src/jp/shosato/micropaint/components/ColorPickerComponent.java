package jp.shosato.micropaint.components;

import org.joml.Vector2d;
import org.joml.Vector4d;

import jp.shosato.micropaint.events.EventHandler;
import jp.shosato.micropaint.events.handlers.ColorChangedEvent;
import jp.shosato.micropaint.events.handlers.ColorChangedEventHandler;
import jp.shosato.micropaint.events.mouse.MouseClickEventListener;
import jp.shosato.micropaint.events.mouse.MouseEvent;
import jp.shosato.micropaint.events.mouse.MouseLeaveEventListener;
import jp.shosato.micropaint.events.mouse.MouseMoveEventListener;
import jp.shosato.micropaint.utils.Utility;

import static org.lwjgl.opengl.GL15.*;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class ColorPickerComponent extends RectangleComponent
        implements MouseMoveEventListener, MouseClickEventListener, MouseLeaveEventListener {

    public EventHandler<ColorChangedEventHandler> onColorChanged = new EventHandler<>();

    private boolean pickkingH = false;
    private boolean pickkingSV = false;
    private double h = 0, s = 1, v = 1;
    private Vector2d svPosition;
    private double margin = 10;

    public ColorPickerComponent(double w, double h) {
        this(new Vector2d(0, 0), w, h, 0, 1, 1);
    }

    public ColorPickerComponent(Vector2d translate, double width, double height, double h, double s, double v) {
        super(translate, width, height, HSVtoRGB(h, s, v));
        this.h = h;
        this.s = s;
        this.v = v;
    }

    private double getSize() {
        return Math.min(dimension.x, dimension.y);
    }

    private double getCircleWidth() {
        return getSize() / 8;
    }

    private double getOuterRadius() {
        return getSize() / 2 - margin;
    }

    private double getInnerRadius() {
        return getOuterRadius() - getCircleWidth();
    }

    @Override
    public void draw() {
        super.draw();

        double outerRadius = getOuterRadius();
        double innerRadius = getInnerRadius();

        glPushMatrix();
        Vector2d center = getCenter();
        glTranslated(center.x, center.y, 0);
        // 環
        {
            glBegin(GL_TRIANGLE_STRIP);

            double arg = 0;
            for (int i = 0, length = 51; i <= length; i++) {
                Vector4d rgb = HSVtoRGB(arg * 180 / Math.PI, 1, 1);
                glColor4d(rgb.x, rgb.y, rgb.z, rgb.w);
                glVertex2d(outerRadius * Math.cos(arg), outerRadius * Math.sin(arg));
                glVertex2d(innerRadius * Math.cos(arg), innerRadius * Math.sin(arg));
                arg += (Math.PI * 2) / length;
            }

            glEnd();
        }

        // 三角
        {
            glBegin(GL_TRIANGLES);

            double h_degree = Utility.toDegree(h);
            Vector4d rgb;
            rgb = HSVtoRGB(h_degree, 0, 0);
            ArrayList<Vector2d> vertices = getTriangleVertices();
            glColor4d(rgb.x, rgb.y, rgb.z, rgb.w);
            glVertex2d(vertices.get(0).x, vertices.get(0).y);
            rgb = HSVtoRGB(h_degree, 0, 1);
            glColor4d(rgb.x, rgb.y, rgb.z, rgb.w);
            glVertex2d(vertices.get(1).x, vertices.get(1).y);
            rgb = HSVtoRGB(h_degree, 1, 1);
            glColor4d(rgb.x, rgb.y, rgb.z, rgb.w);
            glVertex2d(vertices.get(2).x, vertices.get(2).y);

            glEnd();
        }

        // handle
        {
            double w_angle = Utility.toRadian(3);
            glColor4d(0.7, 0.7, 0.7, 1);
            glBegin(GL_POLYGON);
            glVertex2d((outerRadius + 5) * Math.cos(h - w_angle), (outerRadius + 5) * Math.sin(h - w_angle));
            glVertex2d((outerRadius - 20) * Math.cos(h), (outerRadius - 20) * Math.sin(h));
            glVertex2d((outerRadius + 5) * Math.cos(h + w_angle), (outerRadius + 5) * Math.sin(h + w_angle));
            glEnd();
        }
        glPopMatrix();
    }

    private void updateColor() {
        this.color = HSVtoRGB(Utility.toDegree(h), s, v);
        if (onColorChanged != null)
            onColorChanged.invoke(new ColorChangedEvent(this.color));
    }

    private ArrayList<Vector2d> getTriangleVertices() {
        double innerRadius = getInnerRadius();

        ArrayList<Vector2d> vertices = new ArrayList<>();
        vertices.add(new Vector2d(innerRadius * Math.cos(1 * 2 * Math.PI / 3 + h),
                innerRadius * Math.sin(1 * 2 * Math.PI / 3 + h)));
        vertices.add(new Vector2d(innerRadius * Math.cos(2 * 2 * Math.PI / 3 + h),
                innerRadius * Math.sin(2 * 2 * Math.PI / 3 + h)));
        vertices.add(new Vector2d(innerRadius * Math.cos(3 * 2 * Math.PI / 3 + h),
                innerRadius * Math.sin(3 * 2 * Math.PI / 3 + h)));
        return vertices;
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
        switch (event.getButton()) {
            case GLFW_MOUSE_BUTTON_LEFT:
                switch (event.getAction()) {
                    case GLFW_PRESS: {
                        Vector2d pos = event.getPos();
                        Vector2d translatedPos = new Vector2d(pos).sub(getCenter());
                        if (containsInCircle(pos)) {
                            this.h = Utility.getArgRadian(translatedPos);
                            this.pickkingH = true;
                            updateColor();
                            break;
                        }

                        ArrayList<Vector2d> vertices = getTriangleVertices();
                        if (Utility.isInsidePolygon(translatedPos, vertices)) {
                            Vector2d sv = getSV(translatedPos, vertices);
                            this.s = sv.x;
                            this.v = sv.y;
                            this.pickkingSV = true;
                            updateColor();
                            break;
                        }
                    }
                        break;
                    case GLFW_RELEASE:
                        this.setCursor(GLFW_ARROW_CURSOR);
                        this.pickkingH = false;
                        this.pickkingSV = false;
                    default:
                        break;
                }
                break;
        }
    }

    private Vector2d getSV(Vector2d translatedPos, ArrayList<Vector2d> vertices) {
        double ad0 = Utility.getAreaDimension(translatedPos, vertices.get(1), vertices.get(2));
        double ad1 = Utility.getAreaDimension(vertices.get(0), translatedPos, vertices.get(2));
        double ad2 = Utility.getAreaDimension(vertices.get(0), vertices.get(1), translatedPos);
        double ad = ad0 + ad1 + ad2;

        Vector2d sv = (new Vector2d(0, 1).mul(ad1 / ad)).add(new Vector2d(1, 1).mul(ad2 / ad));
        return sv;
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        this.setCursor(GLFW_ARROW_CURSOR);
        this.pickkingH = false;
        this.pickkingSV = false;
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        Vector2d translatedPos = new Vector2d(event.getPos()).sub(getCenter());
        if (this.pickkingH) {
            this.h = Utility.getArgRadian(translatedPos);
            updateColor();
        } else if (this.pickkingSV) {
            ArrayList<Vector2d> vertices = getTriangleVertices();
            Vector2d sv = getSV(translatedPos, vertices);
            this.s = sv.x;
            this.v = sv.y;
            this.pickkingSV = true;
            updateColor();
        } else {
            ArrayList<Vector2d> vertices = getTriangleVertices();
            this.setCursor(containsInCircle(event.getPos()) || Utility.isInsidePolygon(translatedPos, vertices)
                    ? GLFW_HAND_CURSOR
                    : GLFW_ARROW_CURSOR);
        }
    }

    private boolean containsInCircle(Vector2d pos) {
        double r = pos.distance(getCenter());

        double outerRadius = getOuterRadius();
        double innerRadius = getInnerRadius();

        return innerRadius <= r && r <= outerRadius;
    }

    public static Vector4d HSVtoRGB(double h, double s, double v) {
        double c = s;
        double h_prime = h / 60;
        double x = c * (1 - Math.abs(h_prime % 2 - 1));

        Vector4d v_c = new Vector4d(v - c, v - c, v - c, 1);

        switch ((int) h_prime) {
            case 0:
                return v_c.add(c, x, 0, 0);
            case 1:
                return v_c.add(x, c, 0, 0);
            case 2:
                return v_c.add(0, c, x, 0);
            case 3:
                return v_c.add(0, x, c, 0);
            case 4:
                return v_c.add(x, 0, c, 0);
            case 5:
                return v_c.add(c, 0, x, 0);
            default:
                return new Vector4d(0, 0, 0, 1);
        }
    }

    public Vector4d getHSV() {
        return new Vector4d(h, s, v, 1);
    }

    public Vector4d getRGBA() {
        return HSVtoRGB(Utility.toDegree(h), s, v);
    }

    @Override
    public void onMouseClicked(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseLeave(MouseEvent event, boolean captureing) {
    }

    @Override
    public void onMouseMoved(MouseEvent event, boolean captureing) {
    }
}
