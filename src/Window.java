import org.lwjgl.glfw.*;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;

public class Window {

    private int width, height;
    private String title;
    private long window;

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

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

        if (window == 0) {
            System.err.println("Erreur: la fenetre ne peut pas etre créée");
            System.exit(-1);
        }

        // le moniteur
        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        // centrer sur l'ecram videoMode
        GLFW.glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        GLFW.glfwShowWindow(window);

        // set fps
        GLFW.glfwSwapInterval(1);
    }

    //retur if it should be closed
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void update() {
        GLFW.glfwPollEvents();
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
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
