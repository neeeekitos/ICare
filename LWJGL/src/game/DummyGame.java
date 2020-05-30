package game;

import engine.*;
import engine.graph.*;
import engine.graph.lights.PointLight;
import engine.items.SkyBox;
import engine.items.Soleil;
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
import java.nio.file.Paths;


public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;

    private Scene scene;

    private boolean isManualMode;

    Rotation rotateMoteurs;

    private Hud hud;

    private static final float CAMERA_POS_STEP = 0.05f;

    private Terrain terrain;


    private Soleil SoleilGameItem;

    private int touchCounter = 0;

    private GameItem SoleilGameItem;
    private GameItem basMdfItem;
    private GameItem basAcierItem;
    private GameItem basPignonItem;
    private GameItem axeAcierItem;
    private GameItem axeMdfItem;
    private GameItem axePlastiqueItem;
    private GameItem hautMdfItem;
    private GameItem hautAcierItem;
    private GameItem hautPlastiqueItem;
    private GameItem hautPmmaItem;
    private GameItem hautPanneauItem;
    private GameItem basBouleItem;
    private GameItem batiGameItem;
    private GameItem axeGameItem;
    private GameItem hautGameItem;

    private float azimutSoleil;

    private float zenithSoleil;             //Correspond à un l'angle entre l'axe x et y est compris entre 0 et 360°

    private float zenithSoleilpourAffichage; //Correspond à un l'angle entre le plan x0z et y est compris entre -90° et 90°

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        rotateMoteurs = new Rotation(0,0);


        // Setup  GameItems
        Texture textureMDF = chercheTexture("textures/marron_clair.png");
        Texture textureAcier = chercheTexture("textures/acier.png");
        Texture textureBlanc = chercheTexture("textures/blanc.png");

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

        //Assemblage bas
        Mesh asmBasMdf = OBJLoader.loadMesh("/models/Asm_bas_MDF.obj");
        Material asmBasMdfMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        asmBasMdfMaterial.setTexture(textureMDF);
        asmBasMdf.setMaterial(asmBasMdfMaterial);
        basMdfItem = new GameItem(asmBasMdf);
        basMdfItem.setPosition(0, -1, 0);
        basMdfItem.setRotation(90,0,0);
        basMdfItem.setScale(0.5f);

        Mesh asmBasAcier = OBJLoader.loadMesh("/models/Asm_bas_Metal.obj");
        Material asmBasAcierMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        asmBasAcierMaterial.setTexture(textureAcier);
        asmBasAcier.setMaterial(asmBasAcierMaterial);
        basAcierItem = new GameItem(asmBasAcier);
        basAcierItem.setPosition(0, -1, 0);
        basAcierItem.setRotation(90,0,0);
        basAcierItem.setScale(0.5f);

        Mesh asmBasPignon = OBJLoader.loadMesh("/models/Asm_bas_Pignon.obj");
        Material asmBasPignonMaterial = new Material(new Vector4f(0.7f, 0, 0.12f, 1), reflectance);
        asmBasPignon.setMaterial(asmBasPignonMaterial);
        basPignonItem = new GameItem(asmBasPignon);
        basPignonItem.setPosition(0, -1, 0);
        basPignonItem.setRotation(90,0,0);
        basPignonItem.setScale(0.5f);

        //Assemblage axe
        Mesh asmAxeAcier = OBJLoader.loadMesh("/models/Asm_Axe_acier.obj");
        Material asmAxeAcierMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        asmAxeAcierMaterial.setTexture(textureAcier);
        asmAxeAcier.setMaterial(asmAxeAcierMaterial);
        axeAcierItem = new GameItem(asmAxeAcier);
        axeAcierItem.setPosition(0, (float) -0.992, 0);
        axeAcierItem.setRotation(90,0,0);
        axeAcierItem.setScale(0.5f);

        Mesh asmAxeMdf = OBJLoader.loadMesh("/models/Asm_Axe_MDF.obj");
        Material asmAxeMdfMaterial = new Material(new Vector4f(0, 1, 0, 1), reflectance);
        asmAxeMdfMaterial.setTexture(textureMDF);
        asmAxeMdf.setMaterial(asmAxeMdfMaterial);
        axeMdfItem = new GameItem(asmAxeMdf);
        axeMdfItem.setPosition(0, (float) -0.992, 0);
        axeMdfItem.setRotation(90,0,0);
        axeMdfItem.setScale(0.5f);

        Mesh asmAxePLastique = OBJLoader.loadMesh("/models/Asm_Axe_plastique.obj");
        Material asmAxePlastiqueMaterial = new Material(new Vector4f(0.7f, 0, 0.12f, 1), reflectance);
        asmAxePLastique.setMaterial(asmAxePlastiqueMaterial);
        axePlastiqueItem = new GameItem(asmAxePLastique);
        axePlastiqueItem.setPosition(0, (float) -0.992, 0);
        axePlastiqueItem.setRotation(90,0,0);
        axePlastiqueItem.setScale(0.5f);

        Mesh asmBasBoule = OBJLoader.loadMesh("/models/Asm_bas_Boule.obj");
        Material asmBasBouleMaterial = new Material(new Vector4f(1f, 1f, 1f, 0.195f), reflectance);
        asmBasBoule.setMaterial(asmBasBouleMaterial);
        basBouleItem = new GameItem(asmBasBoule);
        basBouleItem.setPosition(0, -1, 0);
        basBouleItem.setRotation(90,0,0);
        basBouleItem.setScale(0.5f);

        //assemblage haut
        Mesh asmHautMdf = OBJLoader.loadMesh("/models/Asm_Haut_Bois.obj");
        Material asmHautMdfMaterial = new Material(new Vector4f(1, 1, 1, (float) 0.2), reflectance);
        asmHautMdfMaterial.setTexture(textureMDF);
        asmHautMdf.setMaterial(asmHautMdfMaterial);
        hautMdfItem = new GameItem(asmHautMdf);
        hautMdfItem.setPosition(0, (float) -0.856, 0);
        hautMdfItem.setRotation(0,0,0);
        hautMdfItem.setScale(0.5f);

        Mesh asmHautAcier = OBJLoader.loadMesh("/models/Asm_Haut_Metal.obj");
        Material asmAcierMdfMaterial = new Material(new Vector4f(1, 1, 1, (float) 0.2), reflectance);
        asmAcierMdfMaterial.setTexture(textureAcier);
        asmHautAcier.setMaterial(asmAcierMdfMaterial);
        hautAcierItem = new GameItem(asmHautAcier);
        hautAcierItem.setPosition(0, (float) -0.856, 0);
        hautAcierItem.setRotation(0,0,0);
        hautAcierItem.setScale(0.5f);

        Mesh asmHautPlastique = OBJLoader.loadMesh("/models/Asm_Haut_plastique.obj");
        Material asmHautPlastiqueMaterial = new Material(new Vector4f(0.7f, 0, 0.12f, 1), reflectance);
        //asmHautPlastiqueMaterial.setTexture(textureMDF);
        asmHautPlastique.setMaterial(asmHautPlastiqueMaterial);
        hautPlastiqueItem = new GameItem(asmHautPlastique);
        hautPlastiqueItem.setPosition(0, (float) -0.856, 0);
        hautPlastiqueItem.setRotation(0,0,0);
        hautPlastiqueItem.setScale(0.5f);

        Mesh asmHautPmma = OBJLoader.loadMesh("/models/Asm_Haut_pmma.obj");
        Material asmHautPmmaMaterial = new Material(new Vector4f(1, 1, 0, (float) 0.4), reflectance);
        //asmHautPmmaMaterial.setTexture(textureMDF);
        asmHautPmma.setMaterial(asmHautPmmaMaterial);
        hautPmmaItem = new GameItem(asmHautPmma);
        hautPmmaItem.setPosition(0, (float) -0.856, 0);
        hautPmmaItem.setRotation(0,0,0);
        hautPmmaItem.setScale(0.5f);

        Mesh asmHautPanneau = OBJLoader.loadMesh("/models/Asm_Haut_panneau.obj");
        Material asmHautPanneauMaterial = new Material(new Vector4f(1, 1, 1,1), 1f);
        asmHautPanneauMaterial.setTexture(textureAcier);
        asmHautPanneau.setMaterial(asmHautPanneauMaterial);
        hautPanneauItem = new GameItem(asmHautPanneau);
        hautPanneauItem.setPosition(0, (float) -0.856, 0);
        hautPanneauItem.setRotation(0,0,0);
        hautPanneauItem.setScale(0.5f);
        
        //Soleil
        Mesh SoleilMesh = OBJLoader.loadMesh("/models/soleil.obj");
        Material SoleilMaterial = new Material( chercheTexture("textures/soleil.png"),reflectance );
        SoleilMesh.setMaterial(SoleilMaterial);
        SoleilGameItem = new Soleil(SoleilMesh,scene);
        SoleilGameItem.setPosition(0, 1.5f, 0);
        SoleilGameItem.setRotation(90,0,0);
        SoleilGameItem.setScale(0.5f);
        zenithSoleil = SoleilGameItem.getZenithSoleil();
        azimutSoleil = SoleilGameItem.getAzimutSoleil();
        zenithSoleilpourAffichage = zenithSoleil;

        Mesh quadMesh = OBJLoader.loadMesh("/models/Table.obj");
        Material quadMaterial = new Material( chercheTexture("textures/bois.png") );
        quadMesh.setMaterial(quadMaterial);
        GameItem quadGameItem = new GameItem(quadMesh);
        quadGameItem.setPosition(0, -1, 0);
        quadGameItem.getRotation().z = 90;
        quadGameItem.setScale(2.5f);


        scene.setGameItems(new GameItem[]{ SoleilGameItem,basPignonItem,basMdfItem,basAcierItem, axePlastiqueItem,
                axeMdfItem, basBouleItem, axeAcierItem, hautMdfItem, hautAcierItem, hautPanneauItem, hautPlastiqueItem, hautPmmaItem,quadGameItem});

        // Setup Lights
        SoleilGameItem.setupLights();

        SkyBox ciel = new SkyBox("/models/skybox.obj",chercheTexture("textures/skyboxFinal_3.png"));
        scene.setSkyBox(ciel);
        scene.getSceneLight().setSkyBoxLight(new Vector3f(1,1,1));
        scene.getSkyBox().setScale(9f);

        camera.getPosition().z = 3;
        camera.getPosition().y = 1.5f;
        camera.getRotation().x = 25;
        hud = new Hud("Azimut Angle:");
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        if (window.isKeyPressed(GLFW_KEY_M)) {
            if (touchCounter < 1) {
                isManualMode = !isManualMode; // switcher le mode manuel
                System.out.println("Manual mode is " + ((isManualMode) ? "ON" : "OFF"));
            }
            touchCounter++;
        } else { touchCounter = 0; }
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
        if (window.isKeyPressed(GLFW_KEY_UP) && isManualMode) {
            // limiter zenithSoleil : toujours < 180
            zenithSoleil++;
            SoleilGameItem.setZenithSoleil(zenithSoleil);
            SoleilGameItem.angleSoleil(zenithSoleil,azimutSoleil);
            rotateMoteurs.getMoteurHaut().setAngle(90-zenithSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_DOWN) && isManualMode) {
            // limiter zenithSoleil : toujours > 0
            zenithSoleil--;
            SoleilGameItem.setZenithSoleil(zenithSoleil);
            SoleilGameItem.angleSoleil(zenithSoleil,azimutSoleil);
            rotateMoteurs.getMoteurHaut().setAngle(90-zenithSoleil);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT) && isManualMode) {
            azimutSoleil -= 1;
            SoleilGameItem.setAzimutSoleil(azimutSoleil);
            SoleilGameItem.angleSoleil(zenithSoleil,azimutSoleil);
            rotateMoteurs.getMoteurBas().setAngle(azimutSoleil);
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT) && isManualMode) {
            azimutSoleil += 1;
            SoleilGameItem.setAzimutSoleil(azimutSoleil);
            SoleilGameItem.angleSoleil(zenithSoleil,azimutSoleil);
            rotateMoteurs.getMoteurBas().setAngle(azimutSoleil);
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

        SoleilGameItem.mise_a_jour();   //Luminosité en fonction de la position du soleil
        SoleilGameItem.coucherDeSoleil();
        hud.setStatusText("Azimut = " + SoleilGameItem.getAzimutSoleil()+" / zenith (angle) = "+ SoleilGameItem.getZenithSoleil()+" zenith (definition) = " + SoleilGameItem.getZenithSoleilpourAffichage());

        updatePosition();

        this.scene.getSkyBox().setPosition(-camera.getPosition().x,-camera.getPosition().y+7.4f,-camera.getPosition().z);
        sortieCamera();
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

    public Texture chercheTexture ( String NomTexture) throws Exception {
        URL res = getClass().getClassLoader().getResource(NomTexture);
        File file = Paths.get(res.toURI()).toFile();
        String absolutePath = file.getAbsolutePath();
        Texture texture = new Texture(absolutePath);
        return  texture;
    }

    public void sortieCamera(){
        float Xcam = camera.getPosition().x;
        float Ycam = camera.getPosition().y;
        float Zcam = camera.getPosition().z;
        float limite = scene.getSkyBox().getScale();
        limite = limite - limite/15;
        if (Xcam <= -limite ){ camera.setPosition(-limite ,Ycam,Zcam); }
        if (Xcam >= limite ){ camera.setPosition(limite ,Ycam,Zcam); }
        if (Ycam <= -1.4 ){ camera.setPosition(Xcam,-1.4f,Zcam); }
        if (Ycam > 12 ){ camera.setPosition(Xcam,12,Zcam); }
        if (Zcam <= -limite ){ camera.setPosition(Xcam,Ycam,-limite); }
        if (Zcam > limite ){ camera.setPosition(Xcam,Ycam,limite); }
    }



    public void updatePosition() {

        rotateMoteurs.getMoteurBas().tournerVerticalement(new GameItem[] {hautPlastiqueItem, hautPmmaItem, hautPanneauItem, hautAcierItem, hautMdfItem});
        rotateMoteurs.getMoteurHaut().tournerHorizontalement(new GameItem[] {hautPlastiqueItem, hautPmmaItem, hautPanneauItem, hautAcierItem, hautMdfItem});

        axeAcierItem.getRotation().z = azimutSoleil;
        axeMdfItem.getRotation().z = azimutSoleil;
        axePlastiqueItem.getRotation().z = azimutSoleil;
    }

}
