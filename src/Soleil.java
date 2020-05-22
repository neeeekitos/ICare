import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Soleil extends Window {

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

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private org.lwjgl.opengl.ARBMatrixPalette projectionMatrix;

    public Soleil (int width, int height, String title) {
        super(width,height,title);
    }

    public void update (){
        super.update();
        GL11.glBegin(GL_QUADS);
        GL11.glColor4f(0,1,0,0);
        GL11.glVertex3d(0.5f,0.5f,0);
        GL11.glVertex3d(-0.5f,0.5f,0);
        GL11.glVertex3d(-0.5f,-0.5f,0);
        GL11.glColor4f(0,0,1,0);
        GL11.glVertex3d(0.5f,-0.5f,0);
        float aspectRatio = (float) this.getWidth() / this.getHeight();
        //projectionMatrix = new org.lwjgl.opengl.ARBMatrixPalette().setPerspective(FOV, aspectRatio,
               // Z_NEAR, Z_FAR);
        GL11.glEnd();


    }

}