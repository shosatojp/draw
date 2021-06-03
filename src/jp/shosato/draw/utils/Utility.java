package jp.shosato.draw.utils;

import org.joml.Vector2d;
import org.joml.Vector4d;

import static org.lwjgl.opengl.GL15.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Utility {

    public static void drawRectangleFill(Vector2d center, Vector2d scale, double rotate) {
        glPushMatrix();

        glTranslated(center.x, center.y, 0);
        glRotated(rotate, 0, 0, 1);
        glScaled(scale.x, scale.y, 1);

        glBegin(GL_QUADS);
        glVertex2d(-0.5, -0.5);
        glVertex2d(-0.5, 0.5);
        glVertex2d(0.5, 0.5);
        glVertex2d(0.5, -0.5);
        glEnd();
        glPopMatrix();
    }

    public static void drawRectangleLineStrip(Vector2d center, Vector2d scale, double rotate) {
        glPushMatrix();

        glTranslated(center.x, center.y, 0);
        glRotated(rotate, 0, 0, 1);
        glScaled(scale.x, scale.y, 1);

        glBegin(GL_LINE_STRIP);
        glVertex2d(-0.5, -0.5);
        glVertex2d(-0.5, 0.5);
        glVertex2d(0.5, 0.5);
        glVertex2d(0.5, -0.5);
        glVertex2d(-0.5, -0.5);
        glEnd();
        glPopMatrix();
    }

    public static void drawRectangleFill(Vector2d dimension) {
        glBegin(GL_QUADS);
        glVertex2d(0, 0);
        glVertex2d(dimension.x, 0);
        glVertex2d(dimension.x, dimension.y);
        glVertex2d(0, dimension.y);
        glEnd();
    }

    public static void drawRectangleFill(Vector2d topLeft, Vector2d bottomRight) {
        glBegin(GL_QUADS);
        glVertex2d(topLeft.x, topLeft.y);
        glVertex2d(bottomRight.x, topLeft.y);
        glVertex2d(bottomRight.x, bottomRight.y);
        glVertex2d(topLeft.x, bottomRight.y);
        glEnd();
    }

    public static void drawRectangleLineStrip(Vector2d topLeft, Vector2d bottomRight) {
        glBegin(GL_LINE_STRIP);
        glVertex2d(topLeft.x, topLeft.y);
        glVertex2d(bottomRight.x, topLeft.y);
        glVertex2d(bottomRight.x, bottomRight.y);
        glVertex2d(topLeft.x, bottomRight.y);
        glVertex2d(topLeft.x, topLeft.y);
        glEnd();
    }

    public static boolean contains(Vector2d pos, Vector2d center, Vector2d scale, double rotate) {
        double radian = rotate * Math.PI / 180;
        Vector2d old = new Vector2d(pos).sub(center);
        Vector2d old3 = new Vector2d(Math.cos(radian) * old.x + Math.sin(radian) * old.y,
                -Math.sin(radian) * old.x + Math.cos(radian) * old.y);
        Vector2d old2 = new Vector2d(old3.x / scale.x, old3.y / scale.y);

        return Math.abs(old2.x) <= 0.5 && Math.abs(old2.y) <= 0.5;
    }

    public static Vector2d marginedScale(Vector2d scale, double margin) {
        return new Vector2d((Math.abs(scale.x) + margin * 2) * scale.x / Math.abs(scale.x),
                (Math.abs(scale.y) + margin * 2) * scale.y / Math.abs(scale.y));
    }

    public static BoundingBox getBB(Vector2d a, Vector2d b) {
        Vector2d topLeft = new Vector2d(Math.min(a.x, b.x), Math.min(a.y, b.y));
        Vector2d bottomRight = new Vector2d(Math.max(a.x, b.x), Math.max(a.y, b.y));
        return new BoundingBox(topLeft, bottomRight);
    }

    public static BoundingBox getBB(ArrayList<Vector2d> vertices) {
        Vector2d min = new Vector2d(vertices.get(0));
        Vector2d max = new Vector2d(vertices.get(0));

        vertices.stream().skip(1).forEach((Vector2d v) -> {
            min.x = Math.min(min.x, v.x);
            max.x = Math.max(max.x, v.x);
            min.y = Math.min(min.y, v.y);
            max.y = Math.max(max.y, v.y);
        });

        return new BoundingBox(min, max);
    }

    public static double getArgRadian(Vector2d pos) {
        Vector2d norm = new Vector2d(pos).normalize();
        if (pos.y < 0) {
            // 上半分
            return Math.PI * 2 - Math.acos(norm.x);
        } else {
            // 下半分
            return Math.acos(norm.x);
        }
    }

    public static double toRadian(double degree) {
        return degree / 180 * Math.PI;
    }

    public static double toDegree(double radian) {
        return radian / Math.PI * 180;
    }

    public static boolean isInsidePolygon(Vector2d pos, ArrayList<Vector2d> vertices) {
        if (vertices.size() < 3) {
            return false;
        }
        // http://shopping2.gmobb.jp/htdmnr/www08/c01/algorithm/polygon01.html
        Vector2d p1, p2;
        boolean inside = false;
        Vector2d oldPoint = vertices.get(vertices.size() - 1);
        for (int i = 0; i < vertices.size(); i++) {
            Vector2d newPoint = vertices.get(i);
            if (newPoint.x > oldPoint.x) {
                p1 = oldPoint;
                p2 = newPoint;
            } else {
                p1 = newPoint;
                p2 = oldPoint;
            }
            if ((p1.x < pos.x) == (pos.x <= p2.x) && (pos.y - p1.y) * (p2.x - p1.x) < (p2.y - p1.y) * (pos.x - p1.x)) {
                inside = !inside;
            }
            oldPoint = newPoint;
        }
        return inside;
    }

    public static double getAngleRadian(Vector2d a, Vector2d b, Vector2d c) {
        Vector2d v0 = new Vector2d(a).sub(b);
        Vector2d v1 = new Vector2d(c).sub(b);

        return Math.acos(new Vector2d(v0).dot(v1) / (v0.length() * v1.length()));
    }

    public static double getAreaDimension(Vector2d a, Vector2d b, Vector2d c) {
        Vector2d v0 = new Vector2d(a).sub(b);
        Vector2d v1 = new Vector2d(c).sub(b);
        double angle = Math.acos(new Vector2d(v0).dot(v1) / (v0.length() * v1.length()));
        return Math.sin(angle) * v0.length() * v1.length();
    }

    public static String getColorCode(Vector4d rgba) {
        return String.format("#%02X%02X%02X%02X", (int) (rgba.x * 255), (int) (rgba.y * 255), (int) (rgba.z * 255),
                (int) (rgba.w * 255));
    }

    public static void getScreenshot(int x, int y, int w, int h, String path) {
        int pixels[] = new int[w * h];
        glReadBuffer(GL_FRONT);
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glReadPixels(x, y, w, h, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        try {
            File file = new File(path);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

            outputStream.write(String.format("P6\n%s %s\n255\n", w, h).getBytes());
            for (int i = h - 1; i >= 0; i--) {
                for (int j = 0; j < w; j++) {
                    int pixel = pixels[(i * w + j)];
                    int a = (pixel >> 24) & 0xff;
                    int b = (pixel >> 16) & 0xff;
                    int g = (pixel >> 8) & 0xff;
                    int r = (pixel) & 0xff;

                    outputStream.write((byte) r);
                    outputStream.write((byte) g);
                    outputStream.write((byte) b);
                }
            }
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Vector2d translate(Vector2d pos, Vector2d translate) {
        return new Vector2d(pos).add(translate);
    }

    public static void translateInplace(Vector2d pos, Vector2d translate) {
        pos.add(translate);
    }

    public static Vector2d rotate(Vector2d pos, double rotate_degree) {
        double radian = rotate_degree * Math.PI / 180;
        return new Vector2d(Math.cos(radian) * pos.x + Math.sin(radian) * pos.y,
                -Math.sin(radian) * pos.x + Math.cos(radian) * pos.y);
    }

    public static void rotateInplace(Vector2d pos, double rotate_degree) {
        double radian = rotate_degree * Math.PI / 180;
        double x = Math.cos(radian) * pos.x + Math.sin(radian) * pos.y;
        double y = -Math.sin(radian) * pos.x + Math.cos(radian) * pos.y;
        pos.x = x;
        pos.y = y;
    }

    public static Vector2d scale(Vector2d pos, Vector2d scale) {
        return new Vector2d(pos).mul(scale);
    }

    public static void scaleInplace(Vector2d pos, Vector2d scale) {
        pos.mul(scale);
    }

    public static Vector2d transform(Vector2d pos, Vector2d center, Vector2d translate, Vector2d scale,
            double rotate_degree) {
        Vector2d newpos = new Vector2d(pos);
        Utility.translateInplace(newpos, new Vector2d(center).mul(-1));
        Utility.rotateInplace(newpos, rotate_degree);
        Utility.scaleInplace(newpos, scale);
        Utility.translateInplace(newpos, translate);
        Utility.translateInplace(newpos, center);
        return newpos;
    }

    public static Vector2d untransform(Vector2d pos, Vector2d center, Vector2d translate, Vector2d scale,
            double rotate_degree) {
        Vector2d newpos = new Vector2d(pos);
        Utility.translateInplace(newpos, new Vector2d(center).mul(-1));
        Utility.translateInplace(newpos, new Vector2d(translate).mul(-1));
        Utility.rotateInplace(newpos, -rotate_degree);
        Utility.scaleInplace(newpos, new Vector2d(1, 1).div(scale));
        Utility.translateInplace(newpos, center);
        return newpos;
    }

    public static void glTransform(Vector2d dimension, Vector2d translate, Vector2d scale, double rotate) {
        glTranslated(dimension.x / 2, dimension.y / 2, 0);
        glTranslated(translate.x, translate.y, 0);
        glScaled(scale.x, scale.y, 1);
        glRotated(rotate, 0, 0, -1);
        glTranslated(-dimension.x / 2, -dimension.y / 2, 0);
    }
}
