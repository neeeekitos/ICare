import org.lwjgl.glfw.GLFW;
import sun.awt.WindowIDProvider;

public class Main implements Runnable{

    public Thread fluxAffichage;
    public static Window window;
    public static final int WIDTH = 1280, HEIGHT = 600;


    public void start() {
        fluxAffichage = new Thread(this, "affichage");
        fluxAffichage.start();
    }

    public static void init() {
        System.out.println("Initialisation d'affichage");
        window = new Window(WIDTH, HEIGHT, "ICare");
        window.setBackgroundColor(1,0,0);
        window.create();
    }

    public void run() {
        init();
        while(!window.shouldClose() ) {
            update();
            render();
        }
        window.destroy();
    }

    public void update() {
        window.update();
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT) ){
            System.out.println("X :" + Input.getMouseX() + ", Y :" + Input.getMouseY());
        }
    }

    public void render() {
        window.swapBuffers();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
