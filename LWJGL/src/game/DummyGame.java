package game;

import engine.*;
import engine.graph.lights.PointLight;
import engine.graph.lights.SpotLight;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

import engine.graph.Camera;
import engine.graph.Material;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Renderer;
import engine.graph.lights.DirectionalLight;
import engine.items.GameItem;
import engine.items.Terrain;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private static final float CAMERA_POS_STEP = 0.05f;

    private Terrain terrain;

    private GameItem cubeGameItem;

    private GameItem SoleilGameItem;
    
    private float azimutSoleil;

    private float zenithSoleil;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        zenithSoleil = 90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        // Setup  GameItems
        float reflectance = 1f;
        Mesh cubeMesh = OBJLoader.loadMesh("/models/assemblage.obj");
        Material cubeMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        cubeMesh.setMaterial(cubeMaterial);
        cubeGameItem = new GameItem(cubeMesh);
        cubeGameItem.setPosition(0, -1, 0);
        cubeGameItem.setRotation(90,0,0);
        cubeGameItem.setScale(0.5f);

        //Soleil
        float reflectanceSoleil = 1f;
        Mesh SoleilMesh = OBJLoader.loadMesh("/models/soleil.obj");
        Material SoleilMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectanceSoleil);
        SoleilMesh.setMaterial(SoleilMaterial);
        SoleilGameItem = new GameItem(SoleilMesh);
        SoleilGameItem.setPosition(0, 0f, 0);
        SoleilGameItem.setRotation(90,0,0);
        SoleilGameItem.setScale(0.5f);

        Mesh quadMesh = OBJLoader.loadMesh("/models/plane.obj");
        Material quadMaterial = new Material(new Vector4f(0.0f, 0.0f, 1.0f, 1.0f), reflectance);
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -1, 1);
        quadGameItem.setScale(4f);

        scene.setGameItems(new GameItem[]{cubeGameItem,SoleilGameItem, quadGameItem});

        // Setup Lights
        setupLights();

        camera.getPosition().z = 5;
        camera.getPosition().y = 2f;
        camera.getRotation().x = 30;
        hud = new Hud("Light Angle:");
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

        // Point Light Représente le soleil
        PointLight [] source = new PointLight[2];
        float Point_lightIntensity = 0.8f;
        Vector3f PointlightDirection_0 = new Vector3f(0, 0, -0.5f); // Soleil principal
        PointLight.Attenuation AttenuationSoleil = new PointLight.Attenuation(0,0,0.7f);
        source [0] = new PointLight(new Vector3f(1,1,1),PointlightDirection_0,Point_lightIntensity);
        source [0].setAttenuation(AttenuationSoleil);
        Vector3f PointlightDirection_1 = new Vector3f(0, -0.2f, 0.5f); // Soleil' éclaire la surface par en dessous sinon ombre
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
            zenithSoleil += 1;
            ZenithSoleil(zenithSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            zenithSoleil -= 1;
            ZenithSoleil(zenithSoleil);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            azimutSoleil -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            azimutSoleil += 0.05f;
        } else {
            azimutSoleil = 0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
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

        float rotY = cubeGameItem.getRotation().z;
        rotY += 0.5f;
        if ( rotY >= 360 ) {
            rotY -= 360;
        }
        cubeGameItem.getRotation().z = rotY;

        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        Vector3f PointlightDirection = this.scene.getSceneLight().getPointLightList(0).getPosition();
        if ( SoleilGameItem.getPosition().y +1<-0.1){                             // Le soleil est couché
            this.scene.getSceneLight().getPointLightList(0).setIntensity(0);
            this.scene.getSceneLight().getPointLightList(1).setIntensity(0.2f); // on garde une petite luminosité
            lightDirection.x = 0;
            lightDirection.y = 0;
            lightDirection.z = 0;
        } else {
            this.scene.getSceneLight().getPointLightList(0).setIntensity((SoleilGameItem.getPosition().y +1)*0.25f+0.75f); // l'intensité du soleil oscille entre 1 et 0.75
            this.scene.getSceneLight().getPointLightList(1).setIntensity((SoleilGameItem.getPosition().y +1)*0.4f+0.2f);   // l'intensité du soleil' oscille entre 0.6 et 0.2
            lightDirection.x = SoleilGameItem.getPosition().x;
            lightDirection.y = SoleilGameItem.getPosition().y +1;
            lightDirection.z = 0.5f;
            PointlightDirection.x = SoleilGameItem.getPosition().x;
            PointlightDirection.y = SoleilGameItem.getPosition().y +1;
            PointlightDirection.z = 0f;
        }
        this.scene.getSceneLight().getPointLightList(0).getAttenuation().setExponent(Math.abs(0.7f*(SoleilGameItem.getPosition().x)));
        this.scene.getSceneLight().getDirectionalLight().setIntensity(SoleilGameItem.getPosition().y +1);
        PointlightDirection.normalize();
        lightDirection.normalize();

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

    public void ZenithSoleil( float angle ){
        float rad = (float) ( angle*Math.PI/180);
        float nouvX = (float)( Math.cos(2*rad+Math.PI/2)) ;
        float nouvY = (float)( Math.sin(2*rad+3*Math.PI/2)-1) ;
        SoleilGameItem.getPosition().x = nouvX;
        SoleilGameItem.getPosition().y = nouvY;
    }
}
