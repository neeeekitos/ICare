import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private int width, height;
    private String title;
    private long window;
    public int frames;
    public static long time;
    public Input input;
    private float backgroundR, backgroundG, backgroundB ;
    private boolean isResized;
    private boolean isFullScreen ;

    private GLFWWindowSizeCallback sizeCallback;
    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        //check si c'est déjà initialisé
        if (!GLFW.glfwInit()) {
             System.err.println("Erreur: Pas possible d'initialiser GLFW");
             System.exit(-1);
        }

        input = new Input();

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
//        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, title, isFullScreen ? GLFW.glfwGetPrimaryMonitor() : 0 , 0);
        if (window == 0) {
            System.err.println("Erreur: la fenetre ne peut pas etre créée");
            System.exit(-1);
        }

        // le moniteur
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // centrer sur l'ecram videoMode
        GLFW.glfwSetWindowPos(window, (videoMode.width()-width)/2,(videoMode.height()-height)/2);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        creatCallbacks();

        GLFW.glfwShowWindow(window);

        // set fps 60 per second
        GLFW.glfwSwapInterval(1);

        time = System.currentTimeMillis();
    }

    private void creatCallbacks(){
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true ;
            }
        };
        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetScrollCallback(window,input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window,sizeCallback);
    }

    //retur if it should be closed
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void update() {

        GL11.glViewport(0,0,width,height);
        GL11.glClearColor(backgroundR,backgroundG,backgroundB,1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
        frames++;

        //compteur des frames par seconde
        if (System.currentTimeMillis() > time + 1000) {
            GLFW.glfwSetWindowTitle(window, title + " | FPS : "+ frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    public void destroy() {
        input.destroy();
        sizeCallback.free();
        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public void setBackgroundColor( float r, float g, float b){
        backgroundR = r;
        backgroundG = g;
        backgroundB = b;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        isResized = true;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public long getWindow() {
        return window;
    }

    public static void main(String[] args) {
        Window window =  new Window(800, 600, "GLFW fenetre");
        window.create();

        while (!window.shouldClose()) {
            window.update();
            System.out.println("hey");
            window.swapBuffers();
        }
    }
}
