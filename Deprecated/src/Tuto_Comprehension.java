import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;

public class Tuto_Comprehension {

    public Tuto_Comprehension(){
        System.out.println("Bonjour");
        if ( glfwInit() != true ){
            System.out.println(" Initialisaion raté");
            System.exit(1);
        }

        long fenetre = glfwCreateWindow(640,480,"Fenetre",0,0);

        glfwShowWindow(fenetre);
        glfwMakeContextCurrent(fenetre);
        GL.createCapabilities();
        //glClearColor();

        while (glfwWindowShouldClose(fenetre) == false){
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT);
            glBegin(GL_TRIANGLES);
                glColor4f(1,0,0,0);
                glVertex2f(-0.5f,0.5f);

                glColor4f(0,1,0,0);
                glVertex2f(0.5f,0.5f);

                glColor4f(0,0,0,1);
                glVertex2f(0.5f,-0.5f);

                glColor4f(0,0,1,0);
                //glVertex2f(-0.5f,-0.5f);
                glEnd();
            glfwSwapBuffers(fenetre);
        }

        glfwTerminate();
    }

    public static void main (String [] args ){
        Tuto_Comprehension fen = new Tuto_Comprehension();
    }

}
