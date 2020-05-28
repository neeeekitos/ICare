package game;

import engine.*;
import engine.graph.*;
import engine.graph.lights.PointLight;
import engine.graph.lights.SpotLight;
import engine.items.SkyBox;
import javafx.geometry.Pos;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.*;

import engine.graph.lights.DirectionalLight;
import engine.items.GameItem;
import engine.items.Terrain;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Vector;

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
        Mesh cubeMesh = OBJLoader.loadMesh("/models/assemblage.obj");
        Material cubeMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        cubeMesh.setMaterial(cubeMaterial);
        cubeGameItem = new GameItem(cubeMesh);
        cubeGameItem.setPosition(0, -1, 0);
        cubeGameItem.setRotation(90,0,0);
        cubeGameItem.setScale(1f);

        //Soleil
        Mesh SoleilMesh = OBJLoader.loadMesh("/models/soleil.obj");
        Material SoleilMaterial = new Material( chercheTexture("textures/soleil.png"), reflectance );
        SoleilMesh.setMaterial(SoleilMaterial);
        SoleilGameItem = new GameItem(SoleilMesh);
        SoleilGameItem.setPosition(0, 1f, 0);
        SoleilGameItem.setRotation(90,0,0);
        SoleilGameItem.setScale(0.5f);

        Mesh quadMesh = OBJLoader.loadMesh("/models/Table.obj");
        Material quadMaterial = new Material( chercheTexture("textures/bois.png") );
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -1, 0);
        quadGameItem.getRotation().z = 90;
        quadGameItem.setScale(2.5f);

        scene.setGameItems(new GameItem[]{cubeGameItem,SoleilGameItem, quadGameItem});

        // Setup Lights
        setupLights();

        SkyBox ciel = new SkyBox("/models/skybox.obj",chercheTexture("textures/skyboxFinal_3.png"));
        scene.setSkyBox(ciel);
        scene.getSceneLight().setSkyBoxLight(new Vector3f(1,1,1));
        scene.getSkyBox().setScale(9f);

        camera.getPosition().z = 3;
        camera.getPosition().y = 1.5f;
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
        Vector3f PointlightDirection_0 = new Vector3f(SoleilGameItem.getPosition().x,SoleilGameItem.getPosition().y,SoleilGameItem.getPosition().z); // Soleil principal
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
        if (window.isKeyPressed(GLFW_KEY_R)) {
            camera.setRotation(camera.getRotation().x+1,camera.getRotation().y,0) ;
        } else if (window.isKeyPressed(GLFW_KEY_F)) {
            camera.setRotation(camera.getRotation().x-1,camera.getRotation().y,0) ;
        }
        if (window.isKeyPressed(GLFW_KEY_T)) {
            camera.setRotation(camera.getRotation().x,camera.getRotation().y+1,0) ;
        } else if (window.isKeyPressed(GLFW_KEY_G)) {
            camera.setRotation(camera.getRotation().x,camera.getRotation().y-1,0) ;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            zenithSoleil += 1;
            angleSoleil(zenithSoleil,azimutSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            zenithSoleil -= 1;
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

        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        Vector3f PointlightDirection = this.scene.getSceneLight().getPointLightList(0).getPosition();
        Vector3f PointlightDirection_2 = this.scene.getSceneLight().getPointLightList(0).getPosition();
        float PositionSoleil = SoleilGameItem.getPosition().y+1 ;
        System.out.println(PositionSoleil);
        if ( PositionSoleil<-0.5){                             // Le soleil est couché
            this.scene.getSceneLight().getPointLightList(0).setIntensity(0);
            this.scene.getSceneLight().getPointLightList(1).setIntensity(0.2f); // on garde une petite luminosité
            this.scene.getSceneLight().setSkyBoxLight(new Vector3f(0.1f,0.1f,0.1f));
            lightDirection.x = 0;
            lightDirection.y = 0;
            lightDirection.z = 0;
        } else {
            this.scene.getSceneLight().getPointLightList(0).setIntensity(PositionSoleil + 0.5f); // l'intensité du soleil oscille entre 1 et 0.75
            this.scene.getSceneLight().getPointLightList(1).setIntensity(PositionSoleil/2 +0.5f);   // l'intensité du soleil' oscille entre 0.6 et 0.2
            lightDirection.x = SoleilGameItem.getPosition().x;
            lightDirection.y = PositionSoleil;
            lightDirection.z = SoleilGameItem.getPosition().z ;
            PointlightDirection.x = SoleilGameItem.getPosition().x;
            PointlightDirection.y = SoleilGameItem.getPosition().y;
            PointlightDirection.z = SoleilGameItem.getPosition().z;
            PointlightDirection_2.x = SoleilGameItem.getPosition().x;
            PointlightDirection_2.y = -0.5f*SoleilGameItem.getPosition().y;
            PointlightDirection_2.z = - 0.5f*SoleilGameItem.getPosition().z;

        }
        coucherDeSoleil(PositionSoleil);
        this.scene.getSceneLight().getPointLightList(0).getAttenuation().setExponent(Math.abs((zenithSoleilpourAffichage/90)+1));
        this.scene.getSceneLight().getDirectionalLight().setIntensity(PositionSoleil/2);
        PointlightDirection.normalize();
        lightDirection.normalize();
        hud.setStatusText("Azimut = " + azimutSoleil+" / zenith (angle) = "+ zenithSoleil+" / zenith (definition) = " + zenithSoleilpourAffichage);

        this.scene.getSkyBox().setPosition(-camera.getPosition().x,-camera.getPosition().y+7.4f,-camera.getPosition().z);
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
        if (Ze>=360 || Ze<=-360){ zenithSoleil=0;}  // Pas obligatoire
        if (Az>=360|| Az<=-360){ azimutSoleil =0;}  // Pas obligatoire non plus
        float Rayon = 3f;
        float radZe = (float) Math.toRadians(Ze);
        float radAz = (float) Math.toRadians(Az);
        float nouvX = (float)( Math.cos(radZe)*Math.cos(radAz)*Rayon);
        float nouvY = (float)( Math.sin(radZe)*Rayon)-2;
        float nouvZ = (float)( Math.cos(radZe)*Math.sin(radAz)*Rayon);
        SoleilGameItem.getPosition().x = nouvX;
        SoleilGameItem.getPosition().y = nouvY;
        SoleilGameItem.getPosition().z = nouvZ;
        AffichageZenith( Ze);

        cubeGameItem.setRotation(90,0,Az);
    }

    public void coucherDeSoleil (float PositionSoleil){
        float Xsoleil = SoleilGameItem.getPosition().x*0.05f;
        float Zsoleil = SoleilGameItem.getPosition().z*0.05f;
        float Coef = PositionSoleil/2;
        Vector3f vecLum = new Vector3f(Coef+Xsoleil+0.14f,Coef+Zsoleil+0.14f,Coef+Zsoleil*Xsoleil+0.14f);
        this.scene.getSceneLight().setSkyBoxLight(vecLum);
    }

    public Texture chercheTexture ( String NomTexture) throws Exception {
        URL res = getClass().getClassLoader().getResource(NomTexture);
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        Texture textureSoleil = new Texture(absolutePath);
        return  textureSoleil;
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

}
