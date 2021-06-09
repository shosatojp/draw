package jp.shosato.micropaint;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.system.MemoryUtil.*;

import jp.shosato.micropaint.components.BasicComponent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;

public class Window {
    private long windowHandle;

    private BasicComponent rootComponent;

    private int width;
    private int height;

    public void setRootComponent(BasicComponent root) {
        this.rootComponent = root;
    }

    public Window(int windowWidth, int windowHeight) {
        this.width = windowWidth;
        this.height = windowHeight;

        glfwInit();
        windowHandle = createWindow();

    }

    protected long createWindow() {
        // ウィンドウのリサイズは考慮しない
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long window = glfwCreateWindow(width, height, "Draw", NULL, NULL);
        glfwMakeContextCurrent(window);
        createCapabilities();
        return window;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void draw() {
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BITS);

        // アンチエイリアシングの有効化
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);

        // ビューポートをウィンドウに設定
        glPushMatrix();
        glScaled(2f / width, -2f / height, 1f);
        glTranslated(-width / 2f, -height / 2f, 0);
        rootComponent.draw();
        glPopMatrix();
    }
    
    /**
     * 描画・イベントのメインループ
     */
    public void loop() {
        while (!glfwWindowShouldClose(windowHandle)) {
            glfwPollEvents();
            this.draw();
            glfwSwapBuffers(windowHandle);
        }
        glfwTerminate();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
