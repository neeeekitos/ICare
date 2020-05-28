package game;

import engine.*;
import engine.graph.*;
import engine.graph.lights.PointLight;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

import engine.graph.lights.DirectionalLight;
import engine.items.GameItem;
import engine.items.Terrain;
import org.lwjgl.opengl.GL11;
import sun.security.provider.certpath.Vertex;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Mesh mesh;
    private Scene scene;

    private Hud hud;

    private static final float CAMERA_POS_STEP = 0.05f;

    private Terrain terrain;

    private GameItem batiGameItem;
    private GameItem axeGameItem;
    private GameItem hautGameItem;

    private GameItem soleilGameItem;
    
    private float azimutSoleil;

    private float zenithSoleil;             //Correspond à un l'angle entre l'axe x et y est compris entre 0 et 360°

    private float zenithSoleilpourAffichage; //Correspond à un l'angle entre le plan x0z et y est compris entre -90° et 90°

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        zenithSoleil = 90;
        zenithSoleilpourAffichage = zenithSoleil;
        azimutSoleil = 0;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();


        // Setup  GameItems
        float reflectance = 1f;
        Mesh cubeMeshBati = OBJLoader.loadMesh("/models/Asm_bati.obj");
        Material cubeMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        cubeMeshBati.setMaterial(cubeMaterial);
        batiGameItem = new GameItem(cubeMeshBati);
        batiGameItem.setPosition(0, -1, 0);
        batiGameItem.getRotation().x = 90;
        batiGameItem.setScale(2.5f); // ne pas changer

        Mesh cubeMeshAxe = OBJLoader.loadMesh("/models/Asm_axe.obj");
        cubeMaterial = new Material(new Vector4f(1, 1, 0, 1), reflectance);
        cubeMeshAxe.setMaterial(cubeMaterial);
        axeGameItem = new GameItem(cubeMeshAxe);
        axeGameItem.setPosition(0, -0.9558927f, 0);
        //axeGameItem.setRotation(90,0,90);
        axeGameItem.getRotation().x = 90;
        axeGameItem.setScale(2.5f); // ne pas changer

        Mesh cubeMeshHaut = OBJLoader.loadMesh("/models/Asm_haut.obj");
        cubeMaterial = new Material(new Vector4f(1, 1, 1, 1), reflectance);
        cubeMeshHaut.setMaterial(cubeMaterial);
        hautGameItem = new GameItem(cubeMeshHaut);
        hautGameItem.setPosition(0, -0.27974615f, 0);
        hautGameItem.setScale(2.5f); // ne pas changer

        //Soleil
        float reflectanceSoleil = 1f;
        Mesh SoleilMesh = OBJLoader.loadMesh("/models/soleil.obj");
        Material SoleilMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectanceSoleil);
        SoleilMesh.setMaterial(SoleilMaterial);
        soleilGameItem = new GameItem(SoleilMesh);
        soleilGameItem.setPosition(0, 2, 0);
        //soleilGameItem.setRotation(90,0,0);
        soleilGameItem.setScale(5f);

        Mesh quadMesh = OBJLoader.loadMesh("/models/plane.obj");
        Material quadMaterial = new Material(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -1, 1);
        quadGameItem.setScale(2.5f);

        scene.setGameItems(new GameItem[]{batiGameItem, axeGameItem, hautGameItem, soleilGameItem, quadGameItem});

        // Setup Lights
        setupLights();

        camera.getPosition().z = 2;
        camera.getPosition().y = 1;
        camera.getRotation().x = 30;
        hud = new Hud("Azimut Angle:");
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);
        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.0f, 0.0f, 0.0f));
        //sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1f, 1f));

        // Directional Light
        float lightIntensity = 1f;
        Vector3f lightDirection = new Vector3f(0, 0, 0.0f);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(5);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);

        // Point Light Représentant le soleil
        PointLight [] source = new PointLight[2];
        float Point_lightIntensity = 0.8f;
        Vector3f PointlightDirection_0 = new Vector3f(soleilGameItem.getPosition().x, soleilGameItem.getPosition().y, soleilGameItem.getPosition().z); // Soleil principal
        PointLight.Attenuation AttenuationSoleil = new PointLight.Attenuation(0,0,0.7f);
        source [0] = new PointLight(new Vector3f(1,1,1),PointlightDirection_0,Point_lightIntensity);
        source [0].setAttenuation(AttenuationSoleil);
        Vector3f PointlightDirection_1 = new Vector3f(0, -0.2f, 0.5f); // Soleil' éclaire la surface par en dessous sinon il y a de l'ombre sur le soleil
        source [1] = new PointLight(new Vector3f(1,1,1),PointlightDirection_1,0.6f);
        sceneLight.setPointLightList(source);

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            // limiter zenithSoleil : toujours < 180
            zenithSoleil = Math.min(++zenithSoleil, 180);
            angleSoleil(zenithSoleil,azimutSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            // limiter zenithSoleil : toujours > 0
            zenithSoleil = Math.max(--zenithSoleil, 0);
            angleSoleil(zenithSoleil,azimutSoleil);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            azimutSoleil -= 1;
            angleSoleil(zenithSoleil,azimutSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            azimutSoleil += 1;
            angleSoleil(zenithSoleil,azimutSoleil);
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera based on mouse            
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain != null ? terrain.getHeight(camera.getPosition()) : -Float.MAX_VALUE;
        if (camera.getPosition().y <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        //mettre à jour la position du panneau et de l'axe vertical
        updatePosition();


//        //tourner l'axe avec la partie haute autour de l'axe vertical
//        hautGameItem.getRotation().z = azimutSoleil;
//        axeGameItem.getRotation().z = azimutSoleil+90; //car décalé par rapport à la base haute

//        Math.toRadians(azimutSoleil+90);
//        float radAz = (float) Math.toRadians(azimutSoleil+90);
//        axeGameItem.getRotation().x= (float)( Math.cos(radAz)*Math.cos(radAz));
//        axeGameItem.getRotation().y = (float)( Math.cos(radAz)*Math.sin(radAz));


//        //tourner que la partie haute autour de l'axe horizontal
//        float radAz = (float) Math.toRadians(azimutSoleil);
//        hautGameItem.getRotation().x = (float) (zenithSoleil*Math.cos(radAz));
//        hautGameItem.getRotation().z = (float) (zenithSoleil*Math.sin(radAz));

        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        Vector3f PointlightDirection = this.scene.getSceneLight().getPointLightList(0).getPosition();
        if ( soleilGameItem.getPosition().y +1<-0.1){                             // Le soleil est couché
            this.scene.getSceneLight().getPointLightList(0).setIntensity(0);
            this.scene.getSceneLight().getPointLightList(1).setIntensity(0.2f); // on garde une petite luminosité
            lightDirection.x = 0;
            lightDirection.y = 0;
            lightDirection.z = 0;
        } else {
            this.scene.getSceneLight().getPointLightList(0).setIntensity((soleilGameItem.getPosition().y +1)*0.25f+0.75f); // l'intensité du soleil oscille entre 1 et 0.75
            this.scene.getSceneLight().getPointLightList(1).setIntensity((soleilGameItem.getPosition().y +1)*0.4f+0.2f);   // l'intensité du soleil' oscille entre 0.6 et 0.2
            lightDirection.x = soleilGameItem.getPosition().x;
            lightDirection.y = soleilGameItem.getPosition().y +1;
            lightDirection.z = soleilGameItem.getPosition().z ;
            PointlightDirection.x = soleilGameItem.getPosition().x;
            PointlightDirection.y = soleilGameItem.getPosition().y +1;
            PointlightDirection.z = soleilGameItem.getPosition().z;
        }
        this.scene.getSceneLight().getPointLightList(0).getAttenuation().setExponent(0.75f*Math.abs((zenithSoleilpourAffichage/90)+1));
        this.scene.getSceneLight().getDirectionalLight().setIntensity(soleilGameItem.getPosition().y +1);
        PointlightDirection.normalize();
        lightDirection.normalize();
        hud.setStatusText("Azimut = " + azimutSoleil+" / zenith (angle) = "+ zenithSoleil+" / zenith (definition) = " + zenithSoleilpourAffichage);

    }

    @Override
    public void render(Window window) {
        if (hud != null) {
            hud.updateSize(window);
        }
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        scene.cleanup();
        if (hud != null) {
            hud.cleanup();
        }
    }

    public void angleSoleil( float Ze, float Az ){
        if (Ze>=360 || Ze<=-360){ zenithSoleil=0;}
        if (Az>=360|| Az<=-360){ azimutSoleil =0;}
        float radZe = (float) Math.toRadians(Ze);
        float radAz = (float) Math.toRadians(Az);
        float nouvX = (float)( 3*Math.cos(radZe)*Math.cos(radAz));
        float nouvY = (float)( 3*Math.sin(radZe)-1);
        float nouvZ = (float)( 3*Math.cos(radZe)*Math.sin(radAz));
        soleilGameItem.getPosition().x = nouvX;
        soleilGameItem.getPosition().y = nouvY;
        soleilGameItem.getPosition().z = nouvZ;
        //AffichageZenith(Ze);
    }

    public void AffichageZenith ( float Ze){
        if (zenithSoleil >= -360 && zenithSoleil < -270) {
            zenithSoleilpourAffichage = 360 + zenithSoleil;
        } else if (zenithSoleil >= -270 && zenithSoleil < -90) {
            zenithSoleilpourAffichage = -180 - zenithSoleil;
        } else if (zenithSoleil >= -90 && zenithSoleil < 90) {
            zenithSoleilpourAffichage =  zenithSoleil ;
        }else if (zenithSoleil >= 90 && zenithSoleil < 270) {
            zenithSoleilpourAffichage = 180-zenithSoleil;
        }else if (zenithSoleil >= 270 && zenithSoleil < 360) {
            zenithSoleilpourAffichage = zenithSoleil - 360  ;
        }
    }

    public void updatePosition() {
        //update tout d'abord y, puis z
        hautGameItem.getRotation().y = azimutSoleil;
        hautGameItem.getRotation().z = 90-zenithSoleil;

        //ici on tourne autour du z car l'axe a un autre repere
        axeGameItem.getRotation().z = azimutSoleil;

        //        hautGameItem.setRotation(0,0,0);
//        hautGameItem.getRotation().z = azimutSoleil;
//        axeGameItem.getRotation().z = azimutSoleil; //car décalé par rapport à la base haute

//        glPushMatrix();
//        glLoadIdentity();
//        float rotX = hautGameItem.getRotation().x;
//        float rotY = hautGameItem.getRotation().y;
//        float rotZ = 0;
//        glRotatef(rotX, 1.0f, 0.0f, 0.0f);
//        glRotatef(rotY, 0.0f, 1.0f, 0.0f);
//        glRotatef(rotZ, 0.0f, 0.0f, 1.0f);
//        glLineWidth(2.0f);
//
//        glBegin(GL_LINES);
//        // X Axis
//        glColor3f(1.0f, 0.0f, 0.0f);
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(1.0f, 0.0f, 0.0f);
//        // Y Axis
//        glColor3f(0.0f, 1.0f, 0.0f);
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(0.0f, 1.0f, 0.0f);
//        // Z Axis
//        glColor3f(1.0f, 1.0f, 1.0f);
//        glVertex3f(0.0f, 0.0f, 0.0f);
//        glVertex3f(0.0f, 0.0f, 1.0f);
//        glEnd();
//
//        glPopMatrix();
//        float radAz = (float) Math.toRadians(azimutSoleil);
//        hautGameItem.getRotation().x = (float) (zenithSoleil*Math.cos(radAz));
//        hautGameItem.getRotation().z = (float) (zenithSoleil*Math.sin(radAz));
    }

}
