package jp.shosato.draw.components;

import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector4d;
import org.lwjgl.BufferUtils;

import jp.shosato.draw.utils.Colors;
import jp.shosato.draw.utils.SingleValueObservable;

import static org.lwjgl.opengl.GL15.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class LabelComponent extends RectangleComponent {
    private Alignment alignment = Alignment.CENTER;
    private Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private Vector4d fontColor = new Vector4d(0, 0, 0, 1);
    private String text;
    private Vector2i textDimension;
    private final int textureId;
    private FontMetrics metrics;

    public enum Alignment {
        CENTER;
    }

    public LabelComponent(double w, double h) {
        this(new Vector2d(0, 0), new Vector2d(w, h), new Vector4d(Colors.GRAY), "");
    }

    public LabelComponent(double w, double h, String text) {
        this(new Vector2d(0, 0), new Vector2d(w, h), new Vector4d(Colors.GRAY), text);
    }

    public LabelComponent(Vector2d topLeft, double w, double h, Vector4d color) {
        this(topLeft, new Vector2d(topLeft.x + w, topLeft.y + h), color, "");
    }

    public LabelComponent(Vector2d topLeft, Vector2d bottomRight, Vector4d color, String text) {
        super(topLeft, bottomRight, color);
        textureId = glGenTextures();
        this.setText(text);
    }

    @Override
    public void draw() {
        super.draw();

        if (text == null || textDimension == null)
            return;

        glBindTexture(GL_TEXTURE_2D, this.textureId);
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();

        int w = this.textDimension.x, h = this.textDimension.y;
        Vector2d textTopLeft = getTextTopLeft(w, h);
        glTranslated(topLeft.x + textTopLeft.x, topLeft.y + textTopLeft.y, 0);

        glBegin(GL_QUADS);

        glTexCoord2d(0, 1);
        glVertex2d(0, h);

        glTexCoord2d(1, 1);
        glVertex2d(w, h);

        glTexCoord2d(1, 0);
        glVertex2d(w, 0);

        glTexCoord2d(0, 0);
        glVertex2d(0, 0);

        glEnd();

        glPopMatrix();
        glDisable(GL_TEXTURE_2D);

    }

    public void setText(String text) {
        this.text = text;
        updateText();
    }

    public String getText() {
        return text;
    }

    public void setFont(Font font) {
        this.font = font;
        updateText();
    }

    /**
     * @apiNote テクスチャの再生成はコストが高いので、テキスト変更時のみ
     */
    public void updateText() {
        this.textDimension = getTextDimension(text, font);
        int w = this.textDimension.x, h = this.textDimension.y;
        BufferedImage bufferedImage = createTextImage(text, w, h, font,
                new Color((float) fontColor.x, (float) fontColor.y, (float) fontColor.z, (float) fontColor.w));
        ByteBuffer buffer = toByteBuffer(bufferedImage);
        transferTexture(this.textureId, buffer, w, h);
    }

    /**
     * @apiNote アラインメント計算
     */
    protected Vector2d getTextTopLeft(int w, int h) {
        switch (alignment) {
            case CENTER: {
                double _w = bottomRight.x - topLeft.x;
                double _h = bottomRight.y - topLeft.y;
                return new Vector2d((_w - w) / 2, (_h - h) / 2);
            }
            default:
                return new Vector2d(0, 0);
        }
    }

    /**
     * @apiNote OpenGLのテクスチャメモリに転送。1バイトアラインメント、RGBA限定
     */
    private static void transferTexture(int id, ByteBuffer buffer, int w, int h) {
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    /**
     * @apiNote awtのBufferedImageではARGBの順でしか作成できないが、OpenGLはRGBAの順なので入れ替える
     * @param bufferedImage awtで作成したバッファ。BufferedImage.TYPE_INT_ARGBのみ
     * @return OpenGLに読み込めるByteBuffer
     */
    private static ByteBuffer toByteBuffer(BufferedImage bufferedImage) {
        assert (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB);

        int w = bufferedImage.getWidth(), h = bufferedImage.getHeight();

        int[] pixels = new int[w * h];
        bufferedImage.getRGB(0, 0, w, h, pixels, 0, w);

        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixelIndex = y * w + x;
                int pixel = pixels[pixelIndex];
                buffer.put((byte) ((pixel >> 16) & 0xff));
                buffer.put((byte) ((pixel >> 8) & 0xff));
                buffer.put((byte) (pixel & 0xff));
                buffer.put((byte) ((pixel >> 24) & 0xff));
            }
        }

        buffer.flip();

        return buffer;
    }

    private Vector2i getTextDimension(String str, Font font) {
        BufferedImage tmpImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = tmpImage.createGraphics();
        metrics = g.getFontMetrics(font);
        g.dispose();

        // width and height cannot be <= 0
        return new Vector2i(Math.max(1, metrics.stringWidth(str)), Math.max(1, metrics.getHeight()));
    }

    /**
     * @apiNote awtのGraphics2Dを用いてビットマップ画像生成
     */
    private static BufferedImage createTextImage(String str, int width, int height, Font font, Color color) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(color);
        g.drawString(str, 0, metrics.getAscent());
        g.dispose();
        return image;

    }

    public FontMetrics getMetrix() {
        return this.metrics;
    }
}
